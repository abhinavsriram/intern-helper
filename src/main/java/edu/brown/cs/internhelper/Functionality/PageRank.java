package edu.brown.cs.internhelper.Functionality;

import edu.brown.cs.internhelper.Graph.DirectedGraph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class PageRank {

  private DirectedGraph<Job, JobEdge> graph;
  private List<Job> vertices;
  private Map<Job, Double> resultRanksMap;
  private static final double DAMPINGFACTOR = 0.85;
  private static final double ERROR = 0.01;
  private static final int MAXITERATIONS = 100;
  private List<Double> previousRanks;
  private List<Double> currentRanks;
  private List<Integer> outgoingEdges;
  private Set<Integer> sinks;

  public PageRank(DirectedGraph<Job, JobEdge> g) {
    this.graph = g;
    this.vertices = new ArrayList<Job>();
    this.outgoingEdges = new ArrayList<>();
    this.previousRanks = new ArrayList<>();
    this.currentRanks = new ArrayList<>();
    this.sinks = new HashSet<>();
    this.resultRanksMap = new HashMap<Job, Double>();
  }

  public Map<Job, Double> calcPageRank() {

    int iterationsCounter = 0;
    this.fillVertices();
    this.fillOutgoingEdges();
    this.fillCurrentRanks();
    this.handleSinks();

    double distributedDampFactor = (1 - DAMPINGFACTOR) / vertices.size();

    do {
      previousRanks = List.copyOf(currentRanks);

      for (int i = 0; i < vertices.size(); i++) {
        currentRanks.set(i, 0.0);
      }

      for (int i = 0; i < vertices.size(); i++) {
        Set<JobEdge> incomingEdges = graph.getIncomingConnections().get(vertices.get(i));

        for (JobEdge incomingEdge : incomingEdges) {

          Job adjacent = graph.adjacent(vertices.get(i), incomingEdge);
          int adjacentIndex = vertices.indexOf(adjacent);
          double prevPageRankAdjacent = previousRanks.get(adjacentIndex);
          double outgoingEdgesAdjacent = outgoingEdges.get(adjacentIndex);
          double currentRankWithoutDamp = (prevPageRankAdjacent / outgoingEdgesAdjacent)
              + currentRanks.get(i);

          currentRanks.set(i, currentRankWithoutDamp);

        }

        double currentRank = distributedDampFactor + (currentRanks.get(i) * DAMPINGFACTOR);

        currentRanks.set(i, currentRank);
        resultRanksMap.put(vertices.get(i), currentRank);
      }
      iterationsCounter++;
    }
    while (!this.stopCalcPageRank(currentRanks, previousRanks)
        || iterationsCounter <= MAXITERATIONS);

    return resultRanksMap;
  }

  private void handleSinks() {
    for (int sink : sinks) {
      for (int i = 0; i < vertices.size(); i++) {
        JobEdge e = new JobEdge(vertices.get(sink), vertices.get(i), 0);
        graph.addEdge(e);
      }
      int numVertices = graph.numOutgoingConnections();
      outgoingEdges.set(sink, numVertices);
    }
  }

  private boolean stopCalcPageRank(List<Double> currentIterationRanks,
                                   List<Double> previousIterationRanks) {

    List<Double> belowError = new ArrayList<>();

    for (int i = 0; i < currentIterationRanks.size(); i++) {
      double diff = currentIterationRanks.get(i) - previousIterationRanks.get(i);
      if (diff <= ERROR) {
        belowError.add(diff);
      }
    }

    return (belowError.size() == currentIterationRanks.size());
  }

  public void fillVertices() {
    for (Job job : graph.getOutgoingConnections().keySet()) {
      vertices.add(job);
    }
  }

  public void fillOutgoingEdges() {
    for (int i = 0; i < vertices.size(); i++) {
      int numEdges = graph.getOutgoingConnections().get(vertices.get(i)).size();
      outgoingEdges.add(i, numEdges);
      if (numEdges  == 0) {
        sinks.add(i);
      }
    }
  }

  public void fillCurrentRanks() {
    double initialRank;
    try {
      initialRank = (1) / (graph.numOutgoingConnections());
    } catch (Exception e) {
      initialRank = 0.0;
    }
    for (int i = 0; i < vertices.size(); i++) {
      currentRanks.add(i, initialRank);
    }
  }


}
