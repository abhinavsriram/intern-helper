package edu.brown.cs.internhelper.Graph;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


public class DirectedGraph<V extends Vertex<V, E>, E extends Edge<V, E>> {

  private final Map<V, Set<E>> outgoingConnections;
  private final Map<V, Set<E>> incomingConnections;


  public DirectedGraph() {
    outgoingConnections = new HashMap<>();
    incomingConnections = new HashMap<>();
  }


  public void addVertex(V vertexToAdd) {
    outgoingConnections.put(vertexToAdd, new HashSet<>());
    incomingConnections.put(vertexToAdd, new HashSet<>());
  }


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


  public Map<V, Set<E>> getOutgoingConnections() {
    return outgoingConnections;
  }

  public Map<V, Set<E>> getIncomingConnections() {
    return incomingConnections;
  }


  public int numOutgoingConnections() {
    return outgoingConnections.size();
  }

  public int numIncomingConnections() {
    return incomingConnections.size();
  }

  public V adjacent(V adj, E e) {
    if (e.getDestinationVertex() == adj) {
      return e.getSourceVertex();
    } else {
      return e.getDestinationVertex();
    }
  }

}
