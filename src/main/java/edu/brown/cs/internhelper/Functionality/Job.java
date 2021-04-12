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
  private List<Double> similarityScores = new ArrayList<>();
  private double compositeSimilarityScore;
  private double resumeSimilarityScore;


  public Job() {
    super();
    this.id = 0;
    this.company = "";
    this.location = "";
    this.requiredQualifications = "";
    this.link = "";
    this.compositeSimilarityScore = 0.0;
    this.resumeSimilarityScore = 0.0;
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


}
