package edu.brown.cs.internhelper.Functionality;

import edu.brown.cs.internhelper.Csv.CsvWriter;
import edu.brown.cs.internhelper.Database.SQLDatabase;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.util.Map;

public class CachePageRanks {

  private static JobGraphBuilder graphBuilder;

  public CachePageRanks() {
    graphBuilder = new JobGraphBuilder();
  }

  public void cacheResults() {
    SQLDatabase db = new SQLDatabase();
    db.connectDatabase("jdbc:sqlite:data/python_scripts/internships.sqlite3");
    Connection conn = db.getConn();
    try {
      DatabaseMetaData md = conn.getMetaData();
      ResultSet rs = md.getTables(null, null, "%", null);
      while (rs.next()) {
        String tableName = '"' + rs.getString(3) + '"';
        graphBuilder.readData(db, tableName);
        graphBuilder.calculateJobScores();
        graphBuilder.buildJobGraph();
        Map<Job, Double> pageRanks =  graphBuilder.runPageRank();
        String csvName = "data/page_rank_results/" + rs.getString(3) + "pr.csv";
        CsvWriter csvWriter = new CsvWriter(pageRanks, csvName);
        csvWriter.mapToCsv();
        System.out.println("DONE WITH " + rs.getString(3));
        System.out.println("=========================================");
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

  }



}
