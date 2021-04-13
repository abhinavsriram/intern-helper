package edu.brown.cs.internhelper.Main;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import com.google.common.collect.ImmutableMap;
import com.google.firebase.cloud.FirestoreClient;
import edu.brown.cs.internhelper.CSV.CSVParser;
import edu.brown.cs.internhelper.Database.SQLDatabase;
import edu.brown.cs.internhelper.Functionality.Experience;
import edu.brown.cs.internhelper.Functionality.Job;
import edu.brown.cs.internhelper.Functionality.JobGraphBuilder;
import edu.brown.cs.internhelper.Functionality.LevenshteinDistance;
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
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;

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
    OptionParser parser = new OptionParser();
    parser.accepts("gui");
    parser.accepts("port").withRequiredArg().ofType(Integer.class)
            .defaultsTo(DEFAULT_PORT);
    OptionSet options = parser.parse(args);
//    JobGraphBuilder graphBuilder = new JobGraphBuilder();
//    graphBuilder.readData();
//    graphBuilder.calculateJobScores();
//    graphBuilder.calculateJobCompositeScore();
//    graphBuilder.buildJobGraph();
//    graphBuilder.runPageRank();
//    CachePageRanks cachePageRanks = new CachePageRanks();
//    cachePageRanks.cacheResults();
    runSparkServer((int) options.valueOf("port"));
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
      System.out.println("HEROKU ASSIGNED PORT FOR BACKEND IS: " + Integer.parseInt(processBuilder.environment().get("PORT")));
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
        String experienceTitle = experience.getTitle();
        double mostSimilarScore = 0.0;
        String mostSimilarRole = "";
        for (String databaseRole : databaseRoles) {
          LevenshteinDistance distance = new LevenshteinDistance();
          double similarity = distance.similarity(experienceTitle, databaseRole);
          if (similarity > mostSimilarScore) {
            mostSimilarScore = similarity;
            mostSimilarRole = databaseRole;
          }

        }
        suggestedRoles.add(mostSimilarRole);
      }

      Map<String, Object> variables = ImmutableMap.of("suggestedRoles", suggestedRoles);
      return GSON.toJson(variables);
    }
  }

  private static class UserJobResultsHandler implements Route {

    @Override
    public String handle(Request req, Response res) throws JSONException, ExecutionException,
            InterruptedException {

      JSONObject data = new JSONObject(req.body());
      String role = data.getString("role");
      String id = data.getString("id");
      User user = FB.getFirebaseResumeData(id);

      String fileName = role + "pr.csv";
      String line = null;
      int counter = 0;
      Map<Job, Double> pageRanks = new HashMap<>();
      try {
        BufferedReader reader = new BufferedReader(new FileReader(fileName));
        CSVParser csvParser = new CSVParser();
        try {
          while ((line = reader.readLine()) != null) {
            if (counter != 0) {
              String[] splitLines = csvParser.parseCSV(line);
              Job job = new Job();
              job.setId(Integer.valueOf(splitLines[0]));
              job.setTitle(splitLines[1]);
              job.setCompany(splitLines[2]);
              job.setLocation(splitLines[3]);
              job.setRequiredQualifications(splitLines[4]);
              job.setLink(splitLines[5]);
              double pageRank = Double.valueOf(splitLines[6]);
              pageRanks.put(job, pageRank);
              counter += 1;
            }
          }
        } catch (Exception e) {
          e.printStackTrace();
        }
      } catch (Exception e) {
        e.printStackTrace();
      }

      JobGraphBuilder graphBuilder = new JobGraphBuilder();

      Map<Job, Double> jobResults = graphBuilder.calculateUserResults(user, pageRanks);

      Map<Double, Job> tempJobResults = new LinkedHashMap<>();
      for (Map.Entry<Job, Double> en : jobResults.entrySet()) {
        tempJobResults.put(en.getValue(), en.getKey());
      }

      Map<String, Object> variables = ImmutableMap.of("userJobResults", tempJobResults);
      return GSON.toJson(variables);


    }

  }


  /**
   * private static class UserJobResultsHandler implements Route {
   *
   * @Override public String handle(Request req, Response res)
   * throws JSONException, ExecutionException, InterruptedException {
   * JSONObject data = new JSONObject(req.body());
   * String id = data.getString("id");
   * System.out.println(id);
   * User user = FB.getFirebaseResumeData(id);
   * <p>
   * JobGraphBuilder graphBuilder = new JobGraphBuilder();
   * //graphBuilder.readData();
   * graphBuilder.calculateJobScores();
   * //graphBuilder.calculateJobCompositeScore();
   * graphBuilder.buildJobGraph();
   * <p>
   * //      String userResumeDescriptions = "";
   * //      for (Experience experience : user.getResume().getResumeExperiences()) {
   * //        userResumeDescriptions += experience.getDescription();
   * //      }
   * Resume resume = user.getResume();
   * Map<Job, Double> jobResults = graphBuilder.userResults(resume);
   * <p>
   * Map<Double, Job> tempJobResults = new HashMap<>();
   * for (Map.Entry<Job, Double> en : jobResults.entrySet()) {
   * tempJobResults.put(en.getValue(), en.getKey());
   * }
   * <p>
   * Map<String, Object> variables = ImmutableMap.of("userJobResults", tempJobResults);
   * return GSON.toJson(variables);
   * }
   * }
   **/

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

