package edu.brown.cs.internhelper.CSV;

import edu.brown.cs.internhelper.Functionality.Job;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CSVWriter {

  private static Map<Job, Double> hmap;
  private static String csvName;

  public CSVWriter(Map<Job, Double> map, String outputCsvName) {
    hmap = map;
    csvName = outputCsvName;

  }

  public void mapToCsv() {
    try {
      File file = new File(csvName);
      boolean fileExists = isFileExists(file);


      //FileWriter writer = new FileWriter(csvName, true);
      FileWriter writer;

      if (fileExists) {
        file.delete();
        writer = new FileWriter(csvName, true);
      }
      else {
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


    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static boolean isFileExists(File file) {
    return file.exists() && !file.isDirectory();
  }


}
