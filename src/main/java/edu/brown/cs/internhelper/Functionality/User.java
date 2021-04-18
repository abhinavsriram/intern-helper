package edu.brown.cs.internhelper.Functionality;


public class User {

  private String id;
  private String skills;
  private String majorGPA;
  private String major;
  private String cumulativeGPA;
  private String university;
  private String degree;
  private String lastName;
  private String coursework;
  private String firstName;
  private String email;
  private boolean initialProfileSetupComplete;
  private Resume resume;


  public User() {
    this.id = "";
    this.skills = "";
    this.majorGPA = "";
    this.major = "";
    this.cumulativeGPA = "";
    this.university = "";
    this.degree = "";
    this.lastName = "";
    this.coursework = "";
    this.firstName = "";
    this.email = "";
    this.initialProfileSetupComplete = true;
  }

  public void setId(String newId) {
    this.id = newId;
  }

  public String getId() {
    return this.id;
  }

  public void setSkills(String newSkills) {
    this.skills = newSkills;
  }

  public String getSkills() {
    return this.skills;
  }

  public void setMajorGPA(String newMajorGPA) {
    this.majorGPA = newMajorGPA;
  }

  public String getMajorGPA() {
    return this.majorGPA;
  }

  public void setMajor(String newMajor) {
    this.major = newMajor;
  }

  public String getMajor() {
    return this.major;
  }

  public void setCumulativeGPA(String newCumulativeGPA) {
    this.cumulativeGPA = newCumulativeGPA;
  }

  public String getCumulativeGPA() {
    return this.cumulativeGPA;
  }

  public void setUniversity(String newUniversity) {
    this.university = newUniversity;
  }

  public String getUniversity() {
    return this.university;
  }

  public void setDegree(String newDegree) {
    this.degree = newDegree;
  }

  public String getDegree() {
    return this.degree;
  }

  public void setLastName(String newLastName) {
    this.lastName = newLastName;
  }

  public String getLastName() {
    return this.lastName;
  }

  public void setCoursework(String newCoursework) {
    this.coursework = newCoursework;
  }

  public String getCoursework() {
    return this.coursework;
  }

  public void setFirstName(String newFirstName) {
    this.firstName = newFirstName;
  }

  public String getFirstName() {
    return this.firstName;
  }

  public void setEmail(String newEmail) {
    this.email = newEmail;
  }

  public String getEmail() {
    return this.email;
  }

  public void setInitialProfileSetupComplete(boolean bool) {
    this.initialProfileSetupComplete = bool;
  }

  public boolean getInitialProfileSetupComplete() {
    return this.initialProfileSetupComplete;
  }

  public void setResume(Resume newResume) {
    this.resume = newResume;
  }

  public Resume getResume() {
    return this.resume;
  }

}
