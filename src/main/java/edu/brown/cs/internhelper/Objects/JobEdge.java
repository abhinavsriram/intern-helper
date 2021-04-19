package edu.brown.cs.internhelper.Objects;

import edu.brown.cs.internhelper.Graph.Edge;

/**
 * This class represents the Edge between Jobs in a directed graph. It is
 * used in the JobGraphBuilder class.
 */
public class JobEdge extends Edge<Job, JobEdge> {

  /**
   * In the constructor we initialize the sourceVertex, destinationVertex, and
   * edgeWeight.
   *
   * @param sourceVertex      source vertex
   * @param destinationVertex destination vertex
   * @param edgeWeight        edge weight
   */
  public JobEdge(Job sourceVertex, Job destinationVertex, double edgeWeight) {
    super(sourceVertex, destinationVertex, edgeWeight);
  }

}
