package edu.brown.cs.internhelper.Functionality;

import edu.brown.cs.internhelper.Database.SQLDatabase;
import edu.brown.cs.internhelper.Graph.DirectedGraph;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Iterator;
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
        if ( i != j) {
          String currJob = allJobs.get(i).getRequiredQualifications();
          String otherJob = allJobs.get(j).getRequiredQualifications();
          double dist = distance.similarity(currJob, otherJob);
          scores.add(dist);
        }
        // compare list.get(i) and list.get(j)
      }

      allJobs.get(i).setSimilarityScores(scores);

    }
  }

  public void calculateJobCompositeScore() {
    for (int i = 0; i < allJobs.size(); i++) {
      List <Double> currScores = allJobs.get(i).getSimilarityScores();
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
        if ( i != j) {
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
      Map.Entry<Job, Set<JobEdge>> pair = (Map.Entry)it.next();
      System.out.println("JOB TITLE " + pair.getKey().getTitle() + " who has a score of " + pair.getKey().getCompositeSimilarityScore());
      System.out.println("THESE ARE THE OUTGOING EDGES: ");
      for (JobEdge edge : pair.getValue()) {
        System.out.println("EDGE IS CONNECTED TO : " + edge.getDestinationVertex().getTitle()
            + " who has a score of " + edge.getDestinationVertex().getCompositeSimilarityScore());
      }
      System.out.println("---------------------------------------------");
      it.remove(); // avoids a ConcurrentModificationException
    }
  }




}
