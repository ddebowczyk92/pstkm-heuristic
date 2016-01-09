package pl.pstkm.graph.algorithm;

import pl.pstkm.graph.Graph;
import pl.pstkm.graph.Path;
import pl.pstkm.graph.abstraction.BaseGraph;
import pl.pstkm.graph.abstraction.BaseVertex;

import java.util.*;

/**
 * Created by DominikD on 2015-12-13.
 */
public class DijkstraShortestPath {
    private final BaseGraph graph;

    private Set<BaseVertex> determinedVertexSet = new HashSet<BaseVertex>();
    private PriorityQueue<BaseVertex> vertexCandidateQueue = new PriorityQueue<BaseVertex>();
    private Map<BaseVertex, Double> startVertexDistanceIndex = new HashMap<BaseVertex, Double>();
    private Map<BaseVertex, BaseVertex> predecessorIndex = new HashMap<BaseVertex, BaseVertex>();

    public DijkstraShortestPath(final BaseGraph graph) {
        this.graph = graph;
    }

    public void clear() {
        determinedVertexSet.clear();
        vertexCandidateQueue.clear();
        startVertexDistanceIndex.clear();
        predecessorIndex.clear();
    }

    public Map<BaseVertex, Double> getStartVertexDistanceIndex() {
        return startVertexDistanceIndex;
    }

    public Map<BaseVertex, BaseVertex> getPredecessorIndex() {
        return predecessorIndex;
    }

    public void getShortestPathTree(BaseVertex root) {
        determineShortestPaths(root, null, true);
    }

    public void getShortestPathFlower(BaseVertex root) {
        determineShortestPaths(null, root, false);
    }


    protected void determineShortestPaths(BaseVertex sourceVertex, BaseVertex sinkVertex, boolean isSource2sink) {
        // 0. clean up variables
        clear();

        // 1. initialize members
        BaseVertex endVertex = isSource2sink ? sinkVertex : sourceVertex;
        BaseVertex startVertex = isSource2sink ? sourceVertex : sinkVertex;
        startVertexDistanceIndex.put(startVertex, 0d);
        startVertex.setWeight(0d);
        vertexCandidateQueue.add(startVertex);

        // 2. start searching for the shortest path
        while (!vertexCandidateQueue.isEmpty()) {
            BaseVertex curCandidate = vertexCandidateQueue.poll();

            if (curCandidate.equals(endVertex)) {
                break;
            }

            determinedVertexSet.add(curCandidate);

            updateVertex(curCandidate, isSource2sink);
        }
    }

    private void updateVertex(BaseVertex vertex, boolean isSource2sink) {
        // 1. get the neighboring vertices
        Set<BaseVertex> neighborVertexList = isSource2sink ? graph.getAdjacentVertices(vertex) : graph.getPrecedentVertices(vertex);

        // 2. update the distance passing on current vertex
        for (BaseVertex curAdjacentVertex : neighborVertexList) {
            // 2.1 skip if visited before
            if (determinedVertexSet.contains(curAdjacentVertex)) {
                continue;
            }

            // 2.2 calculate the new distance
            double distance = startVertexDistanceIndex.containsKey(vertex) ? startVertexDistanceIndex.get(vertex) : Graph.DISCONNECTED;

            distance += isSource2sink ? graph.getEdgeWeight(vertex, curAdjacentVertex) : graph.getEdgeWeight(curAdjacentVertex, vertex);

            // 2.3 update the distance if necessary
            if (!startVertexDistanceIndex.containsKey(curAdjacentVertex) || startVertexDistanceIndex.get(curAdjacentVertex) > distance) {
                startVertexDistanceIndex.put(curAdjacentVertex, distance);

                predecessorIndex.put(curAdjacentVertex, vertex);

                curAdjacentVertex.setWeight(distance);
                vertexCandidateQueue.add(curAdjacentVertex);
            }
        }
    }

    public Path getShortestPath(BaseVertex sourceVertex, BaseVertex sinkVertex) {
        determineShortestPaths(sourceVertex, sinkVertex, true);
        //
        List<BaseVertex> vertexList = new Vector<BaseVertex>();
        double weight = startVertexDistanceIndex.containsKey(sinkVertex) ? startVertexDistanceIndex.get(sinkVertex) : Graph.DISCONNECTED;
        if (weight != Graph.DISCONNECTED) {
            BaseVertex curVertex = sinkVertex;
            do {
                vertexList.add(curVertex);
                curVertex = predecessorIndex.get(curVertex);
            } while (curVertex != null && curVertex != sourceVertex);
            vertexList.add(sourceVertex);
            Collections.reverse(vertexList);
        }
        return new Path(vertexList, weight);
    }

    public Path updateCostForward(BaseVertex vertex) {
        double cost = Graph.DISCONNECTED;

        // 1. get the set of successors of the input vertex
        Set<BaseVertex> adjVertexSet = graph.getAdjacentVertices(vertex);

        // 2. make sure the input vertex exists in the index
        if (!startVertexDistanceIndex.containsKey(vertex)) {
            startVertexDistanceIndex.put(vertex, Graph.DISCONNECTED);
        }

        // 3. update the distance from the root to the input vertex if necessary
        for (BaseVertex curVertex : adjVertexSet) {
            // 3.1 get the distance from the root to one successor of the input vertex
            double distance = startVertexDistanceIndex.containsKey(curVertex) ? startVertexDistanceIndex.get(curVertex) : Graph.DISCONNECTED;

            // 3.2 calculate the distance from the root to the input vertex
            distance += graph.getEdgeWeight(vertex, curVertex);
            //distance += ((VariableGraph)graph).get_edge_weight_of_graph(vertex, curVertex);

            // 3.3 update the distance if necessary
            double costOfVertex = startVertexDistanceIndex.get(vertex);
            if (costOfVertex > distance) {
                startVertexDistanceIndex.put(vertex, distance);
                predecessorIndex.put(vertex, curVertex);
                cost = distance;
            }
        }

        // 4. create the subPath if exists
        Path subPath = null;
        if (cost < Graph.DISCONNECTED) {
            subPath = new Path();
            subPath.setWeight(cost);
            List<BaseVertex> vertexList = subPath.getVertexList();
            vertexList.add(vertex);

            BaseVertex selVertex = predecessorIndex.get(vertex);
            while (selVertex != null) {
                vertexList.add(selVertex);
                selVertex = predecessorIndex.get(selVertex);
            }
        }

        return subPath;
    }

    public void correctCostBackward(BaseVertex vertex) {
        // 1. initialize the list of vertex to be updated
        List<BaseVertex> vertexList = new LinkedList<BaseVertex>();
        vertexList.add(vertex);

        // 2. update the cost of relevant precedents of the input vertex
        while (!vertexList.isEmpty()) {
            BaseVertex curVertex = vertexList.remove(0);
            double costOfCurVertex = startVertexDistanceIndex.get(curVertex);

            Set<BaseVertex> preVertexSet = graph.getPrecedentVertices(curVertex);
            for (BaseVertex preVertex : preVertexSet) {
                double costOfPreVertex = startVertexDistanceIndex.containsKey(preVertex) ? startVertexDistanceIndex.get(preVertex) : Graph.DISCONNECTED;

                double freshCost = costOfCurVertex + graph.getEdgeWeight(preVertex, curVertex);
                if (costOfPreVertex > freshCost) {
                    startVertexDistanceIndex.put(preVertex, freshCost);
                    predecessorIndex.put(preVertex, curVertex);
                    vertexList.add(preVertex);
                }
            }
        }
    }
}
