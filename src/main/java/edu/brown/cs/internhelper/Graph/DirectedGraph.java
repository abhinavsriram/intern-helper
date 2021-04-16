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

  private final Map<V, Set<E>> outgoingConnections;
  private final Map<V, Set<E>> incomingConnections;

  /**
   * In the constructor we initialize the HashMap.
   */
  public DirectedGraph() {
    outgoingConnections = new HashMap<>();
    incomingConnections = new HashMap<>();
  }

  /**
   * addVertex adds a vertex to the directed graph.
   *
   * @param vertexToAdd vertex to be added
   */
  public void addVertex(V vertexToAdd) {
    outgoingConnections.put(vertexToAdd, new HashSet<>());
    incomingConnections.put(vertexToAdd, new HashSet<>());
  }

  /**
   * addEdge adds an edge to the directed graph.
   *
   * @param edgeToAdd edge to be added
   */
  public void addEdge(E edgeToAdd) {
    V sourceVertex = edgeToAdd.getSourceVertex();
    Set<E> edgeList = outgoingConnections.getOrDefault(sourceVertex, new HashSet<>());
    edgeList.add(edgeToAdd);
    outgoingConnections.put(sourceVertex, edgeList);

    V destVertex = edgeToAdd.getDestinationVertex();
    Set<E> destEdgeList = incomingConnections.getOrDefault(destVertex, new HashSet<>());
    destEdgeList.add(edgeToAdd);
    incomingConnections.put(destVertex, destEdgeList);

  }

  /**
   * getVertexConnections returns a set of edges associated with a vertex.
   *
   * @return vertexConnections
   */
  public Map<V, Set<E>> getOutgoingConnections() {
    return outgoingConnections;
  }

  public Map<V, Set<E>> getIncomingConnections() {
    return incomingConnections;
  }


  public int numOutgoingConnections () {
    return outgoingConnections.size();
  }

  public int numIncomingConnections () {
    return incomingConnections.size();
  }

  public V adjacent(V adj, E e) {
    if (e.getDestinationVertex() == adj) {
      return e.getSourceVertex();
    }
    else {
      return e.getDestinationVertex();
    }
  }

}
