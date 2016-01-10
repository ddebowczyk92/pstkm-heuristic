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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Demand demand = (Demand) o;

        if (Double.compare(demand.cost, cost) != 0) return false;
        if (!sourceNode.equals(demand.sourceNode)) return false;
        return sinkNode.equals(demand.sinkNode);

    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = sourceNode.hashCode();
        result = 31 * result + sinkNode.hashCode();
        temp = Double.doubleToLongBits(cost);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }
}
