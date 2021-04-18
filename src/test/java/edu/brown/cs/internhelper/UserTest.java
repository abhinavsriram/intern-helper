package edu.brown.cs.internhelper;

import edu.brown.cs.internhelper.Functionality.Experience;
import edu.brown.cs.internhelper.Functionality.Resume;
import edu.brown.cs.internhelper.Functionality.User;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class UserTest {

  @Test
  public void idTest() {
    User newUser = new User();
    newUser.setId("first");
    assertEquals(newUser.getId(), "first");
  }

  @Test
  public void skillsTest() {
    User newUser = new User();
    newUser.setSkills("Microsoft Office");
    assertEquals(newUser.getSkills(), "Microsoft Office");
  }

  @Test
  public void majorGPATest() {
    User newUser = new User();
    newUser.setMajorGPA("4.00");
    assertEquals(newUser.getMajorGPA(), "4.00");
  }

  @Test
  public void majorTest() {
    User newUser = new User();
    newUser.setMajor("Computer Science");
    assertEquals(newUser.getMajor(), "Computer Science");
  }

  @Test
  public void cumulativeGPATest() {
    User newUser = new User();
    newUser.setCumulativeGPA("3.50");
    assertEquals(newUser.getCumulativeGPA(), "3.50");
  }

  @Test
  public void universityTest() {
    User newUser = new User();
    newUser.setUniversity("Brown University");
    assertEquals(newUser.getUniversity(), "Brown University");
  }

  @Test
  public void degreeTest() {
    User newUser = new User();
    newUser.setDegree("B.A.");
    assertEquals(newUser.getDegree(), "B.A.");
  }

  @Test
  public void lastNameTest() {
    User newUser = new User();
    newUser.setLastName("Nelson");
    assertEquals(newUser.getLastName(), "Nelson");
  }

  @Test
  public void courseworkTest() {
    User newUser = new User();
    newUser.setCoursework("Software Engineering, Computer Systems");
    assertEquals(newUser.getCoursework(), "Software Engineering, Computer Systems");
  }

  @Test
  public void firstNameTest() {
    User newUser = new User();
    newUser.setFirstName("Tim");
    assertEquals(newUser.getFirstName(), "Tim");
  }

  @Test
  public void emailTest() {
    User newUser = new User();
    newUser.setEmail("tim_nelson@cs.brown.edu");
    assertEquals(newUser.getEmail(), "tim_nelson@cs.brown.edu");
  }

  @Test
  public void initialProfileSetupCompleteTest() {
    User newUser = new User();
    newUser.setInitialProfileSetupComplete(false);
    assertEquals(false, newUser.getInitialProfileSetupComplete());
  }

  @Test
  public void resumeTest() {
    User newUser = new User();
    Resume newResume = new Resume();
    newUser.setResume(newResume);
    assertEquals(newResume, newUser.getResume());
  }

}
