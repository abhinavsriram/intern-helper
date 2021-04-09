package edu.brown.cs.internhelper.Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

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


}
