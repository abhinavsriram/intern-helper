package edu.brown.cs.internhelper.Functionality;

import edu.brown.cs.internhelper.Database.SQLDatabase;
import edu.brown.cs.internhelper.Graph.DirectedGraph;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
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


  public void readData(SQLDatabase db, String tableName) {

    /**
    SQLDatabase db = new SQLDatabase();
    db.connectDatabase("jdbc:sqlite:data/python_scripts/internships.sqlite3");


    Connection conn = db.getConn();
    DatabaseMetaData md = conn.getMetaData();
    try {
      ResultSet rs = md.getTables(null, null, "%", null);
      while (rs.next()) {
        System.out.print(rs.getString(1));
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
     **/



//    SQLDatabase db = new SQLDatabase();
//    db.connectDatabase("jdbc:sqlite:data/python_scripts/internships.sqlite3");
    ResultSet rs = db.runQuery("SELECT * FROM " + tableName + "LIMIT 250");
    allJobs = new ArrayList<>();
    try {
      while (rs.next()) {
        Job job = new Job();
        job.setId(rs.getInt(1));
        job.setTitle(rs.getString(2));
        job.setCompany(rs.getString(3));
        job.setLocation(rs.getString(4));
        job.setRequiredQualifications(rs.getString(5));
        job.setLink(rs.getString(6));
        allJobs.add(job);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    System.out.println(allJobs.size());


  }

  public void calculateJobScores() {
    //LevenshteinDistance distance = new LevenshteinDistance();

    TextSimilarity similarityCalculator = new TextSimilarity();
    try {
      similarityCalculator.loadStopWords("data/stopwords/stopwords.txt");
    } catch (Exception e) {
      e.printStackTrace();
    }



    for (int i = 0; i < allJobs.size(); i++) {
      List scores = new ArrayList();
      double totalScore = 0.0;
      for (int j = 0; j < allJobs.size(); j++) {
        if (i != j) {
          String currJob = allJobs.get(i).getRequiredQualifications();



          String otherJob = allJobs.get(j).getRequiredQualifications();






          if (currJob == null || otherJob == null ) {
            scores.add(0.0);
          }
          else {
            //double dist = distance.similarity(currJob, otherJob);

            currJob = currJob.replace("\n", "").replace("\r", "");
            currJob = currJob.replaceAll("[-.^:,•]","");
            Set<String> currJobSet = similarityCalculator.removeStopWords(currJob);

            otherJob = otherJob.replace("\n", "").replace("\r", "");
            otherJob = otherJob.replaceAll("[-.^:,•]","");
            Set<String> otherJobSet = similarityCalculator.removeStopWords(otherJob);

            Set<String> commonWords =  similarityCalculator.commonWords(currJobSet, otherJobSet);

            double dist = (double) (commonWords.size()) / (currJobSet.size());

            scores.add(dist);
            totalScore+=dist;
          }
        }
        // compare list.get(i) and list.get(j)
      }

      allJobs.get(i).setJobSimilarityScores(scores);
      double compositeScore = (totalScore) / (scores.size());
      allJobs.get(i).setCompositeSimilarityScore(compositeScore);
      System.out.println("JOB TITLE " + allJobs.get(i).getTitle() + ", COMPOSITE SCORE" + compositeScore);


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

//    Map<Job, Double> temp = sortByValue(jobPageRanks);
//    System.out.println("THIS IS WHAT THE PAGE RANK RESULTS WOULD BE");
//    System.out.println("============================================");
//    System.out.println("============================================");
//    for (Map.Entry<Job, Double> en : temp.entrySet()) {
//      System.out.println("Key = " + en.getKey().getTitle() + ","
//              + en.getKey().getCompositeSimilarityScore() + " Value = " + en.getValue());
//    }

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

  public Map<Job, Double> calculateJobResumeSimilarity(Map<Job, Double> jobRanks, User user) {
    //LevenshteinDistance distance = new LevenshteinDistance();
    TextSimilarity similarityCalculator = new TextSimilarity();
    try {
      similarityCalculator.loadStopWords("data/stopwords/stopwords.txt");
    } catch (Exception e) {
      e.printStackTrace();
    }

    String skills = user.getSkills();
    String coursework = user.getCoursework();
    Resume resume = user.getResume();
    String allResumeDescriptions = "";
    for (Experience experience : resume.getResumeExperiences()) {
      allResumeDescriptions = allResumeDescriptions + " " + experience.getDescription();
    }
    allResumeDescriptions = allResumeDescriptions.replace("\n", "").replace("\r", "");
    allResumeDescriptions = allResumeDescriptions.replaceAll("[-.^:,•]","");




    Set<String> skillsSet = similarityCalculator.removeStopWords(skills);
    Set<String> courseworkSet = similarityCalculator.removeStopWords(coursework);
    Set<String> resumeDescriptionsSet = similarityCalculator.removeStopWords(allResumeDescriptions);





    //skills similarity to current Job qual is 0.4
    //coursework similarity to current Job qual is 0.35
    //all resume  descriptions to current Job qual is 0.25

    for (Map.Entry<Job, Double> en : jobRanks.entrySet()) {
//      String currJobQual = en.getKey().getRequiredQualifications();
//      double skillsSimilarity = distance.similarity(skills, currJobQual);
//      double courseworkSimilarity = distance.similarity(coursework, currJobQual);
//      double descriptionsSimilarity = distance.similarity(allResumeDescriptions, currJobQual);
//      double totalSimilarityScore = 0.4 * skillsSimilarity + 0.35 * courseworkSimilarity +
//          0.25 * descriptionsSimilarity;

      String currJobQual = en.getKey().getRequiredQualifications();
      currJobQual = currJobQual.replace("\n", "").replace("\r", "");
      currJobQual = currJobQual.replaceAll("[-.^:,•]","");


      Set<String> jobRoleSet = similarityCalculator.removeStopWords(currJobQual);


      Set<String> skillsCommonWords =  similarityCalculator.commonWords(jobRoleSet, skillsSet);
      double skillsSimilarity = (double) (skillsCommonWords.size()) / (skillsSet.size());

      Set<String> courseworkCommonWords =  similarityCalculator.commonWords(jobRoleSet, courseworkSet);
      double courseworkSimilarity = (double) (courseworkCommonWords.size()) / (courseworkSet.size());

      Set<String> resumeDescriptionsCommonWords =  similarityCalculator.commonWords(jobRoleSet, resumeDescriptionsSet);
      double resumeDescriptionsSimilarity = (double) (resumeDescriptionsCommonWords.size()) / (resumeDescriptionsSet.size());

      double totalSimilarityScore = 0.4 * skillsSimilarity + 0.35 * courseworkSimilarity +
          0.25 * resumeDescriptionsSimilarity;

//      System.out.println("SKILLS SIMILARITY: " + skillsSimilarity + "COURSE SIMILARITY: "
//          + courseworkSimilarity + "RESUME SIMILARITY: " + resumeDescriptionsSimilarity);
      //System.out.println("JOB TITLE " + en.getKey().getTitle() + "," + totalSimilarityScore);
      en.getKey().setResumeSimilarityScore(totalSimilarityScore);
    }

    return jobRanks;

  }


  public Map<Job, Double> calculateUserResults(User user, Map<Job, Double> pageRankResults) {

    Map<Job, Double> resumeSimilarityResults = this.calculateJobResumeSimilarity(pageRankResults,
        user);

    Map<Job, Double> userResults = new HashMap<>();

    for (Map.Entry<Job, Double> entry : pageRankResults.entrySet()) {
      Job key = entry.getKey();
      Double pageRankVal = entry.getValue();
      Double resumeSimilarityVal = key.getResumeSimilarityScore();
     // System.out.println(resumeSimilarityVal);
      double combined = pageRankVal + resumeSimilarityVal;
      userResults.put(key, combined);
      // do whatever with value1 and value2
    }

    Map<Job, Double> sortedUserResults = sortByValue(userResults);

    return sortedUserResults;

  }


/**
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
 **/

}
