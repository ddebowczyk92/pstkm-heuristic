package pl.pstkm.graph;

import pl.pstkm.graph.abstraction.BaseElementWithWeight;
import pl.pstkm.graph.abstraction.BaseVertex;

import java.util.List;
import java.util.Vector;

/**
 * Created by DominikD on 2015-12-13.
 */
public class Path implements BaseElementWithWeight {

    private List<BaseVertex> vertexList = new Vector<BaseVertex>();
    private double weight = -1;

    public Path() {
    }

    public Path(List<BaseVertex> vertexList, double weight) {
        this.vertexList = vertexList;
        this.weight = weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public List<BaseVertex> getVertexList() {
        return vertexList;
    }

    @Override
    public double getWeight() {
        return 0;
    }

    @Override
    public boolean equals(Object right) {

        if (right instanceof Path) {
            Path rPath = (Path) right;
            return vertexList.equals(rPath.vertexList);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return vertexList.hashCode();
    }

    @Override
    public String toString() {
        return vertexList.toString() + ":" + weight;
    }
}
