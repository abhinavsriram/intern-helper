package edu.brown.cs.internhelper.CSV;

public class CSVParser {

  public String[] parseCSV(String line) {
    String[] splitLines = line.split(",");
    return splitLines;
  }
}
