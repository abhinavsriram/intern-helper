package edu.brown.cs.internhelper.Objects;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents a Resume that belongs to a User. It contains
 * methods to add, get, and count Experiences.
 */
public class Resume {

  private List<Experience> resumeExperiences;

  /**
   * This is the constructor for the Resume class. It initializes the resumeExperiences
   * instance variable as a new ArrayList of Experiences.
   */
  public Resume() {
    this.resumeExperiences = new ArrayList<>();
  }

  /**
   * This method adds an Experience to the Resume by adding the Experience to the
   * resumeExperiences instance variable.
   * @param newExperience is the new Experience to be added to the Resume
   */
  public void addExperience(Experience newExperience) {
    this.resumeExperiences.add(newExperience);
  }

  /**
   * This method is a getter for the resumeExperiences instance variable.
   * @return the resumeExperiences instance variable
   */
  public List<Experience> getResumeExperiences() {
    return this.resumeExperiences;
  }

  /**
   * This method returns the number of Experiences on the Resume.
   * @return the size of the resumeExperiences instance variable
   */
  public int numResumeExperiences() {
    return this.resumeExperiences.size();
  }

}
