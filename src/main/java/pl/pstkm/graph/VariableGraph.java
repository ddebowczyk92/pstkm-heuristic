package pl.pstkm.graph;

import pl.pstkm.graph.abstraction.BaseVertex;
import pl.pstkm.graph.utils.Pair;

import java.util.*;

/**
 * Created by DominikD on 2015-12-13.
 */
public class VariableGraph extends Graph {
    private Set<String> remVertexIdSet = new HashSet<>();
    private Set<Pair<String, String>> remEdgeSet = new HashSet<>();

    public VariableGraph() {
    }

    public VariableGraph(String dataFileName) {
        super(dataFileName);
    }

    public VariableGraph(Graph graph) {
        super(graph);
    }

    public void setDelVertexIdList(Collection<String> remVertexList) {
        this.remVertexIdSet.addAll(remVertexList);
    }

    public void setDelEdgeHashcodeSet(Collection<Pair<String, String>> remEdgeCollection) {
        remEdgeSet.addAll(remEdgeCollection);
    }

    public void deleteEdge(Pair<String, String> edge) {
        remEdgeSet.add(edge);
    }

    public void deleteVertex(String vertexId) {
        remVertexIdSet.add(vertexId);
    }

    public void recoverDeletedEdges() {
        remEdgeSet.clear();
    }

    public void recoverDeletedEdge(Pair<String, String> edge) {
        remEdgeSet.remove(edge);
    }

    public void recoverDeletedVertices() {
        remVertexIdSet.clear();
    }

    public void recoverDeletedVertex(String vertexId) {
        remVertexIdSet.remove(vertexId);
    }

    public double getEdgeWeight(BaseVertex source, BaseVertex sink) {
        String sourceId = source.getId();
        String sinkId = sink.getId();

        if (remVertexIdSet.contains(sourceId) || remVertexIdSet.contains(sinkId) ||
                    remEdgeSet.contains(new Pair<String, String>(sourceId, sinkId))) {
            return Graph.DISCONNECTED;
        }
        return super.getEdgeWeight(source, sink);
    }

    public double getEdgeWeightOfGraph(BaseVertex source, BaseVertex sink) {
        return super.getEdgeWeight(source, sink);
    }

    public Set<BaseVertex> getAdjacentVertices(BaseVertex vertex) {
        Set<BaseVertex> retSet = new HashSet<BaseVertex>();
        String startingVertexId = vertex.getId();
        if (!remVertexIdSet.contains(startingVertexId)) {
            Set<BaseVertex> adjVertexSet = super.getAdjacentVertices(vertex);
            for (BaseVertex curVertex : adjVertexSet) {
                String endingVertexId = curVertex.getId();
                if (remVertexIdSet.contains(endingVertexId) || remEdgeSet.contains(new Pair<>(startingVertexId, endingVertexId))) {
                    continue;
                }
                //
                retSet.add(curVertex);
            }
        }
        return retSet;
    }

    public Set<BaseVertex> getPrecedentVertices(BaseVertex vertex) {
        Set<BaseVertex> retSet = new HashSet<BaseVertex>();
        if (!remVertexIdSet.contains(vertex.getId())) {
            String endingVertexId = vertex.getId();
            Set<BaseVertex> preVertexSet = super.getPrecedentVertices(vertex);
            for (BaseVertex curVertex : preVertexSet) {
                String startingVertexId = curVertex.getId();
                if (remVertexIdSet.contains(startingVertexId) || remEdgeSet.contains(new Pair<>(startingVertexId, endingVertexId))) {
                    continue;
                }
                //
                retSet.add(curVertex);
            }
        }
        return retSet;
    }

    public List<BaseVertex> getVertexList() {
        List<BaseVertex> retList = new Vector<BaseVertex>();
        for (BaseVertex curVertex : super.getVertexList()) {
            if (remVertexIdSet.contains(curVertex.getId())) {
                continue;
            }
            retList.add(curVertex);
        }
        return retList;
    }

    public BaseVertex getVertex(String id) {
        if (remVertexIdSet.contains(id)) {
            return null;
        } else {
            return super.getVertex(id);
        }
    }
}
