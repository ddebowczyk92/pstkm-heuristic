package pl.pstkm.simannealing;

/**
 * Created by DominikD on 2016-01-10.
 */

import org.apache.log4j.Logger;
import pl.pstkm.exception.InvalidInputDataException;
import pl.pstkm.graph.abstraction.BaseVertex;
import pl.pstkm.graph.utils.Pair;
import pl.pstkm.linkpath.*;

import java.util.*;


public class SimulatedAnnealing {

    private static final Logger log = Logger.getLogger(SimulatedAnnealing.class);

    private PSTKMGraph graph;
    private int numberOfWirelessNodes;
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
        this.numberOfWirelessNodes = graph.getNumberW();
        this.possibleConfigurationSets = possibleConfigurationSets;
        this.demands = demands;
        this.randomGenerator = new Random();
        reduceConfigurationSet();
    }

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
        log.debug("Number of iterations: " + iteration);
        return new Pair<>(bestConfigurations, distance);
    }


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
            if (checkIfAPHasOneConfiguration(pstkmGraph, set)) {
                PathProblem problem = new PathProblem(pstkmGraph, new ArrayList<>(demands));
                if (problem.getResult()) {
                    reduced.add(set);
                }
            }
        }
        if (reduced.isEmpty()) {
            throw new InvalidInputDataException();
        }

        possibleConfigurationSets = reduced;
    }

    private boolean checkIfAPHasOneConfiguration(PSTKMGraph graph, Set<Configuration> set) {
        List<BaseVertex> vertices = graph.getVertexList();
        for (BaseVertex vertex : vertices) {
            if (vertex instanceof Node) {
                Node node = (Node) vertex;
                if (node.isAccessPoint()) {
                    Set<Configuration> possibleConfigurations = new HashSet<>(node.getPossibleConfigurations());
                    if (possibleConfigurations.retainAll(set) && possibleConfigurations.size() > 1) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private int randomInt() {
        int index = Math.abs(randomGenerator.nextInt(possibleConfigurationSets.size()));
        return index;
    }
}
