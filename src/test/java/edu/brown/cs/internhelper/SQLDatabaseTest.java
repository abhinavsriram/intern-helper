package edu.brown.cs.internhelper;

import edu.brown.cs.internhelper.Database.SQLDatabase;
import org.junit.Test;

import java.sql.ResultSet;

import static org.junit.Assert.assertEquals;

public class SQLDatabaseTest {
  
  @Test
  public void connectDatabaseTest() throws Exception {
    SQLDatabase sqlData = new SQLDatabase();
    assertEquals(true, sqlData.connectDatabase("jdbc:sqlite:data/sample_intern_data.sqlite3"));
    sqlData.getConn().close();

    assertEquals(false, sqlData.connectDatabase(null));


  }

  @Test
  public void runQueryTest() throws Exception {
    SQLDatabase sqlData = new SQLDatabase();
    ResultSet noConnectionDB = sqlData.runQuery("This is a random query");
    assertEquals(null, noConnectionDB);

    sqlData.connectDatabase("jdbc:sqlite:data/sample_intern_data.sqlite3");
    ResultSet noValidQuery = sqlData.runQuery("This is random query");
    assertEquals(null, noValidQuery);
    sqlData.getConn().close();

    sqlData.connectDatabase("jdbc:sqlite:data/sample_intern_data.sqlite3");
    ResultSet validResult = sqlData.runQuery("SELECT * FROM intern");

    int size = 0;
    while (validResult.next()) {
      size+=1;
    }
    assertEquals(15, size);

    sqlData.getConn().close();


  }

  @Test
  public void runTableNamesTest() throws Exception {
    SQLDatabase sqlData = new SQLDatabase();

    sqlData.connectDatabase("jdbc:sqlite:data/sample_intern_data.sqlite3");

    assertEquals(1, sqlData.getTableNames().size());

    sqlData.getConn().close();

    sqlData.connectDatabase(null);


    assertEquals(0, sqlData.getTableNames().size());

    sqlData.getConn().close();





  }




}
