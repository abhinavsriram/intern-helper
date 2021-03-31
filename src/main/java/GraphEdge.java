public class GraphEdge implements Edge<GraphVertex> {

  private GraphVertex start;
  private GraphVertex end;
  private double weight;

  private final String id;

  public GraphEdge(String edgeID) {
    this.id = edgeID;
    this.start = null;
    this.end = null;
    this.weight = 0.0;
  }

  public GraphEdge(String edgeID, GraphVertex startVertex, GraphVertex endVertex, double edgeWeight) {
    this.id = edgeID;
    this.start = startVertex;
    this.end = endVertex;
    this.weight = edgeWeight;
  }

  public String getID() {
    return this.id;
  }

  @Override
  public void setStartVertex(GraphVertex startVertex) {
    this.start = startVertex;
  }

  @Override
  public GraphVertex getStartVertex() {
    return this.start;
  }

  @Override
  public void setEndVertex(GraphVertex endVertex) {
    this.end = endVertex;
  }

  @Override
  public GraphVertex getEndVertex() {
    return this.end;
  }

  @Override
  public void setWeight(double edgeWeight) {
    this.weight = edgeWeight;
  }

  @Override
  public double getWeight() {
    return this.weight;
  }

  @Override
  public boolean equals(Edge<GraphVertex> edge) {
    if (edge.getStartVertex().equals(this.getStartVertex())) {
      if (edge.getEndVertex().equals(this.getEndVertex())) {
        return edge.getWeight() == this.getWeight();
      }
    }
    return false;
  }
}
