package edu.brown.cs.internhelper;

import edu.brown.cs.internhelper.Functionality.Job;
import edu.brown.cs.internhelper.Functionality.JobEdge;
import edu.brown.cs.internhelper.Graph.Edge;
import edu.brown.cs.internhelper.Graph.Vertex;
import org.junit.Test;
import static org.junit.Assert.*;

public class EdgeTest {

  @Test
  public void verticesOfEdgeTest() {
    Vertex<Job, JobEdge> start = new Vertex<>();
    Vertex<Job, JobEdge> end = new Vertex<>();
    Edge<Job, JobEdge> newEdge = new Edge(start,end,10);
    assertEquals(newEdge.getSourceVertex(), start);
    assertEquals(newEdge.getDestinationVertex(), end);
  }

  @Test
  public void edgeWeightTest() {
    Vertex<Job, JobEdge> start = new Vertex<>();
    Vertex<Job, JobEdge> end = new Vertex<>();
    Edge<Job, JobEdge> newEdge = new Edge(start,end,10);
    assertEquals(newEdge.getEdgeWeight(), 10, 0.000000001);
    newEdge.setEdgeWeight(35);
    assertEquals(newEdge.getEdgeWeight(), 35, 0.000000001);
  }



}
