package edu.brown.cs.internhelper.Main;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import com.google.common.collect.ImmutableMap;
import com.google.firebase.cloud.FirestoreClient;
import edu.brown.cs.internhelper.CSV.CSVParser;
import edu.brown.cs.internhelper.Database.SQLDatabase;
import edu.brown.cs.internhelper.Functionality.CachePageRanks;
import edu.brown.cs.internhelper.Functionality.Experience;
import edu.brown.cs.internhelper.Functionality.Job;
import edu.brown.cs.internhelper.Functionality.JobGraphBuilder;
import edu.brown.cs.internhelper.Functionality.TextSimilarity;
import edu.brown.cs.internhelper.Functionality.User;
import edu.brown.cs.internhelper.Graph.DirectedGraph;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import org.json.JSONException;
import org.json.JSONObject;
import spark.ExceptionHandler;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.Spark;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import com.google.gson.Gson;


/**
 * The Main class of our project. This is where execution begins.
 */
public final class Main {

  private static final int DEFAULT_PORT = 4567;
  private static final double QUARTER = 0.25;
  private static final double POINTTHREE = 0.3;
  private static final double HALF = 0.5;
  private static final double POINTSEVEN = 0.7;
  private static final double POINTSEVENNINE = 0.79;
  private static final double POINTEIGHT = 0.8;
  private static final double POINTNINENINENINENINE = 0.9999;
  private static final double ONEANDQUARTER = 1.25;
  private static final double ONEANDHALF = 1.5;
  private static final int SEVEN = 7;
  private static final Gson GSON = new Gson();
  private static final MyFirebase FB = new MyFirebase();


  /**
   * The initial method called when execution begins.
   *
   * @param args An array of command line arguments
   */
  public static void main(String[] args) {
    try {
      new Main(args).run();
    } catch (Exception e) {
      e.printStackTrace();
      System.out.println("ERROR: oops something went wrong");
    }
  }

  private final String[] args;

  private Main(String[] args) {
    this.args = args;
  }

  private void run() {
    // Parse command line arguments
    OptionParser parser = new OptionParser();
    parser.accepts("gui");
    // New accepted argument to run pagerank script
    parser.accepts("pagerank");
    parser.accepts("port").withRequiredArg().ofType(Integer.class)
            .defaultsTo(DEFAULT_PORT);
    OptionSet options = parser.parse(args);
    if (options.has("gui")) {
      runSparkServer((int) options.valueOf("port"));
    }
    if (options.has("pagerank")) { //do this to run page rank on the roles within database
      CachePageRanks cachePageRanks = new CachePageRanks();
      cachePageRanks.cacheResults();
    }
  }

  private void runSparkServer(int port) {
    FB.setUp();
    Spark.port(getHerokuAssignedPort());
    Spark.externalStaticFileLocation("src/main/resources/static");
    Spark.options("/*", (request, response) -> {
      String accessControlRequestHeaders = request.headers("Access-Control-Request-Headers");
      if (accessControlRequestHeaders != null) {
        response.header("Access-Control-Allow-Headers", accessControlRequestHeaders);
      }

      String accessControlRequestMethod = request.headers("Access-Control-Request-Method");

      if (accessControlRequestMethod != null) {
        response.header("Access-Control-Allow-Methods", accessControlRequestMethod);
      }
      return "OK";
    });
    Spark.before((request, response) -> response.header("Access-Control-Allow-Origin", "*"));
    Spark.exception(Exception.class, new ExceptionPrinter());
    Spark.get("/userJobResults", new UserJobResultsHandler());
    Spark.post("/userJobResults", new UserJobResultsHandler());
    Spark.get("/suggestedRoles", new DatabaseSuggestedRolesHandler());
    Spark.post("/suggestedRoles", new DatabaseSuggestedRolesHandler());
  }

  static int getHerokuAssignedPort() {
    ProcessBuilder processBuilder = new ProcessBuilder();
    if (processBuilder.environment().get("PORT") != null) {
      Map<String, Object> docData = new HashMap<>();
      docData.put("port",
          String.valueOf(Integer.parseInt(processBuilder.environment().get("PORT"))));
      Firestore db = FirestoreClient.getFirestore();
      ApiFuture<WriteResult> future
          = db.collection("port-data").document("port-number").set(docData);
      return Integer.parseInt(processBuilder.environment().get("PORT"));
    }
    return DEFAULT_PORT; //return default port if heroku-port isn't set (i.e. on localhost)
  }


  /**
   * Takes in user resume prior experience titles and then looks into database of all types of
   * internship positions and then outputs list of roles with highest similarity to user's
   * prior experiences that indicate roles we think the user may be interested in applying for.
   */
  private static class DatabaseSuggestedRolesHandler implements Route {

    @Override
    public String handle(Request req, Response res) throws JSONException,
            ExecutionException, InterruptedException {
      JSONObject data = new JSONObject(req.body());
      String id = data.getString("id");
      User user = FB.getFirebaseResumeData(id);

      List<String> databaseRoles = new ArrayList<>();
      try {
        SQLDatabase db = new SQLDatabase();
        db.connectDatabase("jdbc:sqlite:data/python_scripts/internships.sqlite3");
        databaseRoles = db.getTableNames(); //contains list of all roles we have in database
        db.getConn().close();
      } catch (Exception e) {
        e.printStackTrace();
      }

      Set<String> suggestedRoles = new HashSet<>();

      //iterating through all experiences to then find roles within our database that we can
      //recommend to the user
      for (Experience experience : user.getResume().getResumeExperiences()) {
        //removes intern from experience title so that doesn't contribute to similarity score
        String experienceTitle = experience.getTitle().replaceAll("Intern", "");
        experienceTitle = experienceTitle.replaceAll("intern", "");

        Map<String, Double> unSortedMap = new HashMap<>();

        //iterates through all roles within our database
        for (String databaseRole : databaseRoles) {
          TextSimilarity similarityCalculator = new TextSimilarity();
          try {
            similarityCalculator.loadStopWords("data/stopwords/stopwords.txt");
          } catch (Exception e) {
            e.printStackTrace();
          }
          //removes intern from internship role title so that doesn't contribute to similarity score
          String modifiedDatabaseRoleTitle = databaseRole.replaceAll("Intern", "");
          modifiedDatabaseRoleTitle = modifiedDatabaseRoleTitle.replaceAll("intern",
              "");

          //removes stop words from experience and internship role title
          Set<String> resumeSet = similarityCalculator.removeStopWords(experienceTitle);
          Set<String> databaseRoleSet
              = similarityCalculator.removeStopWords(modifiedDatabaseRoleTitle);
          //creates set of common words between the resume experience and internship role
          Set<String> commonWords = similarityCalculator.commonWords(databaseRoleSet, resumeSet);
          //calculates similarity score
          double similarity = (double) (commonWords.size()) / (resumeSet.size());
          //stores database internship role title and similarity
          unSortedMap.put(databaseRole, similarity);
        }

        //sort the map so that the role with the highest similarity is the first entry
        Map<String, Double> result = unSortedMap.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toMap(
                    Map.Entry::getKey,
                    Map.Entry::getValue,
                    (oldValue, newValue) -> oldValue, LinkedHashMap::new));

        //represents first entry in the map, which now that it is sorted is the highest similarity
        Map.Entry<String, Double> entWithMaxVal = result.entrySet().iterator().next();
        //since there can be multiple roles with the highest similarity, we want to be able to
        //display all of them so we add it to suggestedRoles list
        if (entWithMaxVal.getValue() != 0.0) {
          result.forEach((k, val) -> {
            if (val.equals(entWithMaxVal.getValue())) {
              suggestedRoles.add(k);
            }
          });
        }
      }

      Map<String, Object> variables = ImmutableMap.of("suggestedRoles", suggestedRoles);
      return GSON.toJson(variables);
    }
  }

  /**
   * Handles displaying list of job results that are most similar to the user's resume.
   */
  private static class UserJobResultsHandler implements Route {

    @Override
    public String handle(Request req, Response res) throws JSONException, ExecutionException,
            InterruptedException {

      JSONObject data;
      data = new JSONObject(req.body());
      String role = data.getString("role");
      String id = data.getString("id");
      User user = FB.getFirebaseResumeData(id);

      Map<Job, Double> pageRanks = new HashMap<>();

      String fileName = "data/page_rank_results/" + role + "pr.csv"; //accesses page rank results
      //of particular role that the user has selected
      String line = null;
      try {
        Path pathName = Path.of(fileName);
        String text = Files.readString(pathName);
        text = text.replace("\n", "").replace("\r", "");

        Reader inputString = new StringReader(text);

        BufferedReader reader = new BufferedReader(inputString);
        CSVParser csvParser = new CSVParser();
        line = reader.readLine();
        String[] splitLines = csvParser.parseCSV(line);

        int numberJobEntries = splitLines.length / SEVEN; //divide by 7 because there are 7 columns
        //and so each job will be represented by the length of the split array divided by 7

        if (numberJobEntries > 1) {
          for (int i = SEVEN; i < splitLines.length; i = i + SEVEN) {
            Job job = new Job(); //creates a Java object of the job within the csv and sets
            //attributes
            job.setId(Integer.parseInt(splitLines[i]));
            job.setTitle(splitLines[i + 1]);
            job.setCompany(splitLines[i + 2]);
            job.setLocation(splitLines[i + 3]);
            job.setRequiredQualifications(splitLines[i + 4]);
            job.setLink(splitLines[i + 5]);
            double pageRank = Double.valueOf(splitLines[i + 6]);
            pageRanks.put(job, pageRank); //puts page rank read from csv into map
          }
        }
      } catch (Exception e) {
        e.printStackTrace();
      }

      List<Job> jobs = new ArrayList<>();
      DirectedGraph graph = new DirectedGraph();
      JobGraphBuilder graphBuilder = new JobGraphBuilder(jobs, graph);
      Map<Job, Double> jobResults = graphBuilder.calculateUserResults(user, pageRanks);
      Map<Double, Job> tempJobResults = new LinkedHashMap<>(); //reverses the order of the map
      //so that the job can be stored as the key and the value be Job so it is easier to access
      //in the front-end
      for (Map.Entry<Job, Double> en : jobResults.entrySet()) {
        double oldSkillsScore = en.getKey().getSkillsScore();
        double oldCourseScore = en.getKey().getCourseworkScore();
        double oldExperienceScore = en.getKey().getExperienceScore();
        double oldTotalResumeScore = en.getKey().getResumeSimilarityScore();
        double oldTotalSimilarityScore = en.getValue();
        double resumeTotalRatio = oldTotalResumeScore / oldTotalSimilarityScore;

        double newSkillsScore = 0;
        double newCourseScore = 0;
        double newExperienceScore = 0;
        double newTotalResumeScore = 0;
        double newTotalSimilarityScore = 0;

        //scaling scores so that they can be interpreted by user
        //if the similarity score is above 80 that likely means that internship match is a great
        //match for you and the reason it is not higher is because there's junk text so artificially
        //set it to 99%
        //if the similarity score is 50-79% artificially scale it by 1.25
        //if the similarity score is 25-50% artificially scale by 1.5
        //if the similarity score is less than  25% artificially scale by 2
        if (oldTotalSimilarityScore >= POINTEIGHT) {
          newTotalSimilarityScore = POINTNINENINENINENINE;

        } else if (oldTotalSimilarityScore > HALF && oldTotalSimilarityScore <= POINTSEVENNINE) {
          newTotalSimilarityScore = oldTotalSimilarityScore * ONEANDQUARTER;
        } else if (oldTotalSimilarityScore > QUARTER && oldTotalSimilarityScore <= HALF) {
          newTotalSimilarityScore = oldTotalSimilarityScore * ONEANDHALF;
        } else {
          newTotalSimilarityScore = oldTotalSimilarityScore * 2;
        }

        //once scale overall total similarity score, then need to scale the resume score and the
        //individual component scores of the resume (skills, coursework, experiences scores)
        newTotalResumeScore = resumeTotalRatio * newTotalSimilarityScore;
        double scaleFactor;
        if (oldTotalResumeScore != 0 && resumeTotalRatio <= POINTTHREE) { //do this so that
          //scores in which the page rank is primarily contributing to the score it is not
          //ranked higher
          scaleFactor = (newTotalResumeScore / oldTotalResumeScore) * 2;
          newTotalSimilarityScore = newTotalSimilarityScore * POINTSEVEN;
        } else if (oldTotalResumeScore != 0 && resumeTotalRatio > POINTTHREE
            && resumeTotalRatio <= POINTSEVEN) {
          scaleFactor = (newTotalResumeScore / oldTotalResumeScore) * ONEANDHALF;
          newTotalSimilarityScore = newTotalSimilarityScore * POINTEIGHT;
        } else if (oldTotalResumeScore != 0 && resumeTotalRatio > POINTSEVEN) {
          scaleFactor = (newTotalResumeScore / oldTotalResumeScore);
        } else {
          scaleFactor = 1;
        }

        //scale all individual components of resume
        newSkillsScore = oldSkillsScore * scaleFactor;
        newCourseScore = oldCourseScore * scaleFactor;
        newExperienceScore = oldExperienceScore * scaleFactor;

        //truncate decimal places
        DecimalFormat df = new DecimalFormat("#.####");
        newTotalSimilarityScore = Double.parseDouble(df.format(newTotalSimilarityScore));
        newTotalResumeScore = Double.parseDouble(df.format(newTotalResumeScore));
        newSkillsScore = Double.parseDouble(df.format(newSkillsScore));
        newCourseScore = Double.parseDouble(df.format(newCourseScore));
        newExperienceScore = Double.parseDouble(df.format(newExperienceScore));

        en.getKey().setFinalScore(newTotalSimilarityScore);
        en.getKey().setResumeSimilarityScore(newTotalResumeScore);
        en.getKey().setSkillsScore(newSkillsScore);
        en.getKey().setCourseworkScore(newCourseScore);
        en.getKey().setExperienceScore(newExperienceScore);

        tempJobResults.put(en.getValue(), en.getKey());

      }
      Map<String, Object> variables = ImmutableMap.of("userJobResults", tempJobResults);

      return GSON.toJson(variables);
    }
  }

  /**
   * Displays an error page when an exception occurs in the server.
   */
  private static class ExceptionPrinter implements ExceptionHandler {
    @Override
    public void handle(Exception e, Request req, Response res) {
      e.printStackTrace();
      res.status(500);
      StringWriter stacktrace = new StringWriter();
      try (PrintWriter pw = new PrintWriter(stacktrace)) {
        pw.println("<pre>");
        e.printStackTrace(pw);
        pw.println("</pre>");
      }
      res.body(stacktrace.toString());
    }
  }
}

