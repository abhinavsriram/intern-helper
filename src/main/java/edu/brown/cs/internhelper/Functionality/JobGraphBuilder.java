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

  public JobGraphBuilder(List<Job> jobs, DirectedGraph directedGraph) {
    allJobs = jobs;
    graph = directedGraph;

  }


  public void readData(SQLDatabase db, String tableName) {
    ResultSet rs = db.runQuery("SELECT * FROM " + tableName + "LIMIT 250");
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
    }

  }

  public void calculateJobScores() {

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


          if (currJob == "" || otherJob == "") {
            scores.add(0.0);
          }
          else {

            currJob = currJob.replace("\n", "").replace("\r", "");
            currJob = currJob.replaceAll("[-.^:,•]","");
            Set<String> currJobSet = similarityCalculator.removeStopWords(currJob);

            otherJob = otherJob.replace("\n", "").replace("\r", "");
            otherJob = otherJob.replaceAll("[-.^:,•]","");
            Set<String> otherJobSet = similarityCalculator.removeStopWords(otherJob);

            Set<String> commonWords =  similarityCalculator.commonWords(otherJobSet, currJobSet);

            double dist = (double) (commonWords.size()) / (currJobSet.size());


            scores.add(dist);
            totalScore+=dist;
          }
        }
      }

      allJobs.get(i).setJobSimilarityScores(scores);
      double compositeScore = (totalScore) / (scores.size());
      allJobs.get(i).setCompositeSimilarityScore(compositeScore);

    }
  }


  public void buildJobGraph() {
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


  public Map<Job, Double> runPageRank() {
    PageRank pageRank = new PageRank(graph);
    Map<Job, Double> jobPageRanks = pageRank.calcPageRank();

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
    Map<Job, Double> jobRanksCopy = jobRanks;
    TextSimilarity similarityCalculator = new TextSimilarity();
    try {
      similarityCalculator.loadStopWords("data/stopwords/stopwords.txt");
    } catch (Exception e) {
      e.printStackTrace();
    }

    String skills = user.getSkills();
    skills = skills.replace("\n", "").replace("\r", "");
    skills = skills.replaceAll("[.^:,•]","");
    String coursework = user.getCoursework();
    coursework = coursework.replace("\n", "").replace("\r", "");
    coursework = coursework.replaceAll("[.^:,•]","");
    Resume resume = user.getResume();
    String allResumeDescriptions = "";
//    System.out.println(resume.getResumeExperiences().size());
    for (Experience experience : resume.getResumeExperiences()) {
      allResumeDescriptions = allResumeDescriptions + " " + experience.getDescription();
    }
    allResumeDescriptions = allResumeDescriptions.replace("\n", "").replace("\r", "");
    allResumeDescriptions = allResumeDescriptions.replaceAll("[.^:,•]","");

    //System.out.println(allResumeDescriptions);




    Set<String> skillsSet = similarityCalculator.removeStopWords(skills);
    Set<String> courseworkSet = similarityCalculator.removeStopWords(coursework);
    //System.out.println(skillsSet);
    //System.out.println(courseworkSet);
    Set<String> resumeDescriptionsSet = similarityCalculator.removeStopWords(allResumeDescriptions);

//    System.out.println("SETS ARE: ");
//    System.out.println(skillsSet);
//    System.out.println(courseworkSet);
//     System.out.println(resumeDescriptionsSet.size());


    //skills similarity to current Job qual is 0.4
    //coursework similarity to current Job qual is 0.35
    //all resume  descriptions to current Job qual is 0.25

    for (Map.Entry<Job, Double> en : jobRanksCopy.entrySet()) {
//      String currJobQual = en.getKey().getRequiredQualifications();
//      double skillsSimilarity = distance.similarity(skills, currJobQual);
//      double courseworkSimilarity = distance.similarity(coursework, currJobQual);
//      double descriptionsSimilarity = distance.similarity(allResumeDescriptions, currJobQual);
//      double totalSimilarityScore = 0.4 * skillsSimilarity + 0.35 * courseworkSimilarity +
//          0.25 * descriptionsSimilarity;

      String currJobQual = en.getKey().getRequiredQualifications();
      currJobQual = currJobQual.replace("\n", "").replace("\r", "");
      currJobQual = currJobQual.replaceAll("[.^:,•]","");


      Set<String> jobRoleSet = similarityCalculator.removeStopWords(currJobQual);

//      System.out.println("JOB ROLE SIZE INITIALLY " + jobRoleSet.size());
      //System.out.println("JOB ROLES ARE: ");
//      System.out.println(jobRoleSet);

//      System.out.println("JOB ROLE SIZE BEFORE SKILLS " + jobRoleSet.size());
      Set<String> skillsCommonWords =  similarityCalculator.commonWords(jobRoleSet, skillsSet);
      //System.out.println("SKILLS: ");
//      System.out.println(skillsCommonWords.size());
      double skillsSimilarity = (double) (skillsCommonWords.size()) / (skillsSet.size());
//      System.out.println(en.getKey().getTitle() + "," + en.getKey().getCompany() + "," + skillsSimilarity);
//      System.out.println("JOB ROLE SIZE AFTER SKILLS " + jobRoleSet.size());


//      System.out.println("JOB ROLE SIZE BEFORE COURSE " + jobRoleSet.size());
      Set<String> courseworkCommonWords =  similarityCalculator.commonWords(jobRoleSet, courseworkSet);
      //System.out.println("Coursework: ");
//      System.out.println(courseworkCommonWords.size());
      double courseworkSimilarity = (double) (courseworkCommonWords.size()) / (courseworkSet.size());
//      System.out.println(en.getKey().getTitle() + "," + en.getKey().getCompany() + "," + courseworkSimilarity);

//      System.out.println(courseworkSimilarity);
//      System.out.println("JOB ROLE SIZE AFTER COURSE " + jobRoleSet.size());


//      System.out.println("JOB ROLE SIZE AFTER " + jobRoleSet.size() + " RESUME DESCRIPTION " + resumeDescriptionsSet.size());


      Set<String> resumeDescriptionsCommonWords =  similarityCalculator.commonWords(jobRoleSet, resumeDescriptionsSet);
      //System.out.println("EXPERIENCES: ");

//      System.out.println("RESUME DESCRIPTION SIZE " + resumeDescriptionsCommonWords.size());
      double resumeDescriptionsSimilarity = (double) (resumeDescriptionsCommonWords.size()) / (resumeDescriptionsSet.size());
//      System.out.println(en.getKey().getTitle() + "," + en.getKey().getCompany() + "," + resumeDescriptionsSimilarity);


      double totalSimilarityScore = 0.4 * skillsSimilarity + 0.35 * courseworkSimilarity +
          0.25 * resumeDescriptionsSimilarity;


      en.getKey().setSkillsScore(0.4 * skillsSimilarity);
      en.getKey().setCourseworkScore(0.35 * courseworkSimilarity);
      en.getKey().setExperienceScore(0.25 * resumeDescriptionsSimilarity);
      en.getKey().setResumeSimilarityScore(totalSimilarityScore);

//      System.out.println(en.getKey().getTitle() + "," + en.getKey().getCompany() + "," + totalSimilarityScore);
////
//      System.out.println("===========================");

    }

    return jobRanksCopy;

  }


  public Map<Job, Double> calculateUserResults(User user, Map<Job, Double> pageRankResults) {

    Map<Job, Double> resumeSimilarityResults = this.calculateJobResumeSimilarity(pageRankResults,
        user);

    Map<Job, Double> userResults = new HashMap<>();

    for (Map.Entry<Job, Double> entry : resumeSimilarityResults.entrySet()) {
      Job key = entry.getKey();
      Double pageRankVal = entry.getValue();
      Double resumeSimilarityVal = key.getResumeSimilarityScore();
     // System.out.println(resumeSimilarityVal);
      double combined = pageRankVal + resumeSimilarityVal;
      key.setFinalScore(combined);
      userResults.put(key, combined);
      // do whatever with value1 and value2
    }

    Map<Job, Double> sortedUserResults = sortByValue(userResults);

    return sortedUserResults;

  }


}
