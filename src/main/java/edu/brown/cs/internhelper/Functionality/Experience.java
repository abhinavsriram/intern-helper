package edu.brown.cs.internhelper.Functionality;

public class Experience {

  private String id;
  private String endDate;
  private String description;
  private String company;
  private String title;
  private String startDate;

  public Experience() {
    this.id = "";
    this.endDate = "";
    this.description = "";
    this.company = "";
    this.title = "";
    this.startDate = "";

  }

  public void setId(String newId) {
    this.id = newId;
  }

  public String getId() {
    return this.id;
  }

  public void setEndDate(String newEndDate) {
    this.endDate = newEndDate;
  }

  public String getEndDate() {
    return this.endDate;
  }

  public void setDescription(String newDescription) {
    this.description = newDescription;
  }

  public String getDescription() {
    return this.description;
  }

  public void setCompany(String newCompany) {
    this.company = newCompany;
  }

  public String getCompany() {
    return this.company;
  }

  public void setTitle(String newTitle) {
    this.title = newTitle;
  }

  public String getTitle() {
    return this.title;
  }

  public void setStartDate(String newStartDate) {
    this.startDate = newStartDate;
  }

  public String getStartDate() {
    return this.startDate;
  }

}
