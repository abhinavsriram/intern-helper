package edu.brown.cs.internhelper.CSV;

/**
 * This class parses a CSV file and modifies it as needed.
 */
public class CSVParser {

  /**
   * This method parses a line in a CSV file and splits it to get the essential portions.
   * @param line is a String in a CSV file
   * @return an array of Strings that contains the essential portions of the original String
   */
  public String[] parseCSV(String line) {
    String[] splitLines = line.split(",(?=(?:[^\\\"]*\\\"[^\\\"]*\\\")*[^\\\"]*$)");
    return splitLines;
  }
}
