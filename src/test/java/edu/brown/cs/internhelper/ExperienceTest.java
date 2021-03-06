package edu.brown.cs.internhelper;

import edu.brown.cs.internhelper.Objects.Experience;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ExperienceTest {

  @Test
  public void idTest() {
    Experience newExperience = new Experience();
    newExperience.setId("first");
    assertEquals(newExperience.getId(), "first");
  }

  @Test
  public void endDateTest() {
    Experience newExperience = new Experience();
    newExperience.setEnd_Date("April 2021");
    assertEquals(newExperience.getEnd_Date(), "April 2021");
  }

  @Test
  public void descriptionTest() {
    Experience newExperience = new Experience();
    newExperience.setDescription("Researched and analyzed data");
    assertEquals(newExperience.getDescription(), "Researched and analyzed data");
  }

  @Test
  public void companyTest() {
    Experience newExperience = new Experience();
    newExperience.setCompany("Google");
    assertEquals(newExperience.getCompany(), "Google");
  }

  @Test
  public void titleTest() {
    Experience newExperience = new Experience();
    newExperience.setTitle("Software Engineer");
    assertEquals(newExperience.getTitle(), "Software Engineer");
  }

  @Test
  public void startDateTest() {
    Experience newExperience = new Experience();
    newExperience.setStart_Date("January 2021");
    assertEquals(newExperience.getStart_Date(), "January 2021");
  }

}
