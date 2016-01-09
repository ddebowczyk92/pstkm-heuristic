package pl.pstkm.test;

import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;
import pl.pstkm.graph.Path;
import pl.pstkm.graph.algorithm.YenTopKShortestPaths;
import pl.pstkm.linkpath.Configuration;
import pl.pstkm.linkpath.Node;
import pl.pstkm.linkpath.PSTKMGraph;
import pl.pstkm.linkpath.WirelessNode;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by DominikD on 2016-01-09.
 */
public class PSTKMGraphTest {

    private static final Logger log = Logger.getLogger(PSTKMGraphTest.class);

    @Test
    public void test1() {
        PSTKMGraph graph = new PSTKMGraph("data/graph_1");
        Set<Configuration> configurations = new HashSet<>();
        Configuration k1 = new Configuration("K1");
        Configuration k2 = new Configuration("K2");
        Configuration k3 = new Configuration("K3");
        Configuration k4 = new Configuration("K4");
        Configuration k5 = new Configuration("K5");
        Configuration k6 = new Configuration("K6");

        configurations.addAll(Arrays.asList(new Configuration[]{k2, k6}));

        WirelessNode w1 = (WirelessNode) graph.getVertex("W1");
        WirelessNode w2 = (WirelessNode) graph.getVertex("W2");
        WirelessNode w3 = (WirelessNode) graph.getVertex("W3");
        WirelessNode w4 = (WirelessNode) graph.getVertex("W4");

        Node n1 = (Node) graph.getVertex("N1");
        Node n2 = (Node) graph.getVertex("N2");
        Node n3 = (Node) graph.getVertex("N3");

        w1.addConfiguration(k1);
        w1.addConfiguration(k2);
        w1.addConfiguration(k4);

        w2.addConfiguration(k2);
        w2.addConfiguration(k3);
        w2.addConfiguration(k4);

        w3.addConfiguration(k3);
        w3.addConfiguration(k4);
        w3.addConfiguration(k6);

        w4.addConfiguration(k5);
        w4.addConfiguration(k6);

        n1.addConfiguration(k1);
        n1.addConfiguration(k2);

        n2.addConfiguration(k3);
        n2.addConfiguration(k4);

        n3.addConfiguration(k5);
        n3.addConfiguration(k6);

        YenTopKShortestPaths yen = new YenTopKShortestPaths(graph);
        List<Path> paths = yen.getShortestPaths(graph.getVertex("W3"), graph.getVertex("W2"), 3);
        for(Path path: paths) {
            log.debug(path.toString());
        }


        graph.applyChosenConfigurations(configurations);

        Path shortestPath2 = yen.getShortestPath(graph.getVertex("W3"), graph.getVertex("W2"));
        log.debug(shortestPath2);

        Assert.assertEquals(paths.get(2), shortestPath2);

    }
}
