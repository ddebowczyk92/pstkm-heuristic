package pl.pstkm.linkpath;

import pl.pstkm.graph.Graph;
import pl.pstkm.graph.Path;
import pl.pstkm.graph.abstraction.BaseVertex;
import pl.pstkm.graph.algorithm.DijkstraShortestPath;
import pl.pstkm.graph.algorithm.YenTopKShortestPaths;
import pl.pstkm.graph.utils.Pair;
import pl.pstkm.simannealing.AnnealingFunction;

import java.util.ArrayList;
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

    private ArrayList<Path> chosenPaths;

    public PathProblem(Graph graph, Set<Demand> demands) {
        this.graph = graph;
        this.demands = demands;
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
    //funkcja sprawdzajaca czy dany zestaw sciezek dal danych demandow jest spelniony

    private boolean checkEdgeLoadForDemands(List<Path> paths, Set<Demand> demands) {
        HashMap<Pair<String, String>, Double> edgeLoad = new HashMap<>();
        double costOfDemands = 0;
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

        for(Demand dem : demands){
            costOfDemands = costOfDemands + dem.getCost();
        }

        for (Double load : edgeLoad.values()) {
            if (load > costOfDemands) {
                return false;
            }
        }
        return true;
    }


    @Override
    public double  getResult(){return 0;}

    //funkcja zwracajaca zestaw powiedzmy najlepszych sciezek spelniajacych demandy
    //glownie korzysta z nowej funkcji checkEdgeLoadForDemands(List<Path> paths, Set<Demand> demands)
    //sprawdzajacej czy zestaw demandow dla wyboru danych sciezek moze byc spelniony


    public List<Path> getResult(Set<Demand> demands) {
        int w=0;
        Path[][] listsOfPaths;
        Path[] temporaryPath;
        //List<Path> chosenPaths;
        temporaryPath =new  Path[demands.size()];
        listsOfPaths = new Path[demands.size()][20];
        List<Path> chosenPaths = new ArrayList<Path>();
        //tworzenie 20 sciezek dla kazdego demandu
        for (Demand demand : demands) {
            YenTopKShortestPaths yenTopKShortestPaths  = new YenTopKShortestPaths(graph);
            List<Path> PathsForDemand = yenTopKShortestPaths.getShortestPaths(demand.getSourceNode(), demand.getSinkNode(), 20);
            for(int z = 0; z< PathsForDemand.size(); z++){
                listsOfPaths[w][z]=PathsForDemand.get(z);
            }
            w++;
        }
        //podstawowy zestaw najlepszych sciezek
        for(int i2 =0; demands.size() > i2; i2++){
            temporaryPath[i2]=listsOfPaths[i2][0];
            chosenPaths.add(listsOfPaths[i2][0]);
            //chosenPaths.add(temporaryPath[i2]);
        }
        if(checkEdgeLoadForDemands(chosenPaths, demands)){
            return chosenPaths;
        }else {
            //wybieranie innych sciezek gdy podatwowe nie sa spelnione
            //petle po prostu powoduja,ze wybeiramy inne zestawy
            //tabice dlatgeo by mozna bylo sie latwo przemieszczac po tym
            chosenPaths.clear();
            for(int k=0; 19 > k; k++) {
                for (int i = 0; demands.size() > i; i++) {
                    for (int j = 0; 19 > j - k; j++) {
                        temporaryPath[i]=listsOfPaths[i][j+k];
                        for (int i3 = 0; demands.size() > i3; i3++) {
                            chosenPaths.add(temporaryPath[i3]);
                        }
                        if(checkEdgeLoadForDemands(chosenPaths, demands)){
                            return chosenPaths;
                        }
                        chosenPaths.clear();
                    }

                }
            }
        }


        return chosenPaths;
    }
}
