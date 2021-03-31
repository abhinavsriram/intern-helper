public class GraphEdge implements Edge<GraphVertex> {

  private GraphVertex start;
  private GraphVertex end;
  private double weight;

  public GraphEdge() {}

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
