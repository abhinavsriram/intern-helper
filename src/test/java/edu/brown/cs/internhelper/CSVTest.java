package edu.brown.cs.internhelper;

import edu.brown.cs.internhelper.CSV.CSVWriter;
import edu.brown.cs.internhelper.Database.SQLDatabase;
import edu.brown.cs.internhelper.Functionality.Job;
import org.junit.Test;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class CSVTest {
  
  @Test
  public void mapToCSVTest() {
    Map<Job, Double> newMap = new HashMap<>();
    Job jobOne = new Job();
    Job jobTwo = new Job();
    newMap.put(jobOne, 0.75);
    newMap.put(jobTwo, 0.88);
    CSVWriter newCSV = new CSVWriter(newMap, "testCSVFile");
    assertEquals(true, newCSV.mapToCsv());
  }

}
