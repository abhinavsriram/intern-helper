import java.util.HashSet;
import java.util.Set;

public class GraphVertex implements Vertex<GraphEdge> {

  private Set<GraphEdge> outgoingEdges = new HashSet<>();
  private Set<GraphEdge> incomingEdges = new HashSet<>();

  private final String id;

  public GraphVertex(String vertexID) {
    this.id = vertexID;
  }

  public GraphVertex(String vertexID, Set<GraphEdge> outgoingEdges, Set<GraphEdge> incomingEdges) {
    this.id = vertexID;
    this.setOutgoingEdges(outgoingEdges);
    this.setIncomingEdges(incomingEdges);
  }

  public String getID() {
    return this.id;
  }

  @Override
  public void addOutgoingEdge(GraphEdge edge) {
    this.outgoingEdges.add(edge);
  }

  @Override
  public void setOutgoingEdges(Set<GraphEdge> edgeSet) {
    this.outgoingEdges = edgeSet;
  }

  @Override
  public Set<GraphEdge> getOutgoingEdges() {
    return this.outgoingEdges;
  }

  @Override
  public void addIncomingEdge(GraphEdge edge) {
    this.incomingEdges.add(edge);
  }

  @Override
  public void setIncomingEdges(Set<GraphEdge> edgeSet) {
    this.incomingEdges = edgeSet;
  }

  @Override
  public Set<GraphEdge> getIncomingEdges() {
    return this.incomingEdges;
  }

  @Override
  public boolean equals(Vertex<GraphEdge> vertex) {
    if (vertex.getIncomingEdges().equals(this.getIncomingEdges())) {
      return vertex.getOutgoingEdges().equals(this.getOutgoingEdges());
    }
    return false;
  }
}
