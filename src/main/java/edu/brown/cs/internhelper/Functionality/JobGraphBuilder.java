package edu.brown.cs.internhelper.Functionality;

import edu.brown.cs.internhelper.Database.SQLDatabase;
import edu.brown.cs.internhelper.Graph.DirectedGraph;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * The JobGraphBuilder class provides a way of reading internship role data, creating a graph based
 * on all opportunities within that particular role, running page rank on that graph, calculating
 * the similarity between each of the opportunities with user resume, and then outputting final
 * list of results which is a combination of page rank with resume similarity score for each
 * of the opportunities.
 *
 */
public class JobGraphBuilder {

  private List<Job> allJobs;
  private static DirectedGraph graph;
  private static final double SKILLSWEIGHT = 0.4; //similarity between user skills and internship
  //opportunity should be multiplied by 0.4 because that should contribute most to final score
  private static final double COURSEWEIGHT = 0.35; //similarity between user coursework and
  // internship opportunity should be multiplied by 0.35 because that should contribute second most
  // to final score
  private static final double EXPERIENCEWEIGHT = 0.25; //similarity between user experiences and
  // internship opportunity should be multiplied by 0.25 because that should contribute least
  // to final score

  /**
   * Constructor takes in an arraylist of internship roles and directed graph to make it accessible
   * to the rest of the class by setting it to the corresponding class variable.
   * <p>
   * @param jobs an arrayList of internship roles
   * @param directedGraph a graph that represents all of the internship opportunities of particular
   *                     type
   */
  public JobGraphBuilder(List<Job> jobs, DirectedGraph directedGraph) {
    allJobs = jobs;
    graph = directedGraph;
  }

  /**
   * Returns nothing.
   * <p>
   * Read from  table within database that should corresponds to particular type of internship role
   * and then iterates through all the rows within that table to fill in allJobs list.
   *
   * @param db a SQLDatabase that data should be read from
   * @param tableName particular table name within database that jobs list should be filled with
   *                  and graph built
   */
  public void readData(SQLDatabase db, String tableName) {
    ResultSet rs = db.runQuery("SELECT * FROM " + tableName + "LIMIT 250"); //select all from table
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
  }

  /**
   * Returns nothing.
   * <p>
   * Calculates the similarity between each internship opportunity and every other internship
   * opportunity so that it can be used to build the graph.
   *
   */
  public void calculateJobScores() {
    TextSimilarity similarityCalculator = new TextSimilarity();
    try {
      similarityCalculator.loadStopWords("data/stopwords/stopwords.txt");
    } catch (Exception e) {
      e.printStackTrace();
    }

    for (int i = 0; i < allJobs.size(); i++) { //iterates through all jobs list
      List scores = new ArrayList(); //list of scores that represent similarity between particular
      //internship opportunity and every other opportunity
      double totalScore = 0.0;
      for (int j = 0; j < allJobs.size(); j++) {
        if (i != j) {
          String currJob = allJobs.get(i).getRequiredQualifications();
          String otherJob = allJobs.get(j).getRequiredQualifications();

          if (currJob == "" || otherJob == "") {
            scores.add(0.0);
          } else {
            //removes new lines and unimportant characters
            currJob = currJob.replace("\n", "").replace("\r", "");
            currJob = currJob.replaceAll("[-.^:,•]", "");
            otherJob = otherJob.replace("\n", "").replace("\r", "");
            otherJob = otherJob.replaceAll("[-.^:,•]", "");
            //removes stop words
            Set<String> currJobSet = similarityCalculator.removeStopWords(currJob);
            Set<String> otherJobSet = similarityCalculator.removeStopWords(otherJob);
            //finds common words between two sets
            Set<String> commonWords =  similarityCalculator.commonWords(otherJobSet, currJobSet);
            //similarity score between two jobs
            double dist = (double) (commonWords.size()) / (currJobSet.size());
            scores.add(dist);
            totalScore += dist;
          }
        }
      }
      allJobs.get(i).setJobSimilarityScores(scores);
      //calculate composite score which is a sum of all the scores divided by the number of jobs
      //minus 1
      double compositeScore = (totalScore) / (scores.size());
      allJobs.get(i).setCompositeSimilarityScore(compositeScore);
    }
  }

  /**
   * Returns nothing.
   * <p>
   * Builds graph out of all opportunities. The way it does is this that internship opportunities
   * that are more similar to other opportunities get more incoming edges and then those with
   * that are less similar get more outgoing edges/less incoming edges.
   *
   */
  public void buildJobGraph() {
    for (int i = 0; i < allJobs.size(); i++) {
      graph.addVertex(allJobs.get(i));
    }

    for (int i = 0; i < allJobs.size(); i++) {
      for (int j = 0; j < allJobs.size(); j++) {
        if (i != j) {
          Job currJob = allJobs.get(i);
          Job otherJob = allJobs.get(j);
          //draws edge between every other job with less composite similarity score
          if (currJob.getCompositeSimilarityScore() <= otherJob.getCompositeSimilarityScore()) {
            JobEdge e = new JobEdge(currJob, otherJob, 1);
            graph.addEdge(e);
          }
        }
      }
    }
  }

  /**
   * Returns a map in which key represents job and value is page rank.
   * <p>
   * Runs page rank on the graph.
   *
   * @return     map in which key represents job and value represents page rank
   */
  public Map<Job, Double> runPageRank() {
    PageRank pageRank = new PageRank(graph);
    Map<Job, Double> jobPageRanks = pageRank.calcPageRank();
    return jobPageRanks;
  }

  /**
   * Returns a sorted LinkedHashMap with keys being Job and values being doubles.
   * <p>
   * This method sorts a HashMap using a Comparator to compare each entries with value with the
   * other and then putting the results into a LinkedHashMap in order to preserve order.
   *
   * @param hm a HashMap whose keys are Job and values are doubles
   * @return     a LinkedHashMap representing keys as jobs and values as
   *             ranks in sorted ascending order
   */
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

  /**
   * Returns a map in which key represents job and value represents page rank.
   * <p>
   * Calculates similarity between each internship opportunity and resume.
   *
   * @param jobRanks a HashMap whose keys are Job and values are page ranks
   * @param user a User whose resume we are trying to output results for
   * @return     map in which key represents job and value represents page rank
   */
  public Map<Job, Double> calculateJobResumeSimilarity(Map<Job, Double> jobRanks, User user) {
    Map<Job, Double> jobRanksCopy = jobRanks;
    TextSimilarity similarityCalculator = new TextSimilarity();
    try {
      similarityCalculator.loadStopWords("data/stopwords/stopwords.txt");
    } catch (Exception e) {
      e.printStackTrace();
    }

    String skills = user.getSkills();
    //removes new lines and unimportant characters
    skills = skills.replace("\n", "").replace("\r", "");
    skills = skills.replaceAll("[.^:,•]", "");

    String coursework = user.getCoursework();
    //removes new lines and unimportant characters
    coursework = coursework.replace("\n", "").replace("\r", "");
    coursework = coursework.replaceAll("[.^:,•]", "");

    Resume resume = user.getResume();
    //concatenate all the experience descriptions together
    String allResumeDescriptions = "";
    for (Experience experience : resume.getResumeExperiences()) {
      allResumeDescriptions = allResumeDescriptions + " " + experience.getDescription();
    }
    //removes new lines and unimportant characters
    allResumeDescriptions = allResumeDescriptions.replace("\n", "").
        replace("\r", "");
    allResumeDescriptions = allResumeDescriptions.replaceAll("[.^:,•]", "");

    //removes stop words
    Set<String> skillsSet = similarityCalculator.removeStopWords(skills);
    Set<String> courseworkSet = similarityCalculator.removeStopWords(coursework);
    Set<String> resumeDescriptionsSet = similarityCalculator.removeStopWords(allResumeDescriptions);

    for (Map.Entry<Job, Double> en : jobRanksCopy.entrySet()) {
      String currJobQual = en.getKey().getRequiredQualifications();
      currJobQual = currJobQual.replace("\n", "").replace("\r",
          "");
      currJobQual = currJobQual.replaceAll("[.^:,•]", "");

      Set<String> jobRoleSet = similarityCalculator.removeStopWords(currJobQual);

      //finds common words between two sets
      Set<String> skillsCommonWords =  similarityCalculator.commonWords(jobRoleSet, skillsSet);
      double skillsSimilarity = (double) (skillsCommonWords.size()) / (skillsSet.size());

      Set<String> courseworkCommonWords =
          similarityCalculator.commonWords(jobRoleSet, courseworkSet);
      double courseworkSimilarity =
          (double) (courseworkCommonWords.size()) / (courseworkSet.size());

      Set<String> resumeDescriptionsCommonWords =
          similarityCalculator.commonWords(jobRoleSet, resumeDescriptionsSet);
      double resumeDescriptionsSimilarity =
          (double) (resumeDescriptionsCommonWords.size()) / (resumeDescriptionsSet.size());

      //combines how similar the internship opportunity is to the user's resume info with
      //skills, course, and resume descriptions combined
      //multiply each of the components by how important they should be in the final score
      double totalSimilarityScore = SKILLSWEIGHT * skillsSimilarity
          + COURSEWEIGHT * courseworkSimilarity
          + EXPERIENCEWEIGHT * resumeDescriptionsSimilarity;

      en.getKey().setSkillsScore(SKILLSWEIGHT * skillsSimilarity);
      en.getKey().setCourseworkScore(COURSEWEIGHT * courseworkSimilarity);
      en.getKey().setExperienceScore(EXPERIENCEWEIGHT * resumeDescriptionsSimilarity);
      en.getKey().setResumeSimilarityScore(totalSimilarityScore);
    }
    return jobRanksCopy;
  }

  /**
   * Returns a map in which key represents job and value represents final ranking which is a
   * combination of page rank and similarity to resume.
   * <p>
   * Calls on calculateJobResumeSimilarity method and combines with page rank score to then
   * get final score.
   *
   * @param user a User whose resume we are trying to output results for
   * @param pageRankResults a HashMap whose keys are Job and values are page ranks
   * @return     map in which key represents job and value represents final ranking
   */
  public Map<Job, Double> calculateUserResults(User user, Map<Job, Double> pageRankResults) {
    Map<Job, Double> resumeSimilarityResults = this.calculateJobResumeSimilarity(pageRankResults,
        user);
    Map<Job, Double> userResults = new HashMap<>();

    for (Map.Entry<Job, Double> entry : resumeSimilarityResults.entrySet()) {
      Job key = entry.getKey();
      Double pageRankVal = entry.getValue();
      Double resumeSimilarityVal = key.getResumeSimilarityScore();
      double combined = pageRankVal + resumeSimilarityVal;
      key.setFinalScore(combined);
      userResults.put(key, combined);
    }

    Map<Job, Double> sortedUserResults = sortByValue(userResults);
    return sortedUserResults;
  }

}
