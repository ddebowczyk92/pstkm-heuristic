package pl.pstkm.test;

import org.apache.log4j.Logger;
import org.junit.Test;
import pl.pstkm.graph.PSTKMGraph;
import pl.pstkm.graph.Path;
import pl.pstkm.graph.algorithm.YenTopKShortestPaths;

import java.util.List;


/**
 * Created by DominikD on 2016-01-08.
 */
public class GraphTest {

    private static final Logger log = Logger.getLogger(GraphTest.class);

    @Test
    public void simpleTest() {
        PSTKMGraph graph = new PSTKMGraph("data/graph_1");
        YenTopKShortestPaths yen = new YenTopKShortestPaths(graph);
        List<Path> list = yen.getShortestPaths(graph.getVertex("W1"), graph.getVertex("W4"), 7);
        for (Path path : list) {
            log.debug(path.toString());
        }


    }
}
