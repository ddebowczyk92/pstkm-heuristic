package pl.pstkm.simannealing;

/**
 * Created by DominikD on 2016-01-10.
 */

import pl.pstkm.exception.InvalidInputDataException;
import pl.pstkm.graph.abstraction.BaseVertex;
import pl.pstkm.graph.utils.Pair;
import pl.pstkm.linkpath.Configuration;
import pl.pstkm.linkpath.Demand;
import pl.pstkm.linkpath.PSTKMGraph;
import pl.pstkm.linkpath.PathProblem;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
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
    private Set<Demand> demands;
    private double temperature;
    private final double stopTemp;
    private final double coolingRate;

    private Random randomGenerator;


    public SimulatedAnnealing(PSTKMGraph graph, List<Set<Configuration>> possibleConfigurationSets, Set<Demand> demands, double startTemp, double stopTemp, double coolingRate) throws InvalidInputDataException {
        this.graph = graph;
        this.temperature = startTemp;
        this.stopTemp = stopTemp;
        this.coolingRate = coolingRate;
        vertexList = graph.getVertexList();
        numberOfWirelessNodes = graph.getNumberW();
        this.possibleConfigurationSets = possibleConfigurationSets;
        numberOfAPs = graph.getNumberAP();
        this.demands = demands;
        randomGenerator = new Random();
        reduceConfigurationSet();

    }


    //wybieranie kolejnych zestawow funkcji
    public Pair<Set<Configuration>, Double> findSolution() {
        int random = randomInt();
        Set<Configuration> bestConfigurations = possibleConfigurationSets.get(random);
        double distance = objective(bestConfigurations);
        Set<Configuration> nextConfigurations = null;
        double deltaDistance = 0.0;
        int iteration = -1;
        while (temperature > stopTemp) {
            nextConfigurations = possibleConfigurationSets.get(randomInt());
            deltaDistance = objective(nextConfigurations) - distance;
            if ((deltaDistance < 0) || (distance > 0 && Math.exp(-deltaDistance / temperature) > randomGenerator.nextDouble())) {
                bestConfigurations = nextConfigurations;
                distance = deltaDistance + distance;
            }
            temperature = temperature * coolingRate;
            iteration++;
        }
        return new Pair<>(bestConfigurations, distance);
    }


    //funkcja celu
    private double objective(Set<Configuration> configurations) {
        int numberOfConnections = 0;
        double mean = 0;
        for (Configuration conf : configurations) {
            numberOfConnections = numberOfConnections + conf.getNumberOfWirelessnodes();
        }
        mean = (double) numberOfConnections / (double) numberOfWirelessNodes;
        return mean;
    }

    private void reduceConfigurationSet() throws InvalidInputDataException {
        List<Set<Configuration>> reduced = new ArrayList<>();
        for (Set<Configuration> set : possibleConfigurationSets) {
            PSTKMGraph pstkmGraph = graph.copy();
            pstkmGraph.applyChosenConfigurations(set);
            PathProblem problem = new PathProblem(pstkmGraph, new ArrayList<>(demands));
            if (problem.getResult()) {
                reduced.add(set);
            }
        }
        if (reduced.isEmpty()) {
            throw new InvalidInputDataException();
        }
        possibleConfigurationSets = reduced;
    }

    private boolean shouldAccept(double temperature, double deltaE) {
        return (deltaE > 0.0) || (new Random().nextDouble() <= probabilityOfAcceptance(temperature, deltaE));
    }

    private double probabilityOfAcceptance(double temperature, double deltaE) {
        return Math.exp(deltaE / temperature);
    }

    public int randomInt() {
        int index = Math.abs(randomGenerator.nextInt(possibleConfigurationSets.size()));
        return index;
    }


}
