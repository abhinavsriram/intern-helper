package edu.brown.cs.internhelper;

import edu.brown.cs.internhelper.Database.SQLDatabase;
import edu.brown.cs.internhelper.Functionality.TextSimilarity;
import org.junit.Test;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;

public class SQLDatabaseTest {
  
  @Test
  public void connectDatabaseTest() throws IOException {
    SQLDatabase sqlData = new SQLDatabase();
    assertEquals(sqlData.connectDatabase("jdbc:sqlite:data/sample_intern_data"), true);
  }

}
