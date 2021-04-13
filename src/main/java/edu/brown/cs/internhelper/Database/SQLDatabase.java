package edu.brown.cs.internhelper.Database;

import edu.brown.cs.internhelper.Csv.CsvWriter;
import edu.brown.cs.internhelper.Functionality.Job;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SQLDatabase {

  static Connection conn;
  private Statement stat;

  public boolean connectDatabase(String pathToDatabase) {
    try {
      Class.forName("org.sqlite.JDBC");
      this.conn = DriverManager.getConnection(pathToDatabase);
      this.stat = this.conn.createStatement();
      return true;
    } catch (Exception e) {
      return false;
    }
  }


  public ResultSet runQuery(String query) {
    ResultSet rs = null;
    try {
      this.stat = this.conn.createStatement();
      rs = this.stat.executeQuery(query);
      return rs;
    } catch (Exception e) {
      return rs;
    }
  }

  public Connection getConn() {
    return conn;
  }

  public List<String> getTableNames () {
    List<String> tableNames = new ArrayList<>();
    try {
      DatabaseMetaData md = conn.getMetaData();
      ResultSet rs = md.getTables(null, null, "%", null);
      while (rs.next()) {
        String tableName = rs.getString(3);
        tableNames.add(tableName);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return tableNames;
  }


}
