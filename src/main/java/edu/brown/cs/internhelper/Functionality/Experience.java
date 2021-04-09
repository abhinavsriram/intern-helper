package edu.brown.cs.internhelper.Functionality;

public class Experience {

  private String id;
  private String end_date;
  private String description;
  private String company;
  private String title;
  private String start_date;

  public Experience() {
    this.id = "";
    this.end_date = "";
    this.description = "";
    this.company = "";
    this.title = "";
    this.start_date = "";

  }

  public void setId(String newId) {
    this.id = newId;
  }

  public String getId() {
    return this.id;
  }

  public void setEnd_Date(String newEndDate) {
    this.end_date = newEndDate;
  }

  public String getEnd_Date() {
    return this.end_date;
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

  public void setStart_Date(String newStartDate) {
    this.start_date = newStartDate;
  }

  public String getStart_Date() {
    return this.start_date;
  }

}
