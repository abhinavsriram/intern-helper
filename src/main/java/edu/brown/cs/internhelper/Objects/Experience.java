package edu.brown.cs.internhelper.Objects;

/**
 * This class represents an Experience that appears on a Resume. It contains
 * setters and getters for each respective instance variable.
 */
public class Experience {

  private String id;
  private String end_date;
  private String description;
  private String company;
  private String title;
  private String start_date;

  /**
   * This is the constructor for the Experience class. It initializes each
   * of the instance variables as an empty string.
   */
  public Experience() {
    this.id = "";
    this.end_date = "";
    this.description = "";
    this.company = "";
    this.title = "";
    this.start_date = "";

  }

  /**
   * This method is a setter for the id instance variable.
   * @param newId is what the id instance variable will be set to.
   */
  public void setId(String newId) {
    this.id = newId;
  }

  /**
   * This method is a getter for the id instance variable.
   * @return the id instance variable.
   */
  public String getId() {
    return this.id;
  }

  /**
   * This method is a setter for the end_date instance variable.
   * @param newEndDate is what the end_date instance variable will be set to.
   */
  public void setEnd_Date(String newEndDate) {
    this.end_date = newEndDate;
  }

  /**
   * This method is a getter for the end_date instance variable.
   * @return the end_date instance variable.
   */
  public String getEnd_Date() {
    return this.end_date;
  }

  /**
   * This method is a setter for the description instance variable.
   * @param newDescription is what the description instance variable will be set to.
   */
  public void setDescription(String newDescription) {
    this.description = newDescription;
  }

  /**
   * This method is a getter for the description instance variable.
   * @return the description instance variable.
   */
  public String getDescription() {
    return this.description;
  }

  /**
   * This method is a setter for the company instance variable.
   * @param newCompany is what the company instance variable will be set to.
   */
  public void setCompany(String newCompany) {
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
   * This method is a setter for the title instance variable.
   * @param newTitle is what the id instance variable will be set to.
   */
  public void setTitle(String newTitle) {
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
   * This method is a setter for the start_date instance variable.
   * @param newStartDate is what the start_date instance variable will be set to.
   */
  public void setStart_Date(String newStartDate) {
    this.start_date = newStartDate;
  }

  /**
   * This method is a getter for the start_date instance variable.
   * @return the start_date instance variable.
   */
  public String getStart_Date() {
    return this.start_date;
  }

}
