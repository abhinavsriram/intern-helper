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
import edu.brown.cs.internhelper.Functionality.LevenshteinDistance;
import edu.brown.cs.internhelper.Functionality.TextSimilarity;
import edu.brown.cs.internhelper.Functionality.User;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;
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
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
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
    OptionParser parser = new OptionParser();
    parser.accepts("gui");
    parser.accepts("port").withRequiredArg().ofType(Integer.class)
            .defaultsTo(DEFAULT_PORT);
    OptionSet options = parser.parse(args);

    TextSimilarity similarityCalculator = new TextSimilarity();
    try {
      similarityCalculator.loadStopWords("data/stopwords/stopwords.txt");
    } catch (Exception e) {
      e.printStackTrace();
    }

    /**
    String resume = "Get a jump on your future career by taking advantage of some great hands-on engineering experience Sensata is looking for some intelligent proactive independent thinkers for summer internshipsSensata Technologies is the world's leading supplier of sensors and controls across a broad range of markets and applications We are leaders in the global automotive appliance aircraft industrial and HVAC markets with sensing and protection solutions that improve safety and efficiency for millions of people every day Experienced colleagues are always willing to provide needed guidance The decisions and actions of our entry level engineers have a direct and immediate impact on our products and our customers This team will be focused on bringing connectivity to our next gen sensors playing a critical role in larger Industrial systems You will work on low level embedded firmware design and implementation real time operating systems (RTOS) device drivers and in one or more wired/wireless connectivity technologies such as CANOpen IOLink ZigBee Bluetooth LoRa (Note interns are treated as engineers on the team with real responsibility for important projects that contribute to the business)-Most important tasks and responsibilitiesOur engineers are involved in all phases of product development from initial identification of customer needs to high volume production implementation Emphasis is on the use of state-of-the-art concepts materials and assembly technologies This is a hands-on position for an individual who wants to see her/his ideas embodied in successful productsCreativity and innovation essential to meet customer requirements and to improve performance quality and costDesign creativityMust be able to clearly present new exciting and beneficial design concepts to customers managers and peersAbility to collaborate effectively with team members while maintaining strong sense of individual responsibilityStrong interpersonal skills to work effectively with superiors peers subordinates suppliers and customers  Frequent customer interaction with OEMs (original equipment manufacturers) is commonAbility to work in a fast-paced development environment with complex project attributesAbility to develop unique differentiated solutions to complex problemsAbility to meet demands of customers on schedule and within budgetStrong team leadership and team membership skillsStrong sense of individual responsibilityAbility to work with other business orgs (Quality Marketing Purchasing etc)Ability to influence othersEvidence of participation in team-based project work and ability to fill multiple rolesAbility to understand higher-level aspect of projectAbility to take responsibility for things outside your direct controlAbility and desire to work with global teams-Educational requirements Pursuing a BS or MS in Computer Science Electrical Engineering Computer Engineering or similar degreeUS citizen or US permanent resident is NOT required for this position however proper work authorizations will be required-SmarterTogetherCollaborating at Sensata means working with some of the world’s most talented people in an enriching environment that is constantly pushing towards the next best thingEmployees work across functions countries and cultures gaining new perspectives through mutual respect and open communication As OneSensata we are working together to make things work together   Led cross functional teams of software engineering UI/UX game design and marketing to redesign MVP from scratch Wrote proprietary recommendations and scoring algorithm in JavaScript (the central algorithm driving game logic) Worked closely with UI/UX team and CEO to prototype iterate and develop hi-ﬁdelity wire-frames of redesigned MVP Focused on improving user engagement using gamiﬁcation while transitioning to subscription model (from free model)  Performed market sizing & visualized key data points from 1 million+ excel entries for Indian consumer electronics product  Researched and identiﬁed KPI metrics and performed competitive/SWOT analysis of market leaders & then worked closely with marketing engineering and sales teams to develop competing product based on SWOT analysis  Identiﬁed poor customer service as key weakness of competitors & worked with outsourced software ﬁrm to deploy superior automated customer service solution projected to reduce costs by up to 80%";
    String job = "Get a jump on your future career by taking advantage of some great hands-on " +
        "engineering experience. Sensata is looking for some intelligent, proactive independent " +
        "thinkers for summer internships.\n" +
        "\n" +
        "Sensata Technologies is the world's leading supplier of sensors and controls across a " +
        "broad range of markets and applications. We are leaders in the global automotive, " +
        "appliance, aircraft, industrial and HVAC markets with sensing and protection solutions " +
        "that improve safety and efficiency for millions of people every day. \n" +
        "\n" +
        "Experienced colleagues are always willing to provide needed guidance. The decisions and " +
        "actions of our entry level engineers have a direct and immediate impact on our products " +
        "and our customers. \n" +
        "This team will be focused on bringing connectivity to our next gen sensors playing a " +
        "critical role in larger Industrial systems. You will work on low level embedded firmware" +
        " design and implementation, real time operating systems (RTOS), device drivers, and in " +
        "one or more wired/wireless connectivity technologies such as CANOpen, IOLink, ZigBee, " +
        "Bluetooth, LoRa. \n" +
        "\n" +
        "(Note: interns are treated as engineers on the team, with real responsibility for " +
        "important projects that contribute to the business.)\n" +
        "-\n" +
        "\n" +
        "Most important tasks and responsibilities:\n" +
        "\n" +
        "Our engineers are involved in all phases of product development from initial " +
        "identification of customer needs to high volume production implementation. Emphasis is " +
        "on the use of state-of-the-art concepts, materials, and assembly technologies. This is a" +
        " hands-on position for an individual who wants to see her/his ideas embodied in " +
        "successful products.\n" +
        "Creativity and innovation essential to meet customer requirements and to improve " +
        "performance, quality and cost.\n" +
        "Design creativity.\n" +
        "Must be able to clearly present new, exciting, and beneficial design concepts to " +
        "customers, managers, and peers.\n" +
        "Ability to collaborate effectively with team members while maintaining strong sense of " +
        "individual responsibility.\n" +
        "Strong interpersonal skills to work effectively with superiors, peers, subordinates, " +
        "suppliers, and customers.  Frequent customer interaction with OEMs (original equipment " +
        "manufacturers) is common.\n" +
        "Ability to work in a fast-paced development environment with complex project attributes" +
        ".\n" +
        "Ability to develop unique, differentiated solutions to complex problems.\n" +
        "Ability to meet demands of customers on schedule and within budget.\n" +
        "Strong team leadership and team membership skills.\n" +
        "Strong sense of individual responsibility.\n" +
        "Ability to work with other business orgs. (Quality, Marketing, Purchasing, etc.)\n" +
        "Ability to influence others.\n" +
        "Evidence of participation in team-based project work and ability to fill multiple roles" +
        ".\n" +
        "Ability to understand higher-level aspect of project.\n" +
        "Ability to take responsibility for things outside your direct control.\n" +
        "Ability and desire to work with global teams.\n" +
        "-\n" +
        "\n" +
        "Educational requirements: \n" +
        "\n" +
        "Pursuing a BS or MS in Computer Science, Electrical Engineering, Computer Engineering or" +
        " similar degree\n" +
        "U.S. citizen or U.S. permanent resident is NOT required for this position, however " +
        "proper work authorizations will be required.\n" +
        "-\n" +
        "\n" +
        "SmarterTogether\n" +
        "Collaborating at Sensata means working with some of the world’s most talented people in " +
        "an enriching environment that is constantly pushing towards the next best thing.\n" +
        "\n" +
        "Employees work across functions, countries and cultures gaining new perspectives through" +
        " mutual respect and open communication.\n" +
        "\n" +
        " As OneSensata, we are working together to make things work together ";

    Set<String> resumeSet = similarityCalculator.removeStopWords(resume);
    Set<String> jobSet = similarityCalculator.removeStopWords(job);
    Set<String> commonWords =  similarityCalculator.commonWords(jobSet, resumeSet);

    double commonSimilarity = (double) (commonWords.size()) / (resumeSet.size());
    //System.out.println(commonSimilarity);
     **/




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
          Set<String> commonWords =  similarityCalculator.commonWords(databaseRoleSet, resumeSet);
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

      System.out.println("HERE IN USER JOB RESULTS HANDLER");

      JSONObject data = new JSONObject(req.body());
      String role = data.getString("role");
      String id = data.getString("id");
//      FB.setUp();
      User user = FB.getFirebaseResumeData(id);

      String fileName = "data/page_rank_results2/" + role + "pr.csv";
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

        if (numberJobEntries > 1) {
          for (int i = 7; i < splitLines.length; i = i + 7) {
            Job job = new Job();
            job.setId(Integer.valueOf(splitLines[i]));
            job.setTitle(splitLines[i+1]);
            job.setCompany(splitLines[i+2]);
            //System.out.println(job.getCompany());
            job.setLocation(splitLines[i+3]);
            job.setRequiredQualifications(splitLines[i+4]);
            job.setLink(splitLines[i+5]);
            double pageRank = Double.valueOf(splitLines[i+6]);
            pageRanks.put(job, pageRank);
          }
        }

      } catch (Exception e) {
        e.printStackTrace();
      }


      JobGraphBuilder graphBuilder = new JobGraphBuilder();

      Map<Job, Double> jobResults = graphBuilder.calculateUserResults(user, pageRanks);

      Map<Double, Job> tempJobResults = new LinkedHashMap<>();
      for (Map.Entry<Job, Double> en : jobResults.entrySet()) {
        //System.out.println(en.getKey().getTitle() + " " + en.getValue());
//        if (counter == 20) {
//          break;
//        }
        tempJobResults.put(en.getValue(), en.getKey());

        System.out.println(en.getKey().getTitle()+ "," + en.getKey().getCompany() + "," + en.getValue());
        System.out.println("FINAL SCORE " + en.getKey().getFinalScore()+ "," + " COURSEWORK SCORE " +  en.getKey().getCourseworkScore() +
            "," + " SKILLS SCORE " +   + en.getKey().getSkillsScore() + "," + " EXPERIENCE SCORE " + + en.getKey().getExperienceScore());
        System.out.println("==============================================");

      }



      Map<String, Object> variables = ImmutableMap.of("userJobResults", tempJobResults);
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

