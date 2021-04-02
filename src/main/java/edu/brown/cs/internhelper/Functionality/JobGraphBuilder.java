package edu.brown.cs.internhelper.Functionality;

import edu.brown.cs.internhelper.Database.SQLDatabase;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;



public class JobGraphBuilder {

  List<Job> allJobs = new ArrayList<>();

  public void readData() {

    SQLDatabase db = new SQLDatabase();
    db.connectDatabase("jdbc:sqlite:data/sample_intern_data.sqlite3");
    ResultSet rs = db.runQuery("SELECT * FROM intern");
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

  public void printJobScores() {
    for (int i = 0; i < allJobs.size(); i++) {
      List currScores = allJobs.get(i).getSimilarityScores();
      System.out.println(currScores.size());
    }

  }



}
