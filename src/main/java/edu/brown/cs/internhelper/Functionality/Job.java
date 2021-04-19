package edu.brown.cs.internhelper.Functionality;

import edu.brown.cs.internhelper.Graph.Vertex;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents each Job in the database. It extends the Vertex class
 * so that it can be used in the JobGraphBuilder class. The class contains setters
 * and getters for each respective instance variable.
 */
public class Job extends Vertex<Job, JobEdge> {

  private int id;
  private String title;
  private String company;
  private String location;
  private String requiredQualifications;
  private String link;
  private List<Double> similarityScores;
  private double compositeSimilarityScore;
  private double resumeSimilarityScore;
  private double finalScore;
  private double skillsScore;
  private double courseworkScore;
  private double experienceScore;


  /**
   * This is the constructor for the Job class. It initializes each
   * of the instance variables as an empty string, or as 0.0 for doubles.
   * It also calls super() to call the constructor of the Vertex class.
   */
  public Job() {
    super();
    this.id = 0;
    this.title = "";
    this.company = "";
    this.location = "";
    this.requiredQualifications = "";
    this.link = "";
    this.compositeSimilarityScore = 0.0;
    this.resumeSimilarityScore = 0.0;
    this.finalScore = 0.0;
    this.skillsScore = 0.0;
    this.courseworkScore = 0.0;
    this.experienceScore = 0.0;
    this.similarityScores = new ArrayList<>();
  }

  /**
   * This method is a setter for the id instance variable.
   * @param newId is what the id instance variable will be set to.
   */
  public void setId(int newId) {
    this.id = newId;
  }

  /**
   * This method is a getter for the id instance variable.
   * @return the id instance variable.
   */
  public int getId() {
    return this.id;
  }

  /**
   * This method is a setter for the title instance variable.
   * @param newTitle is what the id instance variable will be set to.
   */
  public void setTitle(String newTitle) {
    if (newTitle == null) {
      newTitle = "";
    }
    this.title = newTitle;
  }

  /**
   * This method is a getter for the title instance variable.
   * @return the title instance variable.
   */
  public String getTitle() {
    return this.title;
  }

  /**
   * This method is a setter for the company instance variable.
   * @param newCompany is what the company instance variable will be set to.
   */
  public void setCompany(String newCompany) {
    if (newCompany == null) {
      newCompany = "";
    }
    this.company = newCompany;
  }

  /**
   * This method is a getter for the company instance variable.
   * @return the company instance variable.
   */
  public String getCompany() {
    return this.company;
  }

  /**
   * This method is a setter for the location instance variable.
   * @param newLocation is what the location instance variable will be set to.
   */
  public void setLocation(String newLocation) {
    if (newLocation == null) {
      newLocation = "";
    }
    this.location = newLocation;
  }

  /**
   * This method is a getter for the location instance variable.
   * @return the location instance variable.
   */
  public String getLocation() {
    return this.location;
  }

  /**
   * This method is a setter for the requiredQualifications instance variable.
   * @param newReqQual is what the requiredQualifications instance variable will be set to.
   */
  public void setRequiredQualifications(String newReqQual) {
    if (newReqQual == null) {
      newReqQual = "";
    }
    this.requiredQualifications = newReqQual;
  }

  /**
   * This method is a getter for the requiredQualifications instance variable.
   * @return the requiredQualifications instance variable.
   */
  public String getRequiredQualifications() {
    return this.requiredQualifications;
  }

  /**
   * This method is a setter for the link instance variable.
   * @param newLink is what the link instance variable will be set to.
   */
  public void setLink(String newLink) {
    if (newLink == null) {
      newLink = "";
    }
    this.link = newLink;
  }

  /**
   * This method is a getter for the link instance variable.
   * @return the link instance variable.
   */
  public String getLink() {
    return this.link;
  }

  /**
   * This method is a setter for the similarityScores instance variable.
   * @param newScores is a list of Doubles that the similarityScores instance
   *                  variable will be set to.
   */
  public void setJobSimilarityScores(List<Double> newScores) {
    this.similarityScores = newScores;
  }

  /**
   * This method is a getter for the similarityScores instance variable.
   * @return the similarityScores instance variable.
   */
  public List<Double> getJobSimilarityScores() {
    return this.similarityScores;
  }

  /**
   * This method is a setter for the compositeSimilarityScore instance variable.
   * @param newSimilarityScore is a double that the compositeSimilarityScore instance
   *                           variable will be set to.
   */
  public void setCompositeSimilarityScore(double newSimilarityScore) {
    this.compositeSimilarityScore = newSimilarityScore;
  }

  /**
   * This method is a getter for the compositeSimilarityScore instance variable.
   * @return the compositeSimilarityScore instance variable.
   */
  public double getCompositeSimilarityScore() {
    return this.compositeSimilarityScore;
  }

  /**
   * This method is a setter for the resumeSimilarityScore instance variable.
   * @param newResumeScore is a double that the resumeSimilarityScore instance
   *                           variable will be set to.
   */
  public void setResumeSimilarityScore(double newResumeScore) {
    this.resumeSimilarityScore = newResumeScore;
  }

  /**
   * This method is a getter for the resumeSimilarityScore instance variable.
   * @return the resumeSimilarityScore instance variable.
   */
  public double getResumeSimilarityScore() {
    return this.resumeSimilarityScore;
  }

  /**
   * This method is a setter for the finalScore instance variable.
   * @param newFinalScore is a double that the finalScore instance
   *                           variable will be set to.
   */
  public void setFinalScore(double newFinalScore) {
    this.finalScore = newFinalScore;
  }

  /**
   * This method is a getter for the finalScore instance variable.
   * @return the finalScore instance variable.
   */
  public double getFinalScore() {
    return this.finalScore;
  }

  /**
   * This method is a setter for the skillsScore instance variable.
   * @param newSkillsScore is a double that the skillsScore instance
   *                           variable will be set to.
   */
  public void setSkillsScore(double newSkillsScore) {
    this.skillsScore = newSkillsScore;
  }

  /**
   * This method is a getter for the skillsScore instance variable.
   * @return the skillsScore instance variable.
   */
  public double getSkillsScore() {
    return this.skillsScore;
  }

  /**
   * This method is a setter for the experienceScore instance variable.
   * @param newExperienceScore is a double that the experienceScore instance
   *                           variable will be set to.
   */
  public void setExperienceScore(double newExperienceScore) {
    this.experienceScore = newExperienceScore;
  }

  /**
   * This method is a getter for the experienceScore instance variable.
   * @return the experienceScore instance variable.
   */
  public double getExperienceScore() {
    return this.experienceScore;
  }

  /**
   * This method is a setter for the courseworkScore instance variable.
   * @param newCourseworkScore is a double that the courseworkScore instance
   *                           variable will be set to.
   */
  public void setCourseworkScore(double newCourseworkScore) {
    this.courseworkScore = newCourseworkScore;
  }

  /**
   * This method is a getter for the courseworkScore instance variable.
   * @return the courseworkScore instance variable.
   */
  public double getCourseworkScore() {
    return this.courseworkScore;
  }


}
