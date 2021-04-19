package edu.brown.cs.internhelper.CSV;

/**
 * The CSVParser class parses a CSV file.
 */
public class CSVParser {

  /**
   * This method parses a line in a CSV file and splits it to get the important portions.
   * @param line is a String in a CSV file
   * @return an array of Strings that contains the important portions of the original String
   */
  public String[] parseCSV(String line) {
    String[] splitLines = line.split(",(?=(?:[^\\\"]*\\\"[^\\\"]*\\\")*[^\\\"]*$)");
    return splitLines;
  }
}
