package pl.pstkm.linkpath;

import pl.pstkm.graph.Graph;
import pl.pstkm.graph.Path;
import pl.pstkm.graph.abstraction.BaseVertex;
import pl.pstkm.graph.algorithm.DijkstraShortestPath;
import pl.pstkm.graph.utils.Pair;
import pl.pstkm.simannealing.AnnealingFunction;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * Created by DominikD on 2016-01-10.
 */
public class PathProblem implements AnnealingFunction {

    /**
     * TODO poskładać to do kupy!!! zaimplementować getResult
     */
    private final Graph graph;

    private final Set<Demand> demands;

    private final List<Path> chosenPaths;

    public PathProblem(Graph graph, Set<Demand> demands, List<Path> chosenPaths) {
        this.graph = graph;
        this.demands = demands;
        this.chosenPaths = chosenPaths;
    }

    public boolean checkIfNetworkRealizesDemands() {
        for (Demand demand : demands) {
            DijkstraShortestPath dijkstraShortestPath = new DijkstraShortestPath(graph);
            Path shortestPath = dijkstraShortestPath.getShortestPath(demand.getSourceNode(), demand.getSinkNode());
            if (shortestPath.getVertexList().isEmpty()) {
                return false;
            }
        }
        return true;
    }

    private boolean checkEdgeLoadForDemand(List<Path> paths, Demand demand) {
        HashMap<Pair<String, String>, Double> edgeLoad = new HashMap<>();
        for (Path path : paths) {
            List<BaseVertex> vertexList = path.getVertexList();
            for (int i = 0; i < vertexList.size() - 1; i++) {
                BaseVertex begin = vertexList.get(i);
                BaseVertex end = vertexList.get(i + 1);
                Pair<String, String> pair = new Pair(begin.getId(), end.getId());
                if (edgeLoad.containsKey(pair)) {
                    Double load = edgeLoad.get(pair);
                    load += graph.getEdgeWeight(begin, end);
                    edgeLoad.put(pair, load);

                } else {
                    edgeLoad.put(pair, graph.getEdgeWeight(begin, end));
                }
            }
        }

        for (Double load : edgeLoad.values()) {
            if (load > demand.getCost()) {
                return false;
            }
        }
        return true;
    }



    @Override
    public double getResult() {
        return 0;
    }
}
