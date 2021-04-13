package edu.brown.cs.internhelper.Main;

import com.google.common.collect.ImmutableMap;
import edu.brown.cs.internhelper.Functionality.CachePageRanks;
import edu.brown.cs.internhelper.Functionality.Job;
import edu.brown.cs.internhelper.Functionality.JobGraphBuilder;
import edu.brown.cs.internhelper.Functionality.Resume;
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

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import java.io.FileWriter;
import java.io.File;
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
  }

  static int getHerokuAssignedPort() {
    ProcessBuilder processBuilder = new ProcessBuilder();
    if (processBuilder.environment().get("PORT") != null) {
      System.out.println("HEROKU ASSIGNED PORT FOR BACKEND IS: " + Integer.parseInt(processBuilder.environment().get("PORT")));

      try {
        File portFile = new File("port.txt");
        FileWriter writer = new FileWriter("port.txt", false);
        writer.write(Integer.parseInt(processBuilder.environment().get("PORT")));
        writer.close();
      } catch (IOException e) {
        System.out.println("An error occurred when writing to port file!");
        e.printStackTrace();
      }

      return Integer.parseInt(processBuilder.environment().get("PORT"));
    }
    return DEFAULT_PORT; //return default port if heroku-port isn't set (i.e. on localhost)
  }

  private static class UserJobResultsHandler implements Route {

    @Override
    public String handle(Request req, Response res)
            throws JSONException, ExecutionException, InterruptedException {
      JSONObject data = new JSONObject(req.body());
      String id = data.getString("id");
      System.out.println(id);
      FB.setUp();
      User user = FB.getFirebaseResumeData(id);

      JobGraphBuilder graphBuilder = new JobGraphBuilder();
      //graphBuilder.readData();
      graphBuilder.calculateJobScores();
      //graphBuilder.calculateJobCompositeScore();
      graphBuilder.buildJobGraph();

//      String userResumeDescriptions = "";
//      for (Experience experience : user.getResume().getResumeExperiences()) {
//        userResumeDescriptions += experience.getDescription();
//      }
      Resume resume = user.getResume();
      Map<Job, Double> jobResults = graphBuilder.userResults(resume);

      Map<Double, Job> tempJobResults = new HashMap<>();
      for (Map.Entry<Job, Double> en : jobResults.entrySet()) {
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

