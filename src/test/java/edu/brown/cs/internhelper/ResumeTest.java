package edu.brown.cs.internhelper;

import edu.brown.cs.internhelper.Functionality.Experience;
import edu.brown.cs.internhelper.Functionality.Resume;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class ResumeTest {

  @Test
  public void addExperienceTest() {
    Resume newResume = new Resume();
    Experience firstExperience = new Experience();
    newResume.addExperience(firstExperience);
    assertEquals(newResume.numResumeExperiences(), 1);

    Experience secondExperience = new Experience();
    newResume.addExperience(secondExperience);
    assertEquals(newResume.numResumeExperiences(), 2);
  }

  @Test
  public void numExperiencesTest() {
    Resume newResume = new Resume();
    Experience firstExperience = new Experience();
    newResume.addExperience(firstExperience);
    Experience secondExperience = new Experience();
    newResume.addExperience(secondExperience);
    Experience thirdExperience = new Experience();
    newResume.addExperience(thirdExperience);
    assertEquals(newResume.numResumeExperiences(), newResume.getResumeExperiences().size());
  }


}
