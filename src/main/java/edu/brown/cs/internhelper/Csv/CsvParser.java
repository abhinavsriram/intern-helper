package edu.brown.cs.internhelper.Csv;

public class CsvParser {

  public String[] parseCSV(String line) {
    String[] splitLines = line.split(",");
    return splitLines;
  }
}
