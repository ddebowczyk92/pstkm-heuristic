package pl.pstkm.graph;


import pl.pstkm.graph.abstraction.BaseGraph;
import pl.pstkm.graph.abstraction.BaseVertex;
import pl.pstkm.graph.utils.Pair;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * Created by DominikD on 2015-12-12.
 */
public class Graph implements BaseGraph {

    public static final double DISCONNECTED = Double.MAX_VALUE;

    protected Map<String, Set<BaseVertex>> fanoutVerticesIndex = new HashMap<String, Set<BaseVertex>>();

    protected Map<String, Set<BaseVertex>> faninVerticesIndex = new HashMap<String, Set<BaseVertex>>();

    protected Map<Pair<String, String>, Double> vertexPairWeightIndex = new HashMap<Pair<String, String>, Double>();

    protected Map<String, BaseVertex> idVertexIndex = new HashMap<String, BaseVertex>();

    protected List<BaseVertex> vertexList = new Vector<BaseVertex>();

    protected int vertexNum = 0;

    protected int edgeNum = 0;

    public Graph(final String dataFileName) {
        importFromFile(dataFileName);
    }

    public Graph(final Graph graph) {
        vertexNum = graph.vertexNum;
        edgeNum = graph.edgeNum;
        vertexList.addAll(graph.vertexList);
        idVertexIndex.putAll(graph.idVertexIndex);
        faninVerticesIndex.putAll(graph.faninVerticesIndex);
        fanoutVerticesIndex.putAll(graph.fanoutVerticesIndex);
        vertexPairWeightIndex.putAll(graph.vertexPairWeightIndex);
    }

    public Graph() {
    }

    public void clear() {
        //PSTKMVertex.reset();
        vertexNum = 0;
        edgeNum = 0;
        vertexList.clear();
        idVertexIndex.clear();
        faninVerticesIndex.clear();
        fanoutVerticesIndex.clear();
        vertexPairWeightIndex.clear();
    }


    @Override
    public List<BaseVertex> getVertexList() {
        return vertexList;
    }

    @Override
    public double getEdgeWeight(BaseVertex source, BaseVertex sink) {
        return vertexPairWeightIndex.containsKey(new Pair<>(source.getId(), sink.getId())) ? vertexPairWeightIndex.get(new Pair<>(source.getId(), sink.getId())) : DISCONNECTED;
    }

    @Override
    public Set<BaseVertex> getAdjacentVertices(BaseVertex vertex) {
        return fanoutVerticesIndex.containsKey(vertex.getId()) ? fanoutVerticesIndex.get(vertex.getId()) : new HashSet<BaseVertex>();
    }

    @Override
    public Set<BaseVertex> getPrecedentVertices(BaseVertex vertex) {
        return faninVerticesIndex.containsKey(vertex.getId()) ? faninVerticesIndex.get(vertex.getId()) : new HashSet<BaseVertex>();
    }


    public void importFromFile(String dataFileName) {
        // 0. Clear the variables
        clear();

        try {
            // 1. read the file and put the content in the buffer
            FileReader input = new FileReader(dataFileName);
            BufferedReader bufRead = new BufferedReader(input);

            boolean isFirstLine = true;
            String line;    // String that holds current file line

            // 2. Read first line
            line = bufRead.readLine();
            while (line != null) {
                // 2.1 skip the empty line
                if (line.trim().equals("")) {
                    line = bufRead.readLine();
                    continue;
                }

                // 2.2 generate nodes and edges for the graph
                if (isFirstLine) {
                    //2.2.1 obtain the number of nodes in the graph
                    isFirstLine = false;
                    String[] vertexArr = line.trim().split("\\s");

                    for (int i = 0; i < vertexArr.length; i++) {
                        BaseVertex vertex = new Vertex(vertexArr[i]);
                        vertexList.add(vertex);
                        idVertexIndex.put(vertex.getId(), vertex);
                        vertexNum += 1;
                    }
                } else {
                    //2.2.2 find a new edge and put it in the graph
                    String[] strList = line.trim().split("\\s");

                    String startVertexId = strList[0];
                    String endVertexId = strList[1];
                    double weight = Double.parseDouble(strList[2]);
                    addEdge(startVertexId, endVertexId, weight);
                }
                //
                line = bufRead.readLine();
            }
            bufRead.close();

        } catch (IOException e) {
            // If another exception is generated, print a stack trace
            e.printStackTrace();
        }
    }

    protected void addEdge(String startVertexId, String endVertexId, double weight) {
        // actually, we should make sure all vertices ids must be correct.
        if (!idVertexIndex.containsKey(startVertexId) ||
                !idVertexIndex.containsKey(endVertexId) ||
                startVertexId.equals(endVertexId)) {
            throw new IllegalArgumentException("The edge from " + startVertexId +
                    " to " + endVertexId + " does not exist in the graph.");
        }

        // update the adjacent-list of the graph
        Set<BaseVertex> fanoutVertexSet = new HashSet<BaseVertex>();
        if (fanoutVerticesIndex.containsKey(startVertexId)) {
            fanoutVertexSet = fanoutVerticesIndex.get(startVertexId);
        }
        fanoutVertexSet.add(idVertexIndex.get(endVertexId));
        fanoutVerticesIndex.put(startVertexId, fanoutVertexSet);
        //
        Set<BaseVertex> faninVertexSet = new HashSet<BaseVertex>();
        if (faninVerticesIndex.containsKey(endVertexId)) {
            faninVertexSet = faninVerticesIndex.get(endVertexId);
        }
        faninVertexSet.add(idVertexIndex.get(startVertexId));
        faninVerticesIndex.put(endVertexId, faninVertexSet);
        // store the new edge
        vertexPairWeightIndex.put(new Pair<>(startVertexId, endVertexId), weight);
        ++edgeNum;
    }

    public void setVertexNum(int num) {
        vertexNum = num;
    }

    public BaseVertex getVertex(String id) {
        return idVertexIndex.get(id);
    }

}
