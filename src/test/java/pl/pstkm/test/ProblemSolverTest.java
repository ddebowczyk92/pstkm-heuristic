package pl.pstkm.test;

import org.apache.log4j.Logger;
import org.junit.Test;
import pl.pstkm.linkpath.InputData;
import pl.pstkm.linkpath.ProblemSolver;

/**
 * Created by DominikD on 2016-01-10.
 */
public class ProblemSolverTest {
    private static final Logger log = Logger.getLogger(ProblemSolverTest.class);

    @Test
    public void test1(){
        InputData inputData = new InputData("data/problem_variables");
        ProblemSolver problemSolution = new ProblemSolver(inputData);

    }

}
