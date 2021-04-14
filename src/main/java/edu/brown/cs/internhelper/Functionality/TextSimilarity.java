package edu.brown.cs.internhelper.Functionality;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TextSimilarity {

  private List<String> stopWords;

  public void loadStopWords(String filePath) throws IOException {

    stopWords = Files.readAllLines(Paths.get(filePath));

  }

  public Set<String> removeStopWords (String original) {
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

  public Set<String> commonWords (Set<String> biggerset, Set<String> subset) {

//    System.out.println(biggerset);
//    System.out.println(subset);

    biggerset.retainAll(subset);
    Set<String> commonSet = biggerset;

//    System.out.println(commonSet);
   return commonSet;

  }


}
