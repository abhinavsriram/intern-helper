
package edu.brown.cs.internhelper.Objects;

/**
 * This class represents a User who is using InternHelper. It contains
 * setters and getters for each respective instance variable.
 */
public class User {

  private String id;
  private String skills;
  private String major_gpa;
  private String major;
  private String cumulative_gpa;
  private String university;
  private String degree;
  private String last_name;
  private String coursework;
  private String first_name;
  private String email;
  private Resume resume;

  /**
   * This is the constructor for the User class. It initializes each
   * of the instance variables as an empty string (except for resume).
   */
  public User() {
    this.id = "";
    this.skills = "";
    this.major_gpa = "";
    this.major = "";
    this.cumulative_gpa = "";
    this.university = "";
    this.degree = "";
    this.last_name = "";
    this.coursework = "";
    this.first_name = "";
    this.email = "";
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
   * This method is a setter for the skills instance variable.
   * @param newSkills is what the skills instance variable will be set to.
   */
  public void setSkills(String newSkills) {
    this.skills = newSkills;
  }

  /**
   * This method is a getter for the skills instance variable.
   * @return the skills instance variable.
   */
  public String getSkills() {
    return this.skills;
  }

  /**
   * This method is a setter for the major_gpa instance variable.
   * @param newMajorGPA is what the major_gpa instance variable will be set to.
   */
  public void setMajor_GPA(String newMajorGPA) {
    this.major_gpa = newMajorGPA;
  }

  /**
   * This method is a getter for the major_gpa instance variable.
   * @return the major_gpa instance variable.
   */
  public String getMajor_GPA() {
    return this.major_gpa;
  }

  /**
   * This method is a setter for the major instance variable.
   * @param newMajor is what the major instance variable will be set to.
   */
  public void setMajor(String newMajor) {
    this.major = newMajor;
  }

  /**
   * This method is a getter for the major instance variable.
   * @return the major instance variable.
   */
  public String getMajor() {
    return this.major;
  }

  /**
   * This method is a setter for the cumulative_gpa instance variable.
   * @param newCumulativeGPA is what the cumulative_gpa instance variable will be set to.
   */
  public void setCumulative_GPA(String newCumulativeGPA) {
    this.cumulative_gpa = newCumulativeGPA;
  }

  /**
   * This method is a getter for the cumulative_gpa instance variable.
   * @return the cumulative_gpa instance variable.
   */
  public String getCumulative_GPA() {
    return this.cumulative_gpa;
  }

  /**
   * This method is a setter for the university instance variable.
   * @param newUniversity is what the university instance variable will be set to.
   */
  public void setUniversity(String newUniversity) {
    this.university = newUniversity;
  }

  /**
   * This method is a getter for the university instance variable.
   * @return the university instance variable.
   */
  public String getUniversity() {
    return this.university;
  }

  /**
   * This method is a setter for the degree instance variable.
   * @param newDegree is what the degree instance variable will be set to.
   */
  public void setDegree(String newDegree) {
    this.degree = newDegree;
  }

  /**
   * This method is a getter for the degree instance variable.
   * @return the degree instance variable.
   */
  public String getDegree() {
    return this.degree;
  }

  /**
   * This method is a setter for the last_name instance variable.
   * @param newLastName is what the last_name instance variable will be set to.
   */
  public void setLast_Name(String newLastName) {
    this.last_name = newLastName;
  }

  /**
   * This method is a getter for the last_name instance variable.
   * @return the last_name instance variable.
   */
  public String getLast_Name() {
    return this.last_name;
  }

  /**
   * This method is a setter for the coursework instance variable.
   * @param newCoursework is what the coursework instance variable will be set to.
   */
  public void setCoursework(String newCoursework) {
    this.coursework = newCoursework;
  }

  /**
   * This method is a getter for the coursework instance variable.
   * @return the coursework instance variable.
   */
  public String getCoursework() {
    return this.coursework;
  }

  /**
   * This method is a setter for the first_name instance variable.
   * @param newFirstName is what the first_name instance variable will be set to.
   */
  public void setFirst_Name(String newFirstName) {
    this.first_name = newFirstName;
  }

  /**
   * This method is a getter for the first_name instance variable.
   * @return the first_name instance variable.
   */
  public String getFirst_Name() {
    return this.first_name;
  }

  /**
   * This method is a setter for the email instance variable.
   * @param newEmail is what the email instance variable will be set to.
   */
  public void setEmail(String newEmail) {
    this.email = newEmail;
  }

  /**
   * This method is a getter for the email instance variable.
   * @return the email instance variable.
   */
  public String getEmail() {
    return this.email;
  }

  /**
   * This method is a setter for the resume instance variable.
   * @param newResume is what the resume instance variable will be set to.
   */
  public void setResume(Resume newResume) {
    this.resume = newResume;
  }

  /**
   * This method is a getter for the resume instance variable.
   * @return the resume instance variable.
   */
  public Resume getResume() {
    return this.resume;
  }

}
