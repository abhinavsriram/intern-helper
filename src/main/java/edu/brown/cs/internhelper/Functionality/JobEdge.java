package edu.brown.cs.internhelper.Functionality;

import edu.brown.cs.internhelper.Graph.Edge;

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
