package pl.pstkm.test;

import org.junit.Assert;
import org.junit.Test;
import pl.pstkm.graph.Path;
import pl.pstkm.graph.algorithm.YenTopKShortestPaths;
import pl.pstkm.linkpath.*;
import pl.pstkm.linkpath.util.Problem;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Piotrek on 2016-01-10.
 */
public class ProblemTest {
    //private static final Logger log = Logger.getLogger(ProblemTest.class);

    @Test
    public void test1() {
        Problem problemGraph = new Problem("data/problem_variables");
        System.out.format(" ");


/*

        YenTopKShortestPaths yen = new YenTopKShortestPaths(graph);
        List<Path> paths = yen.getShortestPaths(problemGraph.graph.getVertex("W3"), problemGraph.graph.getVertex("W2"), 3);
        for(Path path: paths) {
            log.debug(path.toString());
        }


        problemGraph.graph.applyChosenConfigurations(configurations);

        Path shortestPath2 = yen.getShortestPath(graph.getVertex("W3"), graph.getVertex("W2"));
        log.debug(shortestPath2);

        Assert.assertEquals(paths.get(2), shortestPath2);
*/
    }
}
