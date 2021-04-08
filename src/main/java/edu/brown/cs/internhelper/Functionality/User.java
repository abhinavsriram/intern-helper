package edu.brown.cs.internhelper.Functionality;

import java.util.ArrayList;
import java.util.List;

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
  private boolean initial_profile_setup_complete;
  private Resume resume;



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
    this.initial_profile_setup_complete = true;
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

  public void setMajor_GPA(String newMajorGPA) {
    this.major_gpa = newMajorGPA;
  }
  public String getMajor_GPA() {
    return this.major_gpa;
  }

  public void setMajor(String newMajor) {
    this.major = newMajor;
  }
  public String getMajor() {
    return this.major;
  }

  public void setCumulative_GPA(String newCumulativeGPA) { this.cumulative_gpa = newCumulativeGPA; }
  public String getCumulative_GPA() {
    return this.cumulative_gpa;
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

  public void setLast_Name(String newLastName) {
    this.last_name = newLastName;
  }
  public String getLast_Name() {
    return this.last_name;
  }

  public void setCoursework(String newCoursework) {
    this.coursework = newCoursework;
  }
  public String getCoursework() {
    return this.coursework;
  }

  public void setFirst_Name(String newFirstName) {
    this.first_name = newFirstName;
  }
  public String getFirst_Name() {
    return this.first_name;
  }

  public void setEmail(String newEmail) {
    this.email = newEmail;
  }
  public String getEmail() {
    return this.email;
  }

  public void setInitial_Profile_Setup_Complete(boolean bool) {
    this.initial_profile_setup_complete = bool;
  }
  public boolean getInitial_Profile_Setup_Complete() {
    return this.initial_profile_setup_complete;
  }

  public void setResume(Resume newResume) {
    this.resume = newResume;
  }
  public Resume getResume() {
    return this.resume;
  }

}
