package pl.pstkm.simannealing;

/**
 * Created by DominikD on 2016-01-10.
 */

import pl.pstkm.graph.Graph;
import pl.pstkm.graph.Vertex;
import pl.pstkm.graph.abstraction.BaseVertex;
import pl.pstkm.linkpath.Configuration;

import java.util.List;
import java.util.Set;


public class SimulatedAnnealing {
    /**
     * TODO implementacja symulowanego wyrzażania oparta na
     * interfejsie AnnealingFunction.java (można go rozszerzyć o potrzebne elementy lub zastąpić klasą abstrakcyjną)
     */

    private Graph graph;
    private int numberOfWirelessNodes;
    private List<BaseVertex> vertexList;
    private List<Set<Configuration>> possibleConfigurationSets;


    public SimulatedAnnealing(Graph graph, List<Set<Configuration>> possibleConfigurationSets){
        this.graph = graph;
        vertexList = graph.getVertexList();
        numberOfWirelessNodes = numberOfWirelessNodes();
        this.possibleConfigurationSets = possibleConfigurationSets;
    }

    private int numberOfWirelessNodes(){
        int j=0;
        for(int i =0; vertexList.size() < i; i++){
            Vertex node = (Vertex) vertexList.get(i);
            if(node.getId().charAt(0)=='W'){
                j++;
            }
        }
        return j;
    }

    public Set<Configuration> simulatedAnnealing(){
        Set<Configuration> configurations = null;
        int FunkcjaCelu = 0;
        for(int i = 0; possibleConfigurationSets.size() < i; i++){
            if(mainFunction(i) >= FunkcjaCelu){
                FunkcjaCelu = mainFunction(i);
                configurations = possibleConfigurationSets.get(i);
            }

        }
        return configurations;
    }


    //zmiana set na list by moc wyciagac
    //dodanie do configuration wycagania liczby W
    private int mainFunction(int i){
        int numberOfConnections = 0;
        int mean = 0;
        Set<Configuration> configurations = possibleConfigurationSets.get(i);
        for(int j =0; configurations.size() < j; j++){
            Configuration configuration = configurations.get(j);
            numberOfConnections = numberOfConnections + configuration.getNumber();
        }
        mean = numberOfConnections/numberOfWirelessNodes;
        return mean;
    }




}
