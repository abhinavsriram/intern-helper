package edu.brown.cs.internhelper.Functionality;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * This class handles any cases when we need to find the similarity between different texts.
 * It can clean text to identify only the keywords, and compare two different Sets of Strings
 * to find the text that is common between the two.
 */
public class TextSimilarity {

  private List<String> stopWords;

  /**
   * This method loads any Stop Words that should not be considered for text similarity.
   * For example, this could include "the", "a", "and", etc.
   * The Stop Words are used to initialize the stopWords instance variable.
   * @param filePath is a path to a file that contains all the requisite Stop Words.
   * @throws IOException if there are issues opening/reading the file at the end of the
   * filePath.
   */
  public void loadStopWords(String filePath) throws IOException {

    stopWords = Files.readAllLines(Paths.get(filePath));

  }

  /**
   * This method is a getter for the stopWords instance variable.
   * @return the stopWords instance variable.
   */
  public List<String> getStopWords() {
    return stopWords;
  }

  /**
   * This method removes any Stop Words from the String that is passed in as an argument.
   * It uses the stopWords instance variable to identify which words to remove
   * @param original is the initial String that is passed in to be cleaned.
   * @return a Set of Strings such that no Stop Words are included from the initial String
   * that was passed in.
   */
  public Set<String> removeStopWords(String original) {
    String formattedOriginal = original.replace("\n", "").
        replace("\r", "").replaceAll("\\p{Punct}", "");

//    System.out.println(original);
//    System.out.println(formattedOriginal);

    Set<String> originalWordsSet =
        Stream.of(formattedOriginal.toLowerCase().split(" "))
            .collect(Collectors.toCollection(HashSet<String>::new));

//    System.out.println(originalWordsSet);
    originalWordsSet.removeAll(stopWords);
    originalWordsSet.removeAll(Collections.singleton(null));
    originalWordsSet.removeAll(Collections.singleton(""));


    Set<String> noStopWordsSet = originalWordsSet;
//    System.out.println(noStopWordsSet);


    return noStopWordsSet;
  }

  /**
   * This method compares to Sets of Strings and returns the keywords that are
   * common between those two Sets.
   * @param biggerset is the larger of the two Sets that are passed in.
   * @param subset is the smaller of the two Sets that are passed in.
   * @return a Set of Strings with all keywords that are common between the two
   * Sets that were passed in
   */
  public Set<String> commonWords(Set<String> biggerset, Set<String> subset) {

//    System.out.println(biggerset);
//    System.out.println(subset);

    Set<String> biggerSetCopy = new HashSet<>();
    Set<String> subSetCopy = new HashSet<>();

    biggerSetCopy.addAll(biggerset);
    subSetCopy.addAll(subset);

//    System.out.println("BIGGER SET " + biggerset.size());
//    System.out.println("SUBSET " + subset.size());
    biggerSetCopy.retainAll(subSetCopy);
    Set<String> commonSet = biggerSetCopy;

//    System.out.println("COMMON SET " + commonSet.size());
    return commonSet;

  }


}
