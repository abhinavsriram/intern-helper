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
  private final Map<V, Set<E>> incomingConnections;

  /**
   * In the constructor we initialize the HashMap.
   */
  public DirectedGraph() {
    vertexConnections = new HashMap<>();
    incomingConnections = new HashMap<>();
  }

  /**
   * addVertex adds a vertex to the directed graph.
   *
   * @param vertexToAdd vertex to be added
   */
  public void addVertex(V vertexToAdd) {
    vertexConnections.put(vertexToAdd, new HashSet<>());
    incomingConnections.put(vertexToAdd, new HashSet<>());
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
  public Map<V, Set<E>> getVertexConnections() {
    return vertexConnections;
  }

  public Map<V, Set<E>> getIncomingConnections() {
    return incomingConnections;
  }


  public int numVertexConnections () {
    return vertexConnections.size();
  }

  public int numIncomingConnections () {
    return incomingConnections.size();
  }

  public V opposite(V opp, E e) {
    if (e.getDestinationVertex() == opp) {
      return e.getSourceVertex();
    }
    else {
      return e.getDestinationVertex();
    }
  }

}
