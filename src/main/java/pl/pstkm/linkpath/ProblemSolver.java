package pl.pstkm.linkpath;

import pl.pstkm.simannealing.SimulatedAnnealing;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by DominikD on 2016-01-10.
 */
public class ProblemSolver {

    /**
     * TODO tutaj powinno być uruchomione wyrzażanie dla zestawu konfiguracji
     */
    private final InputData inputData;

    /**
     * wszytskie możliwe zestawy konfiguracji
     */
    private List<Set<Configuration>> possibleConfigurationSets;


    public ProblemSolver(InputData inputData) {
        this.inputData = inputData;
        generateConfigurationSets();
    }

    public Set<Configuration> solve() {
        SimulatedAnnealing simulatedAnnealing = new SimulatedAnnealing(new PSTKMGraph(inputData.getGraph()), possibleConfigurationSets, new HashSet<>(inputData.getDemands().values()), 10000.0, 0.00001, 0.9999);
        return simulatedAnnealing.findSolution();
    }

    private void generateConfigurationSets() {
        //possibleConfigurationSets = new ArrayList();
        Set<Set<Configuration>> possibleConfigurationSets2 = new HashSet<>();
        List<String> configurationIds = new ArrayList<>(inputData.getConfigurations().keySet());
        for (String confId2 : configurationIds) {
            for (String confId : configurationIds) {

                Set<Configuration> firstConfigurationSet = new HashSet<>();
                firstConfigurationSet.add(inputData.getConfigurations().get(confId));
                possibleConfigurationSets2.add(firstConfigurationSet);

                for (int i = 0; i < configurationIds.size(); i++) {
                    Set<Configuration> configurationSet = new HashSet<>();
                    configurationSet.add(inputData.getConfigurations().get(confId2));
                    configurationSet.addAll(getConfigurationSubset(i, configurationIds, confId));
                    if (!configurationSet.isEmpty()) {
                        possibleConfigurationSets2.add(configurationSet);
                    }

                }

            }
        }
        possibleConfigurationSets = new ArrayList<>(possibleConfigurationSets2);
    }

    private Set<Configuration> getConfigurationSubset(int endIndex, List<String> configurationIds, String exceptId) {
        Set<Configuration> subset = new HashSet<>();
        for (int i = 0; i < endIndex; i++) {
            String id = configurationIds.get(i);
            if (!id.equals(exceptId)) subset.add(inputData.getConfigurations().get(id));
        }
        return subset;
    }
}
