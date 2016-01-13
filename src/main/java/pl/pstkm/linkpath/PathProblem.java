package pl.pstkm.linkpath;

import com.google.common.collect.HashBiMap;
import pl.pstkm.graph.Graph;
import pl.pstkm.graph.Path;
import pl.pstkm.graph.abstraction.BaseVertex;
import pl.pstkm.graph.algorithm.YenTopKShortestPaths;
import pl.pstkm.graph.utils.Pair;

import java.util.*;

/**
 * Created by DominikD on 2016-01-10.
 */
public class PathProblem {


    private final int numberOfBestPaths = 10;

    private final Graph graph;

    private final List<Demand> demands;

    private List<Path> chosenPaths = new ArrayList<>();

    public PathProblem(Graph graph, List<Demand> demands) {
        this.graph = graph;
        this.demands = demands;
    }

    public boolean getResult() {
        getResultPaths();
        return checkIfNetworkRealizesDemands() && !chosenPaths.isEmpty();
    }

    private boolean checkIfNetworkRealizesDemands() {
        for (Demand demand : demands) {
            YenTopKShortestPaths yen = new YenTopKShortestPaths(graph);
            Path shortestPath = yen.getShortestPath(graph.getVertex(demand.getSourceNode().getId()), graph.getVertex(demand.getSinkNode().getId()));
            if (shortestPath.getVertexList().isEmpty()) {
                return false;
            }
        }
        return true;
    }

    private List<Path> getResultPaths() {
        int kConstant = numberOfBestPaths;
        HashBiMap<Demand, List<Path>> demandCandidates = HashBiMap.create(demands.size());
        HashBiMap<Demand, Path> temporaryBestPaths = HashBiMap.create(demands.size());
        for (Demand demand : demands) {
            List<Path> shortestPaths = getShortestPaths(demand);
            demandCandidates.put(demand, shortestPaths);
            if (shortestPaths.size() == 0) {
                return new ArrayList<>();
            }
            temporaryBestPaths.put(demand, shortestPaths.get(0));
        }
        if (checkEdgeCapacityForDemands(temporaryBestPaths.inverse())) {
            chosenPaths.addAll(temporaryBestPaths.inverse().keySet());
            return chosenPaths;
        } else {
            for (int k = 0; k < kConstant - 1; k++) {
                for (int i = 0; i < demands.size(); i++) {
                    List<Path> paths = demandCandidates.get(demands.get(i));
                    if (k > paths.size() - 1) {
                        continue;
                    }
                    for (int j = k; j < paths.size(); j++) {
                        temporaryBestPaths.put(demands.get(i), paths.get(j));
                        if (checkEdgeCapacityForDemands(temporaryBestPaths.inverse())) {
                            chosenPaths.addAll(temporaryBestPaths.inverse().keySet());
                            return chosenPaths;
                        }
                        if (j == paths.size() - 1) {
                            temporaryBestPaths.put(demands.get(i), paths.get(k));
                        }
                    }
                }
            }
        }
        if (checkEdgeCapacityForDemands(temporaryBestPaths.inverse())) {
            chosenPaths.addAll(temporaryBestPaths.inverse().keySet());
            return chosenPaths;
        } else {
            return new ArrayList<>();
        }
    }

    private List<Path> getShortestPaths(Demand demand) {
        List<Path> pathsRealizingDemand = new ArrayList<>();
        YenTopKShortestPaths yen = new YenTopKShortestPaths(this.graph);
        List<Path> paths = yen.getShortestPaths(demand.getSourceNode(), demand.getSinkNode(), numberOfBestPaths);
        for (Path path : paths) {
            if (checkIfPathCanHandleDemand(path, demand)) {
                pathsRealizingDemand.add(path);
            }
        }
        return pathsRealizingDemand;
    }

    private boolean checkIfPathCanHandleDemand(Path path, Demand demand) {
        List<BaseVertex> vertexList = path.getVertexList();
        for (int i = 0; i < vertexList.size() - 1; i++) {
            BaseVertex begin = vertexList.get(i);
            BaseVertex end = vertexList.get(i + 1);
            if (graph.getEdgeWeight(begin, end) < demand.getCost()) {
                return false;
            }
        }
        return true;
    }

    private boolean checkEdgeCapacityForDemands(Map<Path, Demand> demandsPaths) {
        HashMap<Pair<String, String>, Double> edgeLoad = new HashMap<>();
        for (Path path : demandsPaths.keySet()) {
            List<BaseVertex> vertexList = path.getVertexList();
            for (int i = 0; i < vertexList.size() - 1; i++) {
                BaseVertex begin = vertexList.get(i);
                BaseVertex end = vertexList.get(i + 1);
                Pair<String, String> pair = new Pair(begin.getId(), end.getId());
                Demand demand = demandsPaths.get(path);
                if (edgeLoad.containsKey(pair)) {
                    Double load = demand.getCost() + edgeLoad.get(pair);
                    if (load > graph.getEdgeWeight(begin, end)) {
                        return false;
                    } else {
                        edgeLoad.put(pair, load);
                    }
                } else {
                    edgeLoad.put(pair, demand.getCost());
                }
            }
        }
        return true;
    }

    public List<Path> getChosenPaths() {
        return chosenPaths;
    }

}
