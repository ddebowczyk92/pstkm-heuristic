package pl.pstkm.simannealing;

/**
 * Created by DominikD on 2016-01-10.
 */

import pl.pstkm.graph.Graph;
import pl.pstkm.graph.Vertex;
import pl.pstkm.graph.abstraction.BaseVertex;
import pl.pstkm.linkpath.Configuration;
import pl.pstkm.linkpath.PSTKMGraph;

import java.util.List;
import java.util.Set;


public class SimulatedAnnealing {
    /**
     * TODO implementacja symulowanego wyrzażania oparta na
     * interfejsie AnnealingFunction.java (można go rozszerzyć o potrzebne elementy lub zastąpić klasą abstrakcyjną)
     */

    //te funkcje pewnie trzeba przeniesc w inne meisjce ale to juz sam to lepiej rozplanujesz

    private PSTKMGraph graph;
    private int numberOfWirelessNodes;
    private int numberOfAPs;
    private List<BaseVertex> vertexList;
    private List<Set<Configuration>> possibleConfigurationSets;


    public SimulatedAnnealing(PSTKMGraph graph, List<Set<Configuration>> possibleConfigurationSets) {
        this.graph = graph;
        vertexList = graph.getVertexList();
        numberOfWirelessNodes = graph.getNumberW();
        this.possibleConfigurationSets = possibleConfigurationSets;
        numberOfAPs = graph.getNumberAP();
    }

    private int numberOfWirelessNodes() {
        int j = 0;
        for (int i = 0; vertexList.size() < i; i++) {
            Vertex node = (Vertex) vertexList.get(i);
            if (node.getId().charAt(0) == 'W') {
                j++;
            }
        }
        return j;
    }

    private int numberOfAPs() {
        int j = 0;
        for (int i = 0; vertexList.size() < i; i++) {
            Vertex node = (Vertex) vertexList.get(i);
            if (node.getId().charAt(0) == 'N') {
                j++;
            }
        }
        return j;
    }




    //wybieranie kolejnych zestawow funkcji
    public Set<Configuration> simulatedAnnealing(int random) {
        Set<Configuration> configurations = null;
        int FunkcjaCelu = 0;
        for (int i = random; possibleConfigurationSets.size() < i; i++) {
            if (mainFunction(i) >= FunkcjaCelu) {
                FunkcjaCelu = mainFunction(i);
                configurations = possibleConfigurationSets.get(i);
            }
        }
        return configurations;
    }


    //funkcja celu
    private int mainFunction(int i) {
        int numberOfConnections = 0;
        int mean = 0;
        Set<Configuration> configurations = possibleConfigurationSets.get(i);
        for (Configuration conf : configurations) {
            numberOfConnections = numberOfConnections + conf.getNumber();
        }
        mean = numberOfConnections / numberOfWirelessNodes;
        return mean;
    }


}
