package edu.brown.cs.internhelper;

import edu.brown.cs.internhelper.Functionality.Job;
import edu.brown.cs.internhelper.Functionality.JobEdge;
import edu.brown.cs.internhelper.Graph.DirectedGraph;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class GraphTest {

  @Test
  public void addVertexTest() {
    DirectedGraph<Job, JobEdge> graph = new DirectedGraph<>();
    Job newJob = new Job();
    graph.addVertex(newJob);
    assertEquals(graph.numIncomingConnections(), 1);
    assertEquals(graph.numOutgoingConnections(), 1);
    Job newerJob = new Job();
    graph.addVertex(newerJob);
    assertEquals(graph.numIncomingConnections(), 2);
    assertEquals(graph.numOutgoingConnections(), 2);
  }

  @Test
  public void addEdgeTest() {
    DirectedGraph<Job, JobEdge> graph = new DirectedGraph<>();
    Job start = new Job();
    Job endOne = new Job();
    JobEdge edgeOne = new JobEdge(start,endOne,10);
    graph.addEdge(edgeOne);
    assertEquals(graph.numIncomingConnections(), 1);
    assertEquals(graph.numOutgoingConnections(), 1);

    Job endTwo = new Job();
    JobEdge edgeTwo = new JobEdge(start,endTwo,5);
    graph.addEdge(edgeTwo);
    assertEquals(graph.numIncomingConnections(), 2);
    assertEquals(graph.getIncomingConnections().get(endOne).size(), 1);
    assertEquals(graph.getIncomingConnections().get(endTwo).size(), 1);
    assertEquals(graph.numOutgoingConnections(), 1);
    assertEquals(graph.getOutgoingConnections().get(start).size(), 2);


    JobEdge edgeThree = new JobEdge(endOne,endTwo,7);
    graph.addEdge(edgeThree);
    assertEquals(graph.numIncomingConnections(), 2);
    assertEquals(graph.getIncomingConnections().get(endOne).size(), 1);
    assertEquals(graph.getIncomingConnections().get(endTwo).size(), 2);
    assertEquals(graph.numOutgoingConnections(), 2);
    assertEquals(graph.getOutgoingConnections().get(start).size(), 2);
    assertEquals(graph.getOutgoingConnections().get(endOne).size(), 1);
  }

  @Test
  public void adjacentTest() {
    DirectedGraph<Job, JobEdge> graph = new DirectedGraph<>();
    Job start = new Job();
    Job end = new Job();
    JobEdge newEdge = new JobEdge(start,end,10);
    assertEquals(graph.adjacent(start,newEdge), end);
    assertEquals(graph.adjacent(end,newEdge), start);
  }

}
