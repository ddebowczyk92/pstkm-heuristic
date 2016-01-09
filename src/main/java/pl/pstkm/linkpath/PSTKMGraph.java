package pl.pstkm.linkpath;

import pl.pstkm.graph.Graph;
import pl.pstkm.graph.abstraction.BaseVertex;
import pl.pstkm.graph.utils.Pair;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by DominikD on 2016-01-09.
 */
public class PSTKMGraph extends Graph {

    public PSTKMGraph() {
        super();
    }

    public PSTKMGraph(String file) {
        super(file);
    }

    public PSTKMGraph(final Graph graph) {
        super(graph);
    }

    public PSTKMGraph(final Graph graph, Set<Configuration> configurations) {
        super(graph);
        applyChosenConfigurations(configurations);
    }

    @Override
    public void importFromFile(String dataFileName) {
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
                        BaseVertex vertex = null;
                        if (vertexArr[i].charAt(0) == 'W') {
                            vertex = new WirelessNode(vertexArr[i]);
                        } else vertex = new Node(vertexArr[i]);
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

    public void applyChosenConfigurations(Set<Configuration> configurations) {
        for (BaseVertex vertex : this.getVertexList()) {
            if (vertex instanceof Node) {
                Node ap = (Node) vertex;
                ap.applyChosenConfiguration(configurations);
            }
        }
        deleteInactiveEdges();
    }

    private void deleteInactiveEdges() {
        Set<Pair<String, String>> edgesToDelete = new HashSet<>();
        for (Pair<String, String> edge : this.vertexPairWeightIndex.keySet()) {
            Node apNode = null;
            WirelessNode wNode = null;
            if (this.getVertex(edge.first()) instanceof Node) apNode = (Node) this.getVertex(edge.first());
            else if (this.getVertex(edge.second()) instanceof Node) apNode = (Node) this.getVertex(edge.second());

            if (this.getVertex(edge.first()) instanceof WirelessNode)
                wNode = (WirelessNode) this.getVertex(edge.first());
            else if (this.getVertex(edge.second()) instanceof WirelessNode)
                wNode = (WirelessNode) this.getVertex(edge.second());

            if (wNode != null && apNode != null && apNode.isAccessPoint()) {
                if (!wNode.getConfigurations().contains(apNode.getChosenConf())) {
                    edgesToDelete.add(edge);
                }
            }
        }
        for (Pair<String, String> edge : edgesToDelete) {
            this.vertexPairWeightIndex.remove(edge);
            Set<BaseVertex> vertexIn = this.faninVerticesIndex.get(edge.first());
            vertexIn.remove(this.getVertex(edge.second()));
            Set<BaseVertex> vertexOut = this.fanoutVerticesIndex.get(edge.second());
            vertexOut.remove(this.getVertex(edge.first()));
            edgeNum--;
        }
    }
}
