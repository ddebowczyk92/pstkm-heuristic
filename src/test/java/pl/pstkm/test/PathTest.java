package pl.pstkm.test;

import org.apache.log4j.Logger;
import org.junit.Test;
import pl.pstkm.graph.Path;
import pl.pstkm.linkpath.Demand;
import pl.pstkm.linkpath.InputData;
import pl.pstkm.linkpath.PathProblem;

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
        Set<Demand> demands = new HashSet<>(input.getDemands().values());
        PathProblem problem = new PathProblem(input.getGraph(),demands );
        List<Path> lista = problem.getResult(demands);
        System.out.format("");
    }
}
