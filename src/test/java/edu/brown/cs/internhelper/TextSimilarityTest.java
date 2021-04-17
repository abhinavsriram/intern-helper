package edu.brown.cs.internhelper;

import edu.brown.cs.internhelper.Functionality.Job;
import edu.brown.cs.internhelper.Functionality.JobEdge;
import edu.brown.cs.internhelper.Functionality.TextSimilarity;
import edu.brown.cs.internhelper.Graph.Edge;
import edu.brown.cs.internhelper.Graph.Vertex;
import org.junit.Test;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;

public class TextSimilarityTest {

  @Test
  public void loadStopWordsTest() throws IOException {
    TextSimilarity textSim = new TextSimilarity();
    textSim.loadStopWords("data/stopwords/stopwords.txt");
    assertEquals(textSim.getStopWords().size(), 435);
  }

  @Test
  public void removeStopWordsTest() throws IOException {
    TextSimilarity textSim = new TextSimilarity();
    textSim.loadStopWords("data/stopwords/stopwords.txt");
    assertEquals(textSim.removeStopWords("Tim Nelson is the best").size(), 2);
  }

  @Test
  public void commonWordsTest() throws IOException {
    TextSimilarity textSim = new TextSimilarity();
    textSim.loadStopWords("data/stopwords/stopwords.txt");
    Set<String> largerSet = new HashSet<>();
    largerSet.add("hello");
    largerSet.add("my");
    largerSet.add("name");
    largerSet.add("is");
    largerSet.add("Tim");
    largerSet.add("Nelson");
    Set<String> smallerSet = new HashSet<>();
    smallerSet.add("my");
    smallerSet.add("favorite");
    smallerSet.add("professor");
    smallerSet.add("is");
    smallerSet.add("Tim");
    smallerSet.add("Nelson");
    assertEquals(textSim.commonWords(largerSet, smallerSet).size(), 4);
  }

}
