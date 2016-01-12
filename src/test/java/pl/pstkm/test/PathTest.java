package pl.pstkm.test;

import org.apache.log4j.Logger;
import org.junit.Test;
import pl.pstkm.linkpath.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Piotrek on 2016-01-11.
 */
public class PathTest {

    private static final Logger log = Logger.getLogger(InputDataTest.class);

    @Test
    public void test1() {
        InputData input = new InputData("data/problem_variables");
        List<Demand> demands = new ArrayList<>(input.getDemands().values());
        PSTKMGraph graph = (PSTKMGraph) input.getGraph();
        Configuration k1 = new Configuration("K1");
        Configuration k2 = new Configuration("K4");
        Set<Configuration> set = new HashSet<>();
        set.add(k1);
        set.add(k2);
        graph.applyChosenConfigurations(set);
        PathProblem problem = new PathProblem(graph, demands);
        boolean pathResult = problem.getResult();

    }
}
