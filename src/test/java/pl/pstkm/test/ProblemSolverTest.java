package pl.pstkm.test;

import org.apache.log4j.Logger;
import org.junit.Test;
import pl.pstkm.graph.utils.Pair;
import pl.pstkm.linkpath.Configuration;
import pl.pstkm.linkpath.InputData;
import pl.pstkm.linkpath.ProblemSolver;

import java.util.Set;

/**
 * Created by DominikD on 2016-01-10.
 */
public class ProblemSolverTest {
    private static final Logger log = Logger.getLogger(ProblemSolverTest.class);

    @Test
    public void test1() {
        InputData inputData = new InputData("data/problem_variables");
        ProblemSolver problemSolver = new ProblemSolver(inputData);
        Pair<Set<Configuration>, Double> result = problemSolver.solve();
        for (Configuration conf : result.first()) {
            log.info(conf.getId());
        }
        log.info("objective: " + result.second());
    }

}
