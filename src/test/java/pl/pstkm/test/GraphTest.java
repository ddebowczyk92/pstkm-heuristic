package pl.pstkm.test;

import org.junit.Test;
import pl.pstkm.graph.PSTKMGraph;
import pl.pstkm.graph.algorithm.YenTopKShortestPaths;

/**
 * Created by DominikD on 2016-01-08.
 */
public class GraphTest {

    @Test
    public void simpleTest() {
        PSTKMGraph graph = new PSTKMGraph("data/graph_1");
        YenTopKShortestPaths yen = new YenTopKShortestPaths(graph);
    }
}
