package edu.brown.cs.internhelper.Functionality;

import edu.brown.cs.internhelper.Graph.DirectedGraph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class PageRank {

  private DirectedGraph<Job, JobEdge> _graph;
  private List<Job> _vertices;
  private Map<Job, Double> _vertsToRanks;
  private static final double _dampingFactor = 0.85;
  private static final int _maxIterations = 100;
  private static final double _error = 0.01;
  private double[] _previousPageRank;
  double[] _currentPageRank;
  int[] _vertOutgoingEdges;
  private Set<Integer> _sinks;


  public Map<Job, Double> calcPageRank(DirectedGraph<Job, JobEdge> g) {
    // instance variable for graph
    _graph = g;
    // vertices iterator
    // Iterator<Job> allVertIterator = _graph.();
    // an arraylist that will store all the vertices at specific indices
    _vertices = new ArrayList<Job>();
    // an array that will keep track of each vertex's outgoing edges with matching
    // indices to the vertices array list
    _vertOutgoingEdges = new int[_graph.numVertexConnections()];
    // an array that will keep track of each vertex's previous page rank with
    // matching indices to the vertices array list
    _previousPageRank = new double[_graph.numVertexConnections()];
    // an array that will keep track of each vertex's current page rank with
    // matching indices to the vertices array list
    _currentPageRank = new double[_graph.numVertexConnections()];
    // a hashset that will keep track of all the sinks within the graph
    _sinks = new HashSet<Integer>();
    // counter to keep track of how many times pagerank should be calculated
    int numOfIterations = 0;
    // result hashmap that is returned mapping each vertex and its pagerank
    _vertsToRanks = new HashMap<Job, Double>();

    // adding all the vertices to the _vertices arraylist
    for (Job job : _graph.getVertexConnections().keySet()) {
      _vertices.add(job);
    }

    // filling in number of outgoingEdges
    for (int i = 0; i < _vertices.size(); i++) {
      _vertOutgoingEdges[i] = _graph.getVertexConnections().get(_vertices.get(i)).size();
      // if a vertex has 0 outgoing edges it is a sink and is added to the _sinks set
      if (_vertOutgoingEdges[i] == 0) {
        _sinks.add(i);
      }
    }

    // filling in each vertex initial pagerank to be 1 divided by number of vertices
    for (int i = 0; i < _vertices.size(); i++) {
      int initial_page_rank = (1) / (_graph.numVertexConnections());
      _currentPageRank[i] = initial_page_rank;
    }

    this.handleSinks();

    do {
      // set each vertex previous rank to current rank
      _previousPageRank = _currentPageRank.clone();

      // setting the entirety of current to be 0;
      for (int j = 0; j < _vertices.size(); j++) {
        _currentPageRank[j] = 0.0;

      }

      // loop through vertices and calculate rank for each vertex
      for (int v = 0; v < _vertices.size(); v++) {
        // identifies incoming edges of vertex
        Set<JobEdge> incomingEdges = _graph.getIncomingConnections().get(_vertices.get(v));
        Iterator<JobEdge> vertexIncomingEdgeList = incomingEdges.iterator();
        while (vertexIncomingEdgeList.hasNext()) {
          JobEdge incomingEdge = vertexIncomingEdgeList.next();
          // gets the vertex on the other side of the edge
          Job oppVertex = _graph.opposite(_vertices.get(v), incomingEdge);
          // divides the previous page rank of the oppositeVertex by its corresponding
          // number of outgoing edges
          double previous_divided_outgoing = (_previousPageRank[_vertices.indexOf(oppVertex)])
                  / (_vertOutgoingEdges[_vertices.indexOf(oppVertex)]);
          // sets the current page rank of the vertex to be the sum of its current page
          // rank + previous page rank divided by outgoing
          _currentPageRank[v] = _currentPageRank[v] + previous_divided_outgoing;
        }
        // only need to be calculated once
        double damp_divided_vertices = (1 - _dampingFactor) / (_vertices.size());
        _currentPageRank[v] = damp_divided_vertices + (_currentPageRank[v] * _dampingFactor);
      }
      // increments counter because it means rank has been calculated in one more
      // round
      numOfIterations++;
    }
    // exit condition
    while (this.endPageRank(_currentPageRank, _previousPageRank) == false || numOfIterations <= 100);

    // generating final hashmap containing vertex and corresponding ranks
    for (int i = 0; i < _vertices.size(); i++) {
      _vertsToRanks.put(_vertices.get(i), _currentPageRank[i]);
    }

    return _vertsToRanks;
  }

  private void handleSinks() {
    // iterating through the sink set
    for (int sink : _sinks) {
      // iterating through all the vertices
      for (int i = 0; i < _vertices.size(); i++) {
        // inserting an edge between each sink and every other vertex in the graph
        // including one to itself
        JobEdge e = new JobEdge(_vertices.get(sink), _vertices.get(i), 0);
        _graph.addEdge(e);
      }
      // updating the outgoing edges of the sink to just be the number of vertices
      _vertOutgoingEdges[sink] = _graph.numVertexConnections();
    }

  }


  private boolean endPageRank(double[] currentIteration, double[] previousIteration) {
    int diffRanksCounter = 0;
    for (int i = 0; i < currentIteration.length; i++) {
      if (currentIteration[i] - previousIteration[i] <= _error) {
        diffRanksCounter++;
      }
    }
    // if the counter equals the length that means that every element met the
    // condition
    if (diffRanksCounter == currentIteration.length) {
      return true;
    } else {
      return false;
    }
  }


}
