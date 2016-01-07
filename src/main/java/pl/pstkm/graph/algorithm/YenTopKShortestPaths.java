package pl.pstkm.graph.algorithm;

import pl.pstkm.graph.PSTKMGraph;
import pl.pstkm.graph.PSTKMVariableGraph;
import pl.pstkm.graph.Path;
import pl.pstkm.graph.abstraction.BaseGraph;
import pl.pstkm.graph.abstraction.BaseVertex;
import pl.pstkm.graph.utils.Pair;
import pl.pstkm.graph.utils.QYPriorityQueue;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

/**
 * Created by DominikD on 2015-12-13.
 */
public class YenTopKShortestPaths {

    private PSTKMVariableGraph graph = null;

    private List<Path> resultList = new Vector<Path>();
    private Map<Path, BaseVertex> pathDerivationVertexIndex = new HashMap<Path, BaseVertex>();
    private QYPriorityQueue<Path> pathCandidates = new QYPriorityQueue<Path>();

    private BaseVertex sourceVertex = null;
    private BaseVertex targetVertex = null;

    private int generatedPathNum = 0;

    public YenTopKShortestPaths(BaseGraph graph) {
        this(graph, null, null);
    }

    public YenTopKShortestPaths(BaseGraph graph, BaseVertex sourceVertex, BaseVertex targetVertex) {
        if (graph == null) {
            throw new IllegalArgumentException("A NULL graph object occurs!");
        }
        this.graph = new PSTKMVariableGraph((PSTKMGraph) graph);
        this.sourceVertex = sourceVertex;
        this.targetVertex = targetVertex;
        init();
    }

    /**
     * Initiate members in the class.
     */
    private void init() {
        clear();
        // get the shortest path by default if both source and target exist
        if (sourceVertex != null && targetVertex != null) {
            Path shortestPath = getShortestPath(sourceVertex, targetVertex);
            if (!shortestPath.getVertexList().isEmpty()) {
                pathCandidates.add(shortestPath);
                pathDerivationVertexIndex.put(shortestPath, sourceVertex);
            }
        }
    }

    public void clear() {
        pathCandidates = new QYPriorityQueue<Path>();
        pathDerivationVertexIndex.clear();
        resultList.clear();
        generatedPathNum = 0;
    }

    public Path getShortestPath(BaseVertex sourceVertex, BaseVertex targetVertex) {
        DijkstraShortestPath dijkstraAlg = new DijkstraShortestPath(graph);
        return dijkstraAlg.getShortestPath(sourceVertex, targetVertex);
    }

    public boolean hasNext() {
        return !pathCandidates.isEmpty();
    }

    public Path next() {
        //3.1 prepare for removing vertices and arcs
        Path curPath = pathCandidates.poll();
        resultList.add(curPath);

        BaseVertex curDerivation = pathDerivationVertexIndex.get(curPath);
        int curPathHash = curPath.getVertexList().subList(0, curPath.getVertexList().indexOf(curDerivation)).hashCode();

        int count = resultList.size();

        //3.2 remove the vertices and arcs in the graph
        for (int i = 0; i < count - 1; ++i) {
            Path curResultPath = resultList.get(i);

            int curDevVertexId = curResultPath.getVertexList().indexOf(curDerivation);

            if (curDevVertexId < 0) {
                continue;
            }

            // Note that the following condition makes sure all candidates should be considered.
            /// The algorithm in the paper is not correct for removing some candidates by mistake.
            int pathHash = curResultPath.getVertexList().subList(0, curDevVertexId).hashCode();
            if (pathHash != curPathHash) {
                continue;
            }

            BaseVertex curSuccVertex = curResultPath.getVertexList().get(curDevVertexId + 1);

            graph.deleteEdge(new Pair<>(curDerivation.getId(), curSuccVertex.getId()));
        }

        int pathLength = curPath.getVertexList().size();
        List<BaseVertex> curPathVertexList = curPath.getVertexList();
        for (int i = 0; i < pathLength - 1; ++i) {
            graph.deleteVertex(curPathVertexList.get(i).getId());
            graph.deleteEdge(new Pair<>(curPathVertexList.get(i).getId(), curPathVertexList.get(i + 1).getId()));
        }

        //3.3 calculate the shortest tree rooted at target vertex in the graph
        DijkstraShortestPath reverseTree = new DijkstraShortestPath(graph);
        reverseTree.getShortestPathFlower(targetVertex);

        //3.4 recover the deleted vertices and update the cost and identify the new candidate results
        boolean isDone = false;
        for (int i = pathLength - 2; i >= 0 && !isDone; --i) {
            //3.4.1 get the vertex to be recovered
            BaseVertex curRecoverVertex = curPathVertexList.get(i);
            graph.recoverDeletedVertex(curRecoverVertex.getId());

            //3.4.2 check if we should stop continuing in the next iteration
            if (curRecoverVertex.getId().equals(curDerivation.getId())) {
                isDone = true;
            }

            //3.4.3 calculate cost using forward star form
            Path subPath = reverseTree.updateCostForward(curRecoverVertex);

            //3.4.4 get one candidate result if possible
            if (subPath != null) {
                ++generatedPathNum;

                //3.4.4.1 get the prefix from the concerned path
                double cost = 0;
                List<BaseVertex> prePathList = new Vector<BaseVertex>();
                reverseTree.correctCostBackward(curRecoverVertex);

                for (int j = 0; j < pathLength; ++j) {
                    BaseVertex curVertex = curPathVertexList.get(j);
                    if (curVertex.getId().equals(curRecoverVertex.getId())) {
                        j = pathLength;
                    } else {
                        cost += graph.getEdgeWeightOfGraph(curPathVertexList.get(j), curPathVertexList.get(j + 1));
                        prePathList.add(curVertex);
                    }
                }
                prePathList.addAll(subPath.getVertexList());

                //3.4.4.2 compose a candidate
                subPath.setWeight(cost + subPath.getWeight());
                subPath.getVertexList().clear();
                subPath.getVertexList().addAll(prePathList);

                //3.4.4.3 put it in the candidate pool if new
                if (!pathDerivationVertexIndex.containsKey(subPath)) {
                    pathCandidates.add(subPath);
                    pathDerivationVertexIndex.put(subPath, curRecoverVertex);
                }
            }

            //3.4.5 restore the edge
            BaseVertex succVertex = curPathVertexList.get(i + 1);
            graph.recoverDeletedEdge(new Pair<>(curRecoverVertex.getId(), succVertex.getId()));

            //3.4.6 update cost if necessary
            double cost1 = graph.getEdgeWeight(curRecoverVertex, succVertex) + reverseTree.getStartVertexDistanceIndex().get(succVertex);

            if (reverseTree.getStartVertexDistanceIndex().get(curRecoverVertex) > cost1) {
                reverseTree.getStartVertexDistanceIndex().put(curRecoverVertex, cost1);
                reverseTree.getPredecessorIndex().put(curRecoverVertex, succVertex);
                reverseTree.correctCostBackward(curRecoverVertex);
            }
        }

        //3.5 restore everything
        graph.recoverDeletedEdges();
        graph.recoverDeletedVertices();

        return curPath;
    }

    public List<Path> getShortestPaths(BaseVertex source, BaseVertex target, int k) {
        sourceVertex = source;
        targetVertex = target;

        init();
        int count = 0;
        while (hasNext() && count < k) {
            next();
            ++count;
        }

        return resultList;
    }

    public List<Path> getResultList() {
        return resultList;
    }

    public int getCadidateSize() {
        return pathDerivationVertexIndex.size();
    }

    public int getGeneratedPathSize() {
        return generatedPathNum;
    }
}
