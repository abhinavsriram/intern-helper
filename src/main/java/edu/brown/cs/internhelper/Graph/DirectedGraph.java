package edu.brown.cs.internhelper.Graph;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


/**
 * The DirectedGraph class represents a generic directed graph
 * using a HashMap as the underlying data structure.
 *
 * @param <V> generic vertex
 * @param <E> generic edge
 */
public class DirectedGraph<V extends Vertex<V, E>, E extends Edge<V, E>> {

  private final Map<V, Set<E>> vertexConnections;

  /**
   * In the constructor we initialize the HashMap.
   */
  public DirectedGraph() {
    vertexConnections = new HashMap<>();
  }

  /**
   * addVertex adds a vertex to the directed graph.
   *
   * @param vertexToAdd vertex to be added
   */
  public void addVertex(V vertexToAdd) {
    vertexConnections.put(vertexToAdd, new HashSet<>());
  }

  /**
   * addEdge adds an edge to the directed graph.
   *
   * @param edgeToAdd edge to be added
   */
  public void addEdge(E edgeToAdd) {
    V sourceVertex = edgeToAdd.getSourceVertex();
    Set<E> edgeList = vertexConnections.getOrDefault(sourceVertex, new HashSet<>());
    edgeList.add(edgeToAdd);
    vertexConnections.put(sourceVertex, edgeList);
  }

  /**
   * getVertexConnections returns a set of edges associated with a vertex.
   *
   * @return vertexConnections
   */
  public Map<V, Set<E>> getVertexConnections() {
    return vertexConnections;
  }

}
