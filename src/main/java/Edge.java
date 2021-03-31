public interface Edge<V extends Vertex> {

  void setStartVertex(V startVertex);

  V getStartVertex();

  void setEndVertex(V endVertex);

  V getEndVertex();

  void setWeight(double weight);

  double getWeight();

  boolean equals(Edge<V> edge);
}
