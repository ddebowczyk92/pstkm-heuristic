package pl.pstkm;

import org.apache.log4j.Logger;
import pl.pstkm.graph.Path;
import pl.pstkm.graph.utils.Pair;
import pl.pstkm.linkpath.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class App {
    public static final Logger log = Logger.getLogger(App.class);

    public static void main(String[] args) {
        InputData inputData = new InputData("data/" + args[0]);
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
        for (Path path : paths) {
            log.info(path.toString());
        }

    }
}
