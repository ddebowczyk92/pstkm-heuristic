package pl.pstkm;

import pl.pstkm.graph.PSTKMGraph;
import pl.pstkm.graph.algorithm.YenTopKShortestPaths;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        PSTKMGraph graph = new PSTKMGraph("data/graph_1");
        YenTopKShortestPaths yen = new YenTopKShortestPaths(graph);

    }
}
