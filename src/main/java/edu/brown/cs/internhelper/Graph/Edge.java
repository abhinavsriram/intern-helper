package edu.brown.cs.internhelper.Graph;

/**
 * The Edge class represents a generic edge that is used in DirectedGraph.
 *
 * @param <V> generic vertex
 * @param <E> generic edge
 */
public class Edge<V extends Vertex<V, E>, E extends Edge<V, E>> {

  private final V sourceVertex;
  private final V destinationVertex;
  private double edgeWeight;

  /**
   * In the constructor we initialize the sourceVertex, destinationVertex, and
   * edgeWeight.
   *
   * @param sourceVertex source vertex
   * @param destinationVertex destination vertex
   * @param edgeWeight edge weight
   */
  public Edge(V sourceVertex, V destinationVertex, double edgeWeight) {
    this.sourceVertex = sourceVertex;
    this.destinationVertex = destinationVertex;
    this.edgeWeight = edgeWeight;
  }

  /**
   * getter for the source vertex.
   *
   * @return sourceVertex
   */
  public V getSourceVertex() {
    return sourceVertex;
  }

  /**
   * getter for the destination vertex.
   *
   * @return destinationVertex
   */
  public V getDestinationVertex() {
    return destinationVertex;
  }

  /**
   * getter for the edge weight.
   *
   * @return edgeWeight
   */
  public double getEdgeWeight() {
    return edgeWeight;
  }

  /**
   * setter for the edge weight.
   *
   * @param edgeWeight edge weight
   */
  public void setEdgeWeight(double edgeWeight) {
    this.edgeWeight = edgeWeight;
  }

}
