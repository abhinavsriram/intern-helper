package edu.brown.cs.internhelper.Functionality;

import java.util.ArrayList;
import java.util.List;

public class Resume {

  private List<Experience> resumeExperiences;

  public Resume() {
    this.resumeExperiences = new ArrayList<>();
  }

  public void addExperience(Experience newExperience) {
    this.resumeExperiences.add(newExperience);
  }

  public List<Experience> getResumeExperiences() {
    return this.resumeExperiences;
  }

  public int numResumeExperiences() {
    return this.resumeExperiences.size();
  }

}
