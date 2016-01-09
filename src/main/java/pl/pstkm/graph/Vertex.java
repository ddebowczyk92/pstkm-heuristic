package pl.pstkm.graph;


import pl.pstkm.graph.abstraction.BaseVertex;

/**
 * Created by DominikD on 2015-12-12.
 */
public class Vertex implements BaseVertex, Comparable<Vertex> {

    private String id;
    private double weight;

    public Vertex(String id){
        this.id = id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public double getWeight() {
        return weight;
    }

    @Override
    public void setWeight(double weight) {
        this.weight = weight;
    }

    @Override
    public int compareTo(Vertex o) {
        double diff = this.weight - o.weight;
        if (diff > 0) {
            return 1;
        } else if (diff < 0) {
            return -1;
        } else {
            return 0;
        }
    }

    @Override
    public String toString() {
        return "" + this.id;
    }

    @Override
    public void reset() {
        id = null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Vertex that = (Vertex) o;

        return id.equals(that.id);

    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
