import java.util.Set;

public interface Vertex<E extends Edge> {

  void addOutgoingEdge(E edge);



  void setOutgoingEdges(Set<E> edgeSet);

  Set<E> getOutgoingEdges();

  void addIncomingEdge(E edge);

  void setIncomingEdges(Set<E> edgeSet);

  Set<E> getIncomingEdges();

  boolean equals(Vertex<E> vertex);
}
