package edu.brown.cs.internhelper.Database;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * The SQLDataBase class provides a way of creating a database and executing a query on that
 * same database connection. This class provides accessibility to the database connection that
 * gets passed around through all the other classes.
 *
 */

public class SQLDatabase {

  private static Connection conn;
  private Statement stat;

  /**
   * Returns a boolean that represents if the database was able to successfully be connected (true
   * if that is the case, false otherwise).
   * The pathToDatabase argument must be a String.
   * <p>
   *
   * Connects to the database that is passed in and then creates a statement so queries can be run.
   *
   * @param pathToDatabase a String that represents path to database
   * @return  boolean true if the connection to the database was successful, false otherwise
   */
  public boolean connectDatabase(String pathToDatabase) {
    try {
      Class.forName("org.sqlite.JDBC");
      this.setConn(DriverManager.getConnection(pathToDatabase));
      this.stat = this.getConn().createStatement();
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  /**
   * Returns a ResultSet that gets created when a Statement is created and a query is executed.
   * The query argument must be a String.
   * <p>
   *
   * Runs a query on the connected database.
   *
   * @param query a String that represents desired query to run
   * @return  ResultSet contains the output of the executed query
   */
  public ResultSet runQuery(String query) {
    ResultSet rs = null;
    try {
      this.stat = this.getConn().createStatement();
      rs = this.stat.executeQuery(query);
      return rs;
    } catch (Exception e) {
      return rs;
    }
  }

  /**
   * Returns the Connection established to the database.
   * <p>
   * This method enables access to the database's connection.
   *
   * @return a Connection representing a database's connection
   */
  public Connection getConn() {
    return conn;
  }

  /**
   * Returns nothing.
   * <p>
   * This method enables setting the connection to a database.
   *
   */
  public static void setConn(Connection conn) {
    SQLDatabase.conn = conn;
  }


  /**
   * Returns list of table names within database.
   * <p>
   * This method retrieves a list of all table names within a particular database.
   *
   * @return a list of table names within a database.
   */
  public List<String> getTableNames() {
    List<String> tableNames = new ArrayList<>();
    try {
      DatabaseMetaData md = this.getConn().getMetaData();
      ResultSet rs = md.getTables(null, null, "%", null);
      while (rs.next()) {
        String tableName = rs.getString(3);
        tableNames.add(tableName);
      }
    } catch (Exception e) {
      return tableNames;
    }
    return tableNames;
  }

}
