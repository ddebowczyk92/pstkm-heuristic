package pl.pstkm;

import pl.pstkm.graph.Graph;
import pl.pstkm.graph.algorithm.YenTopKShortestPaths;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        Graph graph = new Graph("data/graph_1");
        YenTopKShortestPaths yen = new YenTopKShortestPaths(graph);

    }
}
