package edu.brown.cs.internhelper.Main;

import com.google.common.collect.ImmutableMap;
import edu.brown.cs.internhelper.Database.SQLDatabase;
import edu.brown.cs.internhelper.Functionality.Experience;
import edu.brown.cs.internhelper.Functionality.Job;
import edu.brown.cs.internhelper.Functionality.JobEdge;
import edu.brown.cs.internhelper.Functionality.JobGraphBuilder;
import edu.brown.cs.internhelper.Functionality.PageRank;
import edu.brown.cs.internhelper.Functionality.User;
import edu.brown.cs.internhelper.Graph.DirectedGraph;
import edu.brown.cs.internhelper.Graph.Edge;
import edu.brown.cs.internhelper.Graph.Vertex;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import org.json.JSONException;
import org.json.JSONObject;
import spark.ExceptionHandler;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.Spark;

import java.io.PrintWriter;
import java.io.StringWriter;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import com.google.gson.Gson;


/**
 * The Main class of our project. This is where execution begins.
 */
public final class Main {

  private static final int DEFAULT_PORT = 4567;
  private static final Gson GSON = new Gson();
  private static final MyFirebase fb = new MyFirebase();




  /**
   * The initial method called when execution begins.
   *
   * @param args An array of command line arguments
   */
  public static void main(String[] args) {
    try {
      new Main(args).run();
    } catch (Exception e) {
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


//    try {
//      fb.connectToApp();
//    } catch (Exception e) {}

    if (options.has("gui")) {
      runSparkServer((int) options.valueOf("port"));
    }
  }

  private void runSparkServer(int port) {
    Spark.port(port);
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
    Spark.get("/userID", new UserIDHandler());
    Spark.post("/userID", new UserIDHandler());
  }


  private static class UserIDHandler implements Route {

    @Override
    public String handle(Request req, Response res)
        throws JSONException, ExecutionException, InterruptedException {
      JSONObject data = new JSONObject(req.body());
      String id = data.getString("id");
      System.out.println(id);
      fb.setUp();
      User user = fb.getFirebaseResumeData(id);

      JobGraphBuilder graphBuilder = new JobGraphBuilder();
      graphBuilder.readData();
      graphBuilder.calculateJobScores();
      graphBuilder.calculateJobCompositeScore();
      graphBuilder.buildJobGraph();

      String userResumeDescriptions = "";
      for (Experience experience : user.getResume().getResumeExperiences()) {
        userResumeDescriptions += experience.getDescription();
      }
      graphBuilder.userResults(userResumeDescriptions);

      Map<String, Object> variables = ImmutableMap.of("userID", id);
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

