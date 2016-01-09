package pl.pstkm.linkpath;

/**
 * Created by DominikD on 2016-01-09.
 */
public class Demand {

    private final WirelessNode sourceNode;

    private final WirelessNode sinkNode;

    private final double cost;

    public Demand(WirelessNode sourceNode, WirelessNode sinkNode, double cost) {
        this.sourceNode = sourceNode;
        this.sinkNode = sinkNode;
        this.cost = cost;
    }

    public WirelessNode getSourceNode() {
        return sourceNode;
    }

    public WirelessNode getSinkNode() {
        return sinkNode;
    }

    public double getCost() {
        return cost;
    }
}
