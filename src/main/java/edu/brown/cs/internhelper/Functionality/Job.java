package edu.brown.cs.internhelper.Functionality;

import edu.brown.cs.internhelper.Graph.Vertex;

import java.util.ArrayList;
import java.util.List;

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


  public Job() {
    super();
    this.id = 0;
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

  public void setId(int newId) {
    this.id = newId;
  }

  public int getId() {
    return this.id;
  }

  public void setTitle(String newTitle) {
    if (newTitle == null) {
      newTitle= "";
    }
    this.title = newTitle;
  }

  public String getTitle() {
    return this.title;
  }

  public void setCompany(String newCompany) {
    if (newCompany == null) {
      newCompany= "";
    }
    this.company = newCompany;
  }

  public String getCompany() {
    return this.company;
  }

  public void setLocation(String newLocation) {
    if (newLocation == null) {
      newLocation= "";
    }
    this.location = newLocation;
  }

  public String getLocation() {
    return this.location;
  }

  public void setRequiredQualifications(String newReqQual) {
    if (newReqQual == null) {
      newReqQual= "";
    }
    this.requiredQualifications = newReqQual;
  }

  public String getRequiredQualifications() {
    return this.requiredQualifications;
  }

  public void setLink(String newLink) {
    if (newLink == null) {
      newLink= "";
    }
    this.link = newLink;
  }

  public String getLink() {
    return this.link;
  }

  public void setJobSimilarityScores(List<Double> newScores) {
    this.similarityScores = newScores;
  }

  public List<Double> getJobSimilarityScores() {
    return this.similarityScores;
  }

  public void setCompositeSimilarityScore(double newSimilarityScore) {
    this.compositeSimilarityScore = newSimilarityScore;
  }

  public double getCompositeSimilarityScore() {
    return this.compositeSimilarityScore;
  }

  public void setResumeSimilarityScore(double newResumeScore) {
    this.resumeSimilarityScore = newResumeScore;
  }

  public double getResumeSimilarityScore() {
    return this.resumeSimilarityScore;
  }

  public void setFinalScore(double newFinalScore) {
    this.finalScore = newFinalScore;
  }

  public double getFinalScore() {
    return this.finalScore;
  }

  public void setSkillsScore(double newSkillsScore) {
    this.skillsScore = newSkillsScore;
  }

  public double getSkillsScore() {
    return this.skillsScore;
  }

  public void setExperienceScore(double newExperienceScore) {
    this.experienceScore = newExperienceScore;
  }

  public double getExperienceScore() {
    return this.experienceScore;
  }

  public void setCourseworkScore(double newCourseworkScore) {
    this.courseworkScore = newCourseworkScore;
  }

  public double getCourseworkScore() {
    return this.courseworkScore;
  }



}
