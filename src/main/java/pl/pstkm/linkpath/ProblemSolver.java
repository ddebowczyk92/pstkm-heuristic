package pl.pstkm.linkpath;

import com.google.common.collect.Sets;
import org.apache.log4j.Logger;
import pl.pstkm.exception.InvalidInputDataException;
import pl.pstkm.graph.utils.Pair;
import pl.pstkm.simannealing.SimulatedAnnealing;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by DominikD on 2016-01-10.
 */
public class ProblemSolver {

    private static final Logger log = Logger.getLogger(ProblemSolver.class);

    private final InputData inputData;

    private List<Set<Configuration>> possibleConfigurationSets;


    public ProblemSolver(InputData inputData) {
        this.inputData = inputData;
        generateConfigurationSets();
    }

    public Pair<Set<Configuration>, Double> solve() {
        SimulatedAnnealing simulatedAnnealing = null;
        try {
            simulatedAnnealing = new SimulatedAnnealing((PSTKMGraph) inputData.getGraph(), possibleConfigurationSets, new HashSet<>(inputData.getDemands().values()), 10000.0, 0.00001, 0.9999);
        } catch (InvalidInputDataException e) {
            log.error("Invalid inpu data", e);
        }
        return simulatedAnnealing.findSolution();
    }

    private void generateConfigurationSets() {
        Set<Configuration> configurations = new HashSet<>(inputData.getConfigurations().values());
        possibleConfigurationSets = new ArrayList(Sets.powerSet(configurations));
        possibleConfigurationSets.remove(0);
    }
}
