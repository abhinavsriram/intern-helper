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
import java.sql.ResultSet;
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
    if (options.has("pagerank")) {
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
    Spark.get("/searchResults", new SearchInternshipsHandler());
    Spark.post("/searchResults", new SearchInternshipsHandler());
    Spark.get("/suggestedRoles", new DatabaseSuggestedRolesHandler());
    Spark.post("/suggestedRoles", new DatabaseSuggestedRolesHandler());
  }

  static int getHerokuAssignedPort() {
    ProcessBuilder processBuilder = new ProcessBuilder();
    if (processBuilder.environment().get("PORT") != null) {
      System.out.println("HEROKU ASSIGNED PORT FOR BACKEND IS: " + Integer.parseInt(processBuilder.environment().get(
              "PORT")));
      Map<String, Object> docData = new HashMap<>();
      docData.put("port", String.valueOf(Integer.parseInt(processBuilder.environment().get("PORT"))));
      Firestore db = FirestoreClient.getFirestore();
      ApiFuture<WriteResult> future = db.collection("port-data").document("port-number").set(docData);
//      try {
//        File portFile = new File("port.txt");
//        FileWriter writer = new FileWriter("port.txt", false);
//        writer.write(String.valueOf(Integer.parseInt(processBuilder.environment().get("PORT"))));
//        writer.close();
//      } catch (IOException e) {
//        System.out.println("An error occurred when writing to port file!");
//        e.printStackTrace();
//      }
      return Integer.parseInt(processBuilder.environment().get("PORT"));
    }
    return DEFAULT_PORT; //return default port if heroku-port isn't set (i.e. on localhost)
  }


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
        databaseRoles = db.getTableNames();
        db.getConn().close();
      } catch (Exception e) {
        e.printStackTrace();
      }

      Set<String> suggestedRoles = new HashSet<>();

      for (Experience experience : user.getResume().getResumeExperiences()) {
//        System.out.println(experience.getTitle() + "," + experience.getCompany());
        String experienceTitle = experience.getTitle().replaceAll("Intern", "");
        experienceTitle = experienceTitle.replaceAll("intern", "");
        //System.out.println(experienceTitle + "," + experience.getCompany());
        Map<String, Double> unSortedMap = new HashMap<>();
        for (String databaseRole : databaseRoles) {
          TextSimilarity similarityCalculator = new TextSimilarity();
          try {
            similarityCalculator.loadStopWords("data/stopwords/stopwords.txt");
          } catch (Exception e) {
            e.printStackTrace();
          }
          String modifiedDatabaseRoleTitle = databaseRole.replaceAll("Intern", "");
          modifiedDatabaseRoleTitle = modifiedDatabaseRoleTitle.replaceAll("intern", "");
          Set<String> resumeSet = similarityCalculator.removeStopWords(experienceTitle);
          Set<String> databaseRoleSet = similarityCalculator.removeStopWords(modifiedDatabaseRoleTitle);
          Set<String> commonWords = similarityCalculator.commonWords(databaseRoleSet, resumeSet);
          double similarity = (double) (commonWords.size()) / (resumeSet.size());
          unSortedMap.put(databaseRole, similarity);
        }
        Map<String, Double> result = unSortedMap.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue, LinkedHashMap::new));


        Map.Entry<String, Double> entWithMaxVal = result.entrySet().iterator().next();

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


  private static class UserJobResultsHandler implements Route {

    @Override
    public String handle(Request req, Response res) throws JSONException, ExecutionException,
            InterruptedException {


      JSONObject data;
      data = new JSONObject(req.body());
      String role = data.getString("role");
      String id = data.getString("id");
//      FB.setUp();

      User user = FB.getFirebaseResumeData(id);

      String fileName = "data/page_rank_results/" + role + "pr.csv";
      String line = null;
      Map<Job, Double> pageRanks = new HashMap<>();
      try {
        Path pathName = Path.of(fileName);
        String text = Files.readString(pathName);
        text = text.replace("\n", "").replace("\r", "");

        Reader inputString = new StringReader(text);

        BufferedReader reader = new BufferedReader(inputString);
        CSVParser csvParser = new CSVParser();
        line = reader.readLine();
        String[] splitLines = csvParser.parseCSV(line);

        int numberJobEntries = splitLines.length / 7;

//        System.out.println("NUMBER OF JOB ENTRIES " + numberJobEntries);

        if (numberJobEntries > 1) {
          for (int i = 7; i < splitLines.length; i = i + 7) {
            Job job = new Job();
            job.setId(Integer.parseInt(splitLines[i]));
            job.setTitle(splitLines[i + 1]);
            job.setCompany(splitLines[i + 2]);
            //System.out.println(job.getCompany());
            job.setLocation(splitLines[i + 3]);
            job.setRequiredQualifications(splitLines[i + 4]);
            job.setLink(splitLines[i + 5]);
            double pageRank = Double.valueOf(splitLines[i + 6]);
            pageRanks.put(job, pageRank);
          }
        }

      } catch (Exception e) {
        // System.out.println("COMING TO THIS STACK TRACE");
        //e.printStackTrace();
      }


      JobGraphBuilder graphBuilder = new JobGraphBuilder();

      Map<Job, Double> jobResults = graphBuilder.calculateUserResults(user, pageRanks);

      Map<Double, Job> tempJobResults = new LinkedHashMap<>();
      for (Map.Entry<Job, Double> en : jobResults.entrySet()) {
//        System.out.println(en.getKey().getTitle() + "," + en.getKey().getCompany() + "," + en.getValue());


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

        if (oldTotalSimilarityScore >= 0.8) {
          newTotalSimilarityScore = 0.9999;
        } else if (oldTotalSimilarityScore > 0.5 && oldTotalSimilarityScore <= 0.79) {
          newTotalSimilarityScore = oldTotalSimilarityScore * 1.25;
        } else if (oldTotalSimilarityScore > 0.25 && oldTotalSimilarityScore <= 0.5) {
          newTotalSimilarityScore = oldTotalSimilarityScore * 1.5;
        } else {
          newTotalSimilarityScore = oldTotalSimilarityScore * 2;
        }

        newTotalResumeScore = resumeTotalRatio * newTotalSimilarityScore;
        double scaleFactor;
        if (oldTotalResumeScore != 0 && resumeTotalRatio <= 0.3) {
          scaleFactor = (newTotalResumeScore / oldTotalResumeScore) * 2;
          newTotalSimilarityScore = newTotalSimilarityScore * 0.7;

        } else if (oldTotalResumeScore != 0 && resumeTotalRatio > 0.3 && resumeTotalRatio <= 0.7) {
          scaleFactor = (newTotalResumeScore / oldTotalResumeScore) * 1.5;
          newTotalSimilarityScore = newTotalSimilarityScore * 0.8;

        } else if (oldTotalResumeScore != 0 && resumeTotalRatio > 0.7) {
          scaleFactor = (newTotalResumeScore / oldTotalResumeScore);

        } else {
          scaleFactor = 1;
        }

        newSkillsScore = oldSkillsScore * scaleFactor;
        newCourseScore = oldCourseScore * scaleFactor;
        newExperienceScore = oldExperienceScore * scaleFactor;

//        System.out.println("BEFORE SETTING");
//        System.out.println("FINAL SCORE " + en.getKey().getFinalScore() + "," + " COURSEWORK SCORE " + en.getKey()
//        .getCourseworkScore() +
//                "," + " SKILLS SCORE " + +en.getKey().getSkillsScore() + "," + " EXPERIENCE SCORE " + +en.getKey()
//                .getExperienceScore());


        DecimalFormat df = new DecimalFormat("#.####");
        newTotalSimilarityScore = Double.parseDouble(df.format(newTotalSimilarityScore));
        newTotalResumeScore = Double.parseDouble(df.format(newTotalResumeScore));
        newSkillsScore = Double.parseDouble(df.format(newSkillsScore));
        newCourseScore = Double.parseDouble(df.format(newCourseScore));
        newExperienceScore = Double.parseDouble(df.format(newExperienceScore));

//        newTotalSimilarityScore = Math.floor(newTotalSimilarityScore * 1000) / 1000;
//        newTotalResumeScore = Math.floor(newTotalResumeScore * 1000) / 1000;
//        newSkillsScore = Math.floor(newSkillsScore * 1000) / 1000;
//        newCourseScore = Math.floor(newCourseScore * 1000) / 1000;
//        newExperienceScore = Math.floor(newExperienceScore * 1000) / 1000;


        en.getKey().setFinalScore(newTotalSimilarityScore);
        en.getKey().setResumeSimilarityScore(newTotalResumeScore);
        en.getKey().setSkillsScore(newSkillsScore);
        en.getKey().setCourseworkScore(newCourseScore);
        en.getKey().setExperienceScore(newExperienceScore);


        tempJobResults.put(en.getValue(), en.getKey());


//        System.out.println("AFTER SETTING");
//        System.out.println("FINAL SCORE " + en.getKey().getFinalScore() + "," + " COURSEWORK SCORE " + en.getKey()
//        .getCourseworkScore() +
//                "," + " SKILLS SCORE " + +en.getKey().getSkillsScore() + "," + " EXPERIENCE SCORE " + +en.getKey()
//                .getExperienceScore());
//        System.out.println("==============================================");


      }
      Map<String, Object> variables = ImmutableMap.of("userJobResults", tempJobResults);
//      System.out.println("FINAL JSON SENT IS: ");
//      System.out.println(GSON.toJson(variables));
      return GSON.toJson(variables);


    }

  }


  private static class SearchInternshipsHandler implements Route {

    @Override
    public String handle(Request req, Response res)
            throws JSONException, ExecutionException, InterruptedException {
      JSONObject data = new JSONObject(req.body());
      String tableName = data.getString("role");

      SQLDatabase db = new SQLDatabase();
      db.connectDatabase("jdbc:sqlite:data/python_scripts/internships.sqlite3");
      ResultSet rs = db.runQuery("SELECT * FROM " + '"' + tableName + '"' + "LIMIT 10");
      List<Job> internships = new ArrayList<>();
      try {
        while (rs.next()) {

          Job internship = new Job();
          internship.setId(rs.getInt(1));
          internship.setTitle(rs.getString(2));
          internship.setCompany(rs.getString(3));
          internship.setLocation(rs.getString(4));
          internship.setRequiredQualifications(rs.getString(5));
          internship.setLink(rs.getString(6));

          internships.add(internship);
        }
        rs.close();
        db.getConn().close();
      } catch (Exception e) {
        e.printStackTrace();
      }

      Map<String, Object> variables = ImmutableMap.of("searchResults", internships);
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

