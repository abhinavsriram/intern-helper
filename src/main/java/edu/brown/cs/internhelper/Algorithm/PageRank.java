package edu.brown.cs.internhelper.Algorithm;

import edu.brown.cs.internhelper.Graph.DirectedGraph;
import edu.brown.cs.internhelper.Graph.Edge;
import edu.brown.cs.internhelper.Graph.Vertex;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * The PageRank class provides a way of running PageRank algorithm on a directed graph.
 *
 * @param <V> extends the Vertex class, representing the vertices of the
 *            Directed Graph.
 * @param <E> extends the Edge class, representing the edges of the Directed
 *            Graph.
 */
public class PageRank<V extends Vertex<V, E>, E extends Edge<V, E>> {

  private DirectedGraph<V, E> graph;
  private List<V> vertices;
  private Map<V, Double> resultRanksMap;
  private static final double DAMPINGFACTOR = 0.85;
  private static final double ERROR = 0.01;
  private static final int MAXITERATIONS = 100;
  private List<Double> previousRanks;
  private List<Double> currentRanks;
  private List<Integer> outgoingEdges;
  private Set<Integer> sinks;

  /**
   * Constructor takes in a directed graph makes it accessible to the rest of the class
   * by setting it to the corresponding class variable.
   * <p>
   * @param g a directed graph
   */
  public PageRank(DirectedGraph<V, E> g) {
    this.graph = g;
    this.vertices = new ArrayList<V>();
    this.outgoingEdges = new ArrayList<>();
    this.previousRanks = new ArrayList<>();
    this.currentRanks = new ArrayList<>();
    this.sinks = new HashSet<>();
    this.resultRanksMap = new HashMap<V, Double>();
  }

  /**
   * Returns a map in which key represents vertex and value represents page rank.
   * <p>
   * Runs page rank algorithm on graph by looking at number of incoming and outgoing edges
   * for each of the vertices on the graph and modifying rank based on that.
   *
   * @return     map in which key represents vertex and value represents page rank
   */
  public Map<V, Double> calcPageRank() {
    int iterationsCounter = 0;
    this.fillVertices(); //initializes vertices list
    this.fillOutgoingEdges(); //initializes outgoing edges list
    this.fillCurrentRanks(); //initializes current ranks
    this.handleSinks(); //handles sinks prior to running core of algorithm so those with no outgoing
    //edges are handled appropriately

    double distributedDampFactor = (1 - DAMPINGFACTOR) / vertices.size(); //distributes
    //damping factor by the number of vertices in graph

    do {
      previousRanks = List.copyOf(currentRanks);

      for (int i = 0; i < vertices.size(); i++) {
        currentRanks.set(i, 0.0);
      }

      for (int i = 0; i < vertices.size(); i++) {
        //gets set of incoming edges
        Set<E> incomingEdges = graph.getIncomingConnections().get(vertices.get(i));
        //iterates through all incoming edges for a particular vertex
        for (E incomingEdge : incomingEdges) {
          V adjacent = graph.adjacent(vertices.get(i), incomingEdge);
          int adjacentIndex = vertices.indexOf(adjacent);
          double prevPageRankAdjacent = previousRanks.get(adjacentIndex); //gets previous rank of
          //vertex on opposite side of edge
          double outgoingEdgesAdjacent = outgoingEdges.get(adjacentIndex); //gets outgoing edges
          //associated with vertex
          double currentRankWithoutDamp = (prevPageRankAdjacent / outgoingEdgesAdjacent)
              + currentRanks.get(i); //divides the previous page rank by the number of outgoing
          //edges with the vertex on the opposite of the current edge
          currentRanks.set(i, currentRankWithoutDamp);
        }

        //takes into account the distributed damping factor and only does this once for each
        //vertex
        double currentRank = distributedDampFactor + (currentRanks.get(i) * DAMPINGFACTOR);
        currentRanks.set(i, currentRank);
        resultRanksMap.put(vertices.get(i), currentRank);
      }
      iterationsCounter++;
    }

    //needs this to know when to stop running the algorithm
    while (!this.stopCalcPageRank(currentRanks, previousRanks)
        || iterationsCounter <= MAXITERATIONS);

    return resultRanksMap;
  }

  /**
   * Returns nothing.
   * <p>
   * Sinks are vertices with no outgoing edges and so naturally if we don't handle these vertices
   * then they will always have the highest page rank. So this inserts an edge between this vertex
   * and every other vertex in the graph and one to itself as well.
   *
   */
  private void handleSinks() {
    for (int sink : sinks) {
      for (int i = 0; i < vertices.size(); i++) {
        E e = (E) new Edge(vertices.get(sink), vertices.get(i), 0);
        graph.addEdge(e);
      }
      int numVertices = graph.numOutgoingConnections();
      outgoingEdges.set(sink, numVertices);
    }
  }

  /**
   * Returns a boolean which indicates when to stop the page rank algorithm.
   * <p>
   * Indicates whether or not page ranks can stop being calculated if the difference between
   * the current rank and previous iteration rank is below the error. In other words, if there
   * is not that much of a change.
   *
   * @return     boolean that will be true if the page rank algorithm has completed the max
   *             iterations, false otherwise
   */
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

  /**
   * Returns nothing.
   * <p>
   * Fills initial vertices list with all the vertices.
   *
   */
  public void fillVertices() {
    for (V vertex : graph.getOutgoingConnections().keySet()) {
      vertices.add(vertex);
    }
  }

  /**
   * Returns nothing.
   * <p>
   * Fills initial outgoing edges with the number of outgoing edges for each vertex.
   *
   */
  public void fillOutgoingEdges() {
    for (int i = 0; i < vertices.size(); i++) {
      int numEdges = graph.getOutgoingConnections().get(vertices.get(i)).size();
      outgoingEdges.add(i, numEdges);
      if (numEdges  == 0) {
        sinks.add(i);
      }
    }
  }

  /**
   * Returns nothing.
   * <p>
   * Fills initial current rank for all the vertices to just be 1 divided by the number of
   * vertices.
   *
   */
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
