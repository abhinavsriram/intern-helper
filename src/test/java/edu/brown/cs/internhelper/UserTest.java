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
    newUser.setMajor_GPA("4.00");
    assertEquals(newUser.getMajor_GPA(), "4.00");
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
    newUser.setCumulative_GPA("3.50");
    assertEquals(newUser.getCumulative_GPA(), "3.50");
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
    newUser.setLast_Name("Nelson");
    assertEquals(newUser.getLast_Name(), "Nelson");
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
    newUser.setFirst_Name("Tim");
    assertEquals(newUser.getFirst_Name(), "Tim");
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
    newUser.setInitial_Profile_Setup_Complete(false);
    assertEquals(newUser.getInitial_Profile_Setup_Complete(), false);
  }

  @Test
  public void resumeTest() {
    User newUser = new User();
    Resume newResume = new Resume();
    newUser.setResume(newResume);
    assertEquals(newUser.getResume(), newResume);
  }

}
