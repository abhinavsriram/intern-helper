package edu.brown.cs.internhelper.Graph;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * This class represents a Directed Graph that includes vertices and edges
 * that connect those vertices.
 * @param <V> extends the Vertex class, representing the vertices of the
 *           Directed Graph.
 * @param <E> extends the Edge class, representing the edges of the Directed
 *           Graph.
 */
public class DirectedGraph<V extends Vertex<V, E>, E extends Edge<V, E>> {

  private final Map<V, Set<E>> outgoingConnections;
  private final Map<V, Set<E>> incomingConnections;


  /**
   * This is the constructor for the DirectedGraph class. It initializes the
   * outgoingConnections and incomingConnections as new HashMaps that map a
   * Vertex to a set of Edges..
   */
  public DirectedGraph() {
    outgoingConnections = new HashMap<>();
    incomingConnections = new HashMap<>();
  }

  /**
   * This method adds a Vertex to the DirectedGraph by updating the outgoingConnections
   * and the incomingConnections HashMaps.
   * @param vertexToAdd is the new Vertex to be added to the DirectedGraph.
   */
  public void addVertex(V vertexToAdd) {
    outgoingConnections.put(vertexToAdd, new HashSet<>());
    incomingConnections.put(vertexToAdd, new HashSet<>());
  }

  /**
   * This method adds an Edge to the DirectedGraph by updating the outgoingConnections
   * and the incomingConnections HashMaps through the edgeList.
   * @param edgeToAdd is the new Edge to be added to the DirectedGraph.
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
   * This method is a getter for the outgoingConnections instance variable.
   * @return the outgoingConnections instance variable.
   */
  public Map<V, Set<E>> getOutgoingConnections() {
    return outgoingConnections;
  }

  /**
   * This method is a getter for the incomingConnections instance variable.
   * @return the incomingConnections instance variable.
   */
  public Map<V, Set<E>> getIncomingConnections() {
    return incomingConnections;
  }

  /**
   * This method returns the number of vertices with an outgoing edge.
   * @return the size of the outgoingConnections instance variable.
   */
  public int numOutgoingConnections() {
    return outgoingConnections.size();
  }

  /**
   * This method returns the number of vertices with an incoming edge.
   * @return the size of the incomingConnections instance variable.
   */
  public int numIncomingConnections() {
    return incomingConnections.size();
  }

  /**
   * This method returns the Vertex at the opposite end of an Edge,
   * given an Edge and one of the Vertices on that Edge.
   * @param adj is one of the Vertices on the Edge that is passed in.
   * @param e is the Edge that is passed in.
   * @return the Vertex at the opposite end of the Edge that was passed in.
   */
  public V adjacent(V adj, E e) {
    if (e.getDestinationVertex() == adj) {
      return e.getSourceVertex();
    } else {
      return e.getDestinationVertex();
    }
  }

}
