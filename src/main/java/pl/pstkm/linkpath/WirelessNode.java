package pl.pstkm.linkpath;

import pl.pstkm.graph.Vertex;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by DominikD on 2016-01-09.
 */
public class WirelessNode extends Vertex {

    private Set<Configuration> configurations;

    public WirelessNode(String id) {
        super(id);
    }

    public Set<Configuration> getConfigurations() {
        return configurations;
    }

    public void setConfigurations(Set<Configuration> configurations) {
        this.configurations = configurations;
    }

    public void addConfiguration(Configuration conf) {
        if (configurations == null) configurations = new HashSet<>();
        configurations.add(conf);
    }


}
