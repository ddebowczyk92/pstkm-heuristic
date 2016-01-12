package pl.pstkm.test;

import org.apache.log4j.Logger;
import org.junit.Test;
import pl.pstkm.graph.Path;
import pl.pstkm.graph.utils.Pair;
import pl.pstkm.linkpath.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by DominikD on 2016-01-10.
 */
public class ProblemSolverTest {
    private static final Logger log = Logger.getLogger(ProblemSolverTest.class);

    @Test
    public void test1() {
        InputData inputData = new InputData("data/problem_variables2.txt");
        ProblemSolver problemSolver = new ProblemSolver(inputData);
        Pair<Set<Configuration>, Double> result = problemSolver.solve();
        PSTKMGraph graph = (PSTKMGraph) inputData.getGraph();
        graph.applyChosenConfigurations(result.first());
        PathProblem problem = new PathProblem(graph, new ArrayList<>(inputData.getDemands().values()));
        problem.getResult();
        List<Path> paths = problem.getChosenPaths();
        for (Configuration conf : result.first()) {
            log.info(conf.getId());
        }
        log.info("objective: " + result.second());
        for(Path path : paths){
            log.info(path.toString());
        }
    }

}
