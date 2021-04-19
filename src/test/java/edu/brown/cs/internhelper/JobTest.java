package edu.brown.cs.internhelper;

import edu.brown.cs.internhelper.Objects.Job;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class JobTest {

  @Test
  public void idTest() {
    Job newJob = new Job();
    newJob.setId(1);
    assertEquals(newJob.getId(), 1);
  }

  @Test
  public void titleTest() {
    Job newJob = new Job();
    newJob.setTitle(null);
    assertEquals(newJob.getTitle(), "");
    newJob.setTitle("Research Analyst");
    assertEquals(newJob.getTitle(), "Research Analyst");
  }

  @Test
  public void companyTest() {
    Job newJob = new Job();
    newJob.setCompany(null);
    assertEquals(newJob.getCompany(), "");
    newJob.setCompany("Microsoft");
    assertEquals(newJob.getCompany(), "Microsoft");
  }

  @Test
  public void locationTest() {
    Job newJob = new Job();
    newJob.setLocation(null);
    assertEquals(newJob.getLocation(), "");
    newJob.setLocation("New York");
    assertEquals(newJob.getLocation(), "New York");
  }

  @Test
  public void requiredQualificationTest() {
    Job newJob = new Job();
    newJob.setRequiredQualifications(null);
    assertEquals(newJob.getRequiredQualifications(), "");
    newJob.setRequiredQualifications("STEM Major");
    assertEquals(newJob.getRequiredQualifications(), "STEM Major");
  }

  @Test
  public void linkTest() {
    Job newJob = new Job();
    newJob.setLink(null);
    assertEquals(newJob.getLink(), "");
    newJob.setLink("http://www.linkedin.com/google-jobs");
    assertEquals(newJob.getLink(), "http://www.linkedin.com/google-jobs");
  }

  @Test
  public void jobSimilarityScoreTest() {
    Job newJob = new Job();
    List<Double> jobScores = new ArrayList<>();
    jobScores.add(0.572);
    jobScores.add(0.491);
    jobScores.add(0.936);
    jobScores.add(0.418);
    jobScores.add(0.204);
    newJob.setJobSimilarityScores(jobScores);
    assertEquals(newJob.getJobSimilarityScores(), jobScores);
    assertEquals(newJob.getJobSimilarityScores().size(), 5);
  }

  @Test
  public void compositeSimilarityScoreTest() {
    Job newJob = new Job();
    newJob.setCompositeSimilarityScore(0.543);
    assertEquals(newJob.getCompositeSimilarityScore(), 0.543, 0.000001);
  }

  @Test
  public void resumeSimilarityScoreTest() {
    Job newJob = new Job();
    newJob.setResumeSimilarityScore(0.417);
    assertEquals(newJob.getResumeSimilarityScore(), 0.417, 0.000001);
  }

  @Test
  public void finalScoreTest() {
    Job newJob = new Job();
    newJob.setFinalScore(0.769);
    assertEquals(newJob.getFinalScore(), 0.769, 0.000001);
  }

  @Test
  public void skillsScoreTest() {
    Job newJob = new Job();
    newJob.setSkillsScore(0.377);
    assertEquals(newJob.getSkillsScore(), 0.377, 0.000001);
  }

  @Test
  public void experienceScoreTest() {
    Job newJob = new Job();
    newJob.setExperienceScore(0.714);
    assertEquals(newJob.getExperienceScore(), 0.714, 0.000001);
  }

  @Test
  public void courseworkScoreTest() {
    Job newJob = new Job();
    newJob.setCourseworkScore(0.268);
    assertEquals(newJob.getCourseworkScore(), 0.268, 0.000001);
  }

}
