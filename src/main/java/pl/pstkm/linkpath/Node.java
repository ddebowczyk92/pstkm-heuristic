package pl.pstkm.linkpath;

import pl.pstkm.graph.Vertex;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by DominikD on 2016-01-09.
 */
public class Node extends Vertex {

    private Set<Configuration> possibleConfigurations;
    private Configuration chosenConf;

    public Node(String id) {
        super(id);
    }

    public void applyChosenConfiguration(Set<Configuration> configurations) {
        for (Configuration configuration : configurations) {
            if (isAccessPoint() && this.getPossibleConfigurations().contains(configuration)) {
                this.setChosenConf(configuration);
                break;
            }
        }

    }

    public Set<Configuration> getPossibleConfigurations() {
        return possibleConfigurations;
    }

    public void setPossibleConfigurations(Set<Configuration> possibleConfigurations) {
        this.possibleConfigurations = possibleConfigurations;
    }

    @Override
    public void addConfiguration(Configuration conf) {
        if(possibleConfigurations == null)
            possibleConfigurations = new HashSet<>();
        this.possibleConfigurations.add(conf);
    }

    public Configuration getChosenConf() {

        return chosenConf;
    }

    public void setChosenConf(Configuration chosenConf) {
        this.chosenConf = chosenConf;
    }

    public boolean isAccessPoint() {
        return possibleConfigurations != null && !possibleConfigurations.isEmpty();
    }
}
