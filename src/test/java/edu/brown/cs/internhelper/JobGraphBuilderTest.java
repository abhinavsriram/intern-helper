package edu.brown.cs.internhelper;

import edu.brown.cs.internhelper.Database.SQLDatabase;
import edu.brown.cs.internhelper.Functionality.Experience;
import edu.brown.cs.internhelper.Functionality.Job;
import edu.brown.cs.internhelper.Functionality.JobEdge;
import edu.brown.cs.internhelper.Functionality.JobGraphBuilder;
import edu.brown.cs.internhelper.Functionality.Resume;
import edu.brown.cs.internhelper.Functionality.TextSimilarity;
import edu.brown.cs.internhelper.Functionality.User;
import edu.brown.cs.internhelper.Graph.DirectedGraph;
import org.junit.Test;

import java.io.IOException;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertEquals;

public class JobGraphBuilderTest {

  @Test
  public void readDataTest() throws Exception {

    SQLDatabase sqlData = new SQLDatabase();
    sqlData.connectDatabase("jdbc:sqlite:data/sample_intern_data.sqlite3");
    String tableName = " \"intern\" " ;

    List<Job> allJobs = new ArrayList<>();
    DirectedGraph graph = new DirectedGraph();

    JobGraphBuilder graphBuilder = new JobGraphBuilder(allJobs, graph);

    graphBuilder.readData(sqlData, tableName);

    assertEquals(15, allJobs.size());
    assertEquals("Software Engineer, Intern/Co-op", allJobs.get(0).getTitle());

    sqlData.getConn().close();


    allJobs = new ArrayList<>();
    graphBuilder = new JobGraphBuilder(allJobs, graph);
    tableName = "non existent table";
    graphBuilder.readData(sqlData, tableName);
    assertEquals(0, allJobs.size());

  }

  @Test
  public void calculateJobScoresTest() {

    SQLDatabase sqlData = new SQLDatabase();
    sqlData.connectDatabase("jdbc:sqlite:data/sample_intern_data.sqlite3");
    String tableName = " \"intern\" " ;

    List<Job> allJobs = new ArrayList<>();
    DirectedGraph graph = new DirectedGraph();

    JobGraphBuilder graphBuilder = new JobGraphBuilder(allJobs, graph);

    graphBuilder.readData(sqlData, tableName);

    graphBuilder.calculateJobScores();

    String maxTitle = "";
    Double maxScore = 0.0;

    String minTitle = "";
    Double minScore = 1000000000000000.00;

    for (Job job : allJobs) {

      if (job.getCompositeSimilarityScore() > maxScore) {
        maxScore = job.getCompositeSimilarityScore();
        maxTitle = job.getTitle();
      }

      if (job.getCompositeSimilarityScore() < minScore) {
        minScore = job.getCompositeSimilarityScore();
        minTitle = job.getTitle();
      }

    }

    assertEquals("Data Engineer Intern - Data Science", maxTitle);
    assertEquals( 0.177, maxScore, 0.05);

    assertEquals("UI JavaScript Intern", minTitle);
    assertEquals( 0.045, minScore, 0.05);


    Job job = new Job();
    job.setRequiredQualifications(null);


    allJobs.add(job);

    graphBuilder = new JobGraphBuilder(allJobs, graph);
    graphBuilder.calculateJobScores();



    for (Double score : allJobs.get(allJobs.indexOf(job)).getJobSimilarityScores()) {
      assertEquals(0.0, score, 0.0);
    }



  }

  @Test
  public void buildJobGraphTest() {

    SQLDatabase sqlData = new SQLDatabase();
    sqlData.connectDatabase("jdbc:sqlite:data/sample_intern_data.sqlite3");
    String tableName = " \"intern\" " ;

    List<Job> allJobs = new ArrayList<>();
    DirectedGraph graph = new DirectedGraph();

    JobGraphBuilder graphBuilder = new JobGraphBuilder(allJobs, graph);

    graphBuilder.readData(sqlData, tableName);

    graphBuilder.calculateJobScores();

    graphBuilder.buildJobGraph();

    Map<Job, Set<JobEdge>> allIncomingConnections = graph.getIncomingConnections();


    for (Map.Entry<Job, Set<JobEdge>> en : allIncomingConnections.entrySet()) {

      if (en.getKey().getTitle() == "UI JavaScript Intern") {
        assertEquals(0, en.getValue().size());
        assertEquals(14, graph.getOutgoingConnections().get(en.getKey()));
      }

      if (en.getKey().getTitle() == "Data Engineer Intern - Data Science") {
        assertEquals(14, en.getValue().size());
        assertEquals(0, graph.getOutgoingConnections().get(en.getKey()));

      }

    }

  }

  @Test
  public void runPageRankTest() {

    SQLDatabase sqlData = new SQLDatabase();
    sqlData.connectDatabase("jdbc:sqlite:data/sample_intern_data.sqlite3");
    String tableName = " \"intern\" " ;

    List<Job> allJobs = new ArrayList<>();
    DirectedGraph graph = new DirectedGraph();

    JobGraphBuilder graphBuilder = new JobGraphBuilder(allJobs, graph);

    graphBuilder.readData(sqlData, tableName);

    graphBuilder.calculateJobScores();

    graphBuilder.buildJobGraph();

    Map<Job, Double> ranks = graphBuilder.runPageRank();

    String maxTitle = "";
    Double maxRank = 0.0;

    String minTitle = "";
    Double minRank = 1000000000000000.00;

    for (Map.Entry<Job, Double> en : ranks.entrySet()) {

      if (en.getValue() > maxRank) {
        maxRank = en.getValue();
        maxTitle = en.getKey().getTitle();
      }

      if (en.getValue() < minRank) {
        minRank = en.getValue();
        minTitle = en.getKey().getTitle();
      }

    }

    assertEquals("Data Engineer Intern - Data Science", maxTitle);
    assertEquals( 0.26, maxRank, 0.05);

    assertEquals("UI JavaScript Intern", minTitle);
    assertEquals( 0.024, minRank, 0.05);

  }

  @Test
  public void runCalculateJobResumeSimilarityTest() {

    SQLDatabase sqlData = new SQLDatabase();
    sqlData.connectDatabase("jdbc:sqlite:data/sample_intern_data.sqlite3");
    String tableName = " \"intern\" " ;

    List<Job> allJobs = new ArrayList<>();
    DirectedGraph graph = new DirectedGraph();

    JobGraphBuilder graphBuilder = new JobGraphBuilder(allJobs, graph);

    graphBuilder.readData(sqlData, tableName);

    graphBuilder.calculateJobScores();

    graphBuilder.buildJobGraph();

    Map<Job, Double> ranks = graphBuilder.runPageRank();

    User user = new User();

    String coursework = "object-oriented programming, algorithms and data structures, software"
        + "engineering, introduction to systems, linear algebra, statistics";

    String skills = "java, perl, c++, php, python";

    Experience experience = new Experience();
    experience.setCompany("Google");
    experience.setTitle("Software Engineering Intern");
    experience.setDescription("Worked with pearl and java on back end of search engine");

    Resume resume = new Resume();
    resume.addExperience(experience);

    user.setCoursework(coursework);
    user.setSkills(skills);
    user.setResume(resume);

    Map<Job, Double> resumeSimilarityMap = graphBuilder.calculateJobResumeSimilarity(ranks, user);

    String maxTitle = "";
    Double maxSim = 0.0;

    String minTitle = "";
    Double minSim = 1000000000000000.00;

    for (Map.Entry<Job, Double> en : resumeSimilarityMap.entrySet()) {



        if (en.getKey().getResumeSimilarityScore() > maxSim) {
          maxSim = en.getKey().getResumeSimilarityScore();
          maxTitle = en.getKey().getTitle();
        }

        if (en.getKey().getResumeSimilarityScore() < minSim) {
          minSim = en.getKey().getResumeSimilarityScore();
          minTitle = en.getKey().getTitle();
        }


    }

    assertEquals("Software Engineer, Intern/Co-op", maxTitle);
    assertEquals( 0.46, maxSim, 0.05);

//    assertEquals("Product Management Intern - Flurry Company Name", minTitle);
    assertEquals( 0.0, minSim, 0.05);



  }

  @Test
  public void runCalculateUserResultsTest() {

    SQLDatabase sqlData = new SQLDatabase();
    sqlData.connectDatabase("jdbc:sqlite:data/sample_intern_data.sqlite3");
    String tableName = " \"intern\" " ;

    List<Job> allJobs = new ArrayList<>();
    DirectedGraph graph = new DirectedGraph();

    JobGraphBuilder graphBuilder = new JobGraphBuilder(allJobs, graph);

    graphBuilder.readData(sqlData, tableName);

    graphBuilder.calculateJobScores();

    graphBuilder.buildJobGraph();

    Map<Job, Double> ranks = graphBuilder.runPageRank();

    User user = new User();

    String coursework = "object-oriented programming, algorithms and data structures, software"
        + "engineering, introduction to systems, linear algebra, statistics";

    String skills = "java, perl, c++, php, python";

    Experience experience = new Experience();
    experience.setCompany("Google");
    experience.setTitle("Software Engineering Intern");
    experience.setDescription("Worked with pearl and java on back end of search engine");

    Resume resume = new Resume();
    resume.addExperience(experience);

    user.setCoursework(coursework);
    user.setSkills(skills);
    user.setResume(resume);

    Map<Job, Double> resumeSimilarityMap = graphBuilder.calculateJobResumeSimilarity(ranks, user);

    Map<Job, Double> userResultsMap = graphBuilder.calculateUserResults(user, resumeSimilarityMap);



    String minTitle = "";
    Double maxSim = 0.0;

    String maxTitle = "";
    Double minSim = 1000000000000000.00;

    for (Map.Entry<Job, Double> en : userResultsMap.entrySet()) {


      if (en.getValue() > maxSim) {
        maxSim = en.getValue();
        maxTitle = en.getKey().getTitle();
      }

      if (en.getValue() < minSim) {
        minSim = en.getValue();
        minTitle = en.getKey().getTitle();
      }


    }

    assertEquals("Software Engineer, Intern/Co-op", maxTitle);

    assertEquals("UI JavaScript Intern", minTitle);





  }





}
