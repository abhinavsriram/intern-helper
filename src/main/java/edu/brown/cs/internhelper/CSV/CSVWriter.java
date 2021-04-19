package edu.brown.cs.internhelper.CSV;

import edu.brown.cs.internhelper.Objects.Job;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * This class writes to a CSV file.
 */
public class CSVWriter {

  private static Map<Job, Double> hmap;
  private static String csvName;

  /**
   * This constructor instantiates the instance variables using the passed arguments.
   * @param map is a Map of Jobs to Doubles
   * @param outputCsvName is a String name of the output CSV file.
   */
  public CSVWriter(Map<Job, Double> map, String outputCsvName) {
    hmap = map;
    csvName = outputCsvName;

  }

  /**
   * This method uses the Map of Jobs to Doubles and writes the data to a CSV file.
   * @return true if no Exceptions thrown, else return false
   */
  public boolean mapToCsv() {
    try {
      File file = new File(csvName);
      boolean fileExists = isFileExists(file);
      FileWriter writer;
      if (fileExists) {
        file.delete();
        writer = new FileWriter(csvName, true);
      } else {
        writer = new FileWriter(csvName, true);
      }

      List<String> csvHeaders = new ArrayList<>();
      csvHeaders.add("Id");
      csvHeaders.add("Title");
      csvHeaders.add("Company");
      csvHeaders.add("Location");
      csvHeaders.add("Required Qualifications");
      csvHeaders.add("Link");
      csvHeaders.add("Page Rank");

      StringBuilder headersStr = new StringBuilder("");
      for (String eachstring : csvHeaders) {
        headersStr.append(eachstring).append(",");
      }

      writer.append(headersStr);

      for (Map.Entry<Job, Double> en : hmap.entrySet()) {
        List<String> rowData = new ArrayList<>();
        Job job = en.getKey();
        rowData.add(String.valueOf(job.getId()));

        String title = job.getTitle().replace("\"", "");
        String company = job.getCompany().replace("\"", "");
        String location = job.getLocation().replace("\"", "");
        String requiredQualifications = job.getRequiredQualifications()
            .replace("\"", "");
        String link = job.getLink().replace("\"", "");

        rowData.add('"' + title + '"');
        rowData.add('"' + company + '"');
        rowData.add('"' + location + '"');
        rowData.add('"' + requiredQualifications + '"');
        rowData.add('"' + link + '"');
        Double pageRank = en.getValue();
        rowData.add(String.valueOf(pageRank));
        StringBuilder rowStr = new StringBuilder("");
        for (String eachstring : rowData) {
          rowStr.append(eachstring).append(",");
        }
        writer.append("\n");
        writer.append(rowStr);
      }

      writer.flush();
      writer.close();
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  /**
   * This method checks whether the passed file exists.
   * @param file is the File that is being checked.
   * @return true if the File exists, else returns false
   */
  public static boolean isFileExists(File file) {
    return file.exists() && !file.isDirectory();
  }


}
