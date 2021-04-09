package edu.brown.cs.internhelper.Functionality;

import edu.brown.cs.internhelper.Database.SQLDatabase;
import edu.brown.cs.internhelper.Graph.DirectedGraph;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class JobGraphBuilder {

  private List<Job> allJobs;
  private static DirectedGraph graph;


  public void readData() {

    SQLDatabase db = new SQLDatabase();
    db.connectDatabase("jdbc:sqlite:data/sample_intern_data.sqlite3");
    ResultSet rs = db.runQuery("SELECT * FROM intern");
    allJobs = new ArrayList<>();
    try {
      while (rs.next()) {
        Job job = new Job();
        job.setId(rs.getInt(1));
        job.setTitle(rs.getString(2));
        job.setCompany(rs.getString(3));
        job.setLocation(rs.getString(4));
        job.setRequiredQualifications(rs.getString(5));
        job.setPreferredQualifications(rs.getString(6));
        job.setLink(rs.getString(7));
        allJobs.add(job);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }


  }

  public void calculateJobScores() {
    LevenshteinDistance distance = new LevenshteinDistance();

    for (int i = 0; i < allJobs.size(); i++) {
      List scores = new ArrayList();
      for (int j = 0; j < allJobs.size(); j++) {
        if (i != j) {
          String currJob = allJobs.get(i).getRequiredQualifications();
          String otherJob = allJobs.get(j).getRequiredQualifications();
          double dist = distance.similarity(currJob, otherJob);
          scores.add(dist);
        }
        // compare list.get(i) and list.get(j)
      }

      allJobs.get(i).setJobSimilarityScores(scores);

    }
  }

  public void calculateJobCompositeScore() {
    for (int i = 0; i < allJobs.size(); i++) {
      List<Double> currScores = allJobs.get(i).getJobSimilarityScores();
      double totalScore = 0.0;
      for (int j = 0; j < currScores.size(); j++) {
        totalScore += currScores.get(j);
      }
      double compositeScore = (totalScore) / (currScores.size());
      allJobs.get(i).setCompositeSimilarityScore(compositeScore);
      //System.out.println(compositeScore);
    }

  }

  public void buildJobGraph() {
    graph = new DirectedGraph();
    for (int i = 0; i < allJobs.size(); i++) {
      graph.addVertex(allJobs.get(i));
    }

    for (int i = 0; i < allJobs.size(); i++) {
      for (int j = 0; j < allJobs.size(); j++) {
        if (i != j) {
          Job currJob = allJobs.get(i);
          Job otherJob = allJobs.get(j);
          if (currJob.getCompositeSimilarityScore() <= otherJob.getCompositeSimilarityScore()) {
            JobEdge e = new JobEdge(currJob, otherJob, 1);
            graph.addEdge(e);
          }
        }
      }
    }

  }

  public static void printMap() {
    Iterator it = graph.getVertexConnections().entrySet().iterator();
    while (it.hasNext()) {
      Map.Entry<Job, Set<JobEdge>> pair = (Map.Entry) it.next();
      System.out.println("JOB TITLE " + pair.getKey().getTitle() + " who has a score of " + pair.getKey().getCompositeSimilarityScore());
      System.out.println("THESE ARE THE OUTGOING EDGES: ");
      for (JobEdge edge : pair.getValue()) {
        System.out.println("EDGE IS CONNECTED TO : " + edge.getDestinationVertex().getTitle()
                + " who has a score of " + edge.getDestinationVertex().getCompositeSimilarityScore());
      }
      System.out.println("---------------------------------------------");
      it.remove(); // avoids a ConcurrentModificationException
    }
    System.out.println("===============================================");
    Iterator it2 = graph.getIncomingConnections().entrySet().iterator();
    while (it2.hasNext()) {
      Map.Entry<Job, Set<JobEdge>> pair = (Map.Entry) it2.next();
      System.out.println("JOB TITLE " + pair.getKey().getTitle() + " who has a score of " + pair.getKey().getCompositeSimilarityScore());
      System.out.println("THESE ARE THE INCOMING EDGES: ");
      for (JobEdge edge : pair.getValue()) {
        System.out.println("EDGE IS CONNECTED TO : " + edge.getSourceVertex().getTitle()
                + " who has a score of " + edge.getSourceVertex().getCompositeSimilarityScore());
      }
      System.out.println("---------------------------------------------");
      it2.remove(); // avoids a ConcurrentModificationException
    }

  }

  public Map<Job, Double> runPageRank() {
    PageRank pageRank = new PageRank();
    Map<Job, Double> jobPageRanks = pageRank.calcPageRank(graph);

    Map<Job, Double> temp = sortByValue(jobPageRanks);
    System.out.println("THIS IS WHAT THE PAGE RANK RESULTS WOULD BE");
    System.out.println("============================================");
    System.out.println("============================================");
    for (Map.Entry<Job, Double> en : temp.entrySet()) {
      System.out.println("Key = " + en.getKey().getTitle() + ","
              + en.getKey().getCompositeSimilarityScore() + " Value = " + en.getValue());
    }

    return jobPageRanks;


  }

  public static Map<Job, Double> sortByValue(Map<Job, Double> hm) {
    // Create a list from elements of HashMap
    List<Map.Entry<Job, Double>> list =
            new LinkedList<Map.Entry<Job, Double>>(hm.entrySet());

    // Sort the list
    Collections.sort(list, new Comparator<Map.Entry<Job, Double>>() {
      public int compare(Map.Entry<Job, Double> o1,
                         Map.Entry<Job, Double> o2) {
        return (o2.getValue()).compareTo(o1.getValue());
      }
    });

    // put data from sorted list to hashmap
    HashMap<Job, Double> temp = new LinkedHashMap<Job, Double>();
    for (Map.Entry<Job, Double> aa : list) {
      temp.put(aa.getKey(), aa.getValue());
    }
    return temp;
  }

  public Map<Job, Double> calculateJobResumeSimilarity(Map<Job, Double> jobRanks, String resume) {
    LevenshteinDistance distance = new LevenshteinDistance();
//    String resume = "Currently enrolled in or recently graduated from a Bachelor's Degree\n" +
//        "Knowledge of UI/UX best practices and trends\n" +
//        "Portfolio of UI/UX related work Experience or interest designing and shipping web-based " +
//        "products and interfaces\n" +
//        "Strong experience working from flows and wireframes through to the final product\n" +
//        "UX and UI experience\n" +
//        "Experience working with a SaaS product\n" +
//        "Designing wireframes and mock-ups of new features\n" +
//        "Collaborating with team members during design reviews to refine the user experience\n" +
//        "Working with software engineers to ensure consistency between design and implementation" +
//        ".\n" +
//        "Propose solutions to solve design/user experience challenges\n" +
//        "Writing clean, concise and descriptive documentation of your features. At least 18 years" +
//        " old, currently enrolled in a four year academic institution completing an undergrad, " +
//        "grad, or PhD degree in Business/Economics, Human-Computer Interaction, Computer Science " +
//        "or a related STEM field.\n" +
//        "Legally authorized to work in the United States. Due to the limited duration of this " +
//        "program, sponsorship for employment visa status is not available for this position.\n" +
//        "Availability to complete the full 10-12-week internship program during summer 2021, with" +
//        " a time commitment of approximately 30-40 hours per week.\n" +
//        "Must be highly proficient in SQL and Microsoft Excel\n" +
//        "Strong understanding of growth principles, product development, and product go to market" +
//        " processes";


    for (Map.Entry<Job, Double> en : jobRanks.entrySet()) {
      String currJob = en.getKey().getRequiredQualifications();
      double dist = distance.similarity(currJob, resume);
      en.getKey().setResumeSimilarityScore(dist);
      //System.out.println("JOB TITLE " + en.getKey().getTitle() + "," + dist);

    }

    return jobRanks;

  }

  public Map<Job, Double> userResults(Resume resume) {
    Map<Job, Double> pageRankResults = this.runPageRank();
    String userResumeDescriptions = "";
    for (Experience experience : resume.getResumeExperiences()) {
      userResumeDescriptions += experience.getDescription();
    }
    Map<Job, Double> resumeSimilarityResults = this.calculateJobResumeSimilarity(pageRankResults,
            userResumeDescriptions);

    System.out.println("THIS IS WHAT THE COMPARISON TO RESUME RESULTS WOULD BE");
    System.out.println("============================================");
    System.out.println("============================================");
    for (Map.Entry<Job, Double> en : resumeSimilarityResults.entrySet()) {
      System.out.println("JOB TITLE " + en.getKey().getTitle() + ",RESUME SIMILARITY " + en.getKey().getResumeSimilarityScore());

    }


    Map<Job, Double> userResults = new HashMap<>();

    for (Map.Entry<Job, Double> entry : pageRankResults.entrySet()) {
      Job key = entry.getKey();
      Double pageRankVal = entry.getValue();
      Double resumeSimilarityVal = key.getResumeSimilarityScore();
      double combined = pageRankVal + resumeSimilarityVal;
      userResults.put(key, combined);
      // do whatever with value1 and value2
    }

    System.out.println("THIS IS WHAT THE OUTPUTTED RESULTS WOULD BE");
    System.out.println("============================================");
    System.out.println("============================================");

    Map<Job, Double> sortedUserResults = sortByValue(userResults);
    for (Map.Entry<Job, Double> en : sortedUserResults.entrySet()) {
      System.out.println("Key = " + en.getKey().getTitle() + ","
              + en.getKey().getCompositeSimilarityScore() + " Value = " + en.getValue());
    }

    return sortedUserResults;


  }

}
