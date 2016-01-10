package pl.pstkm.linkpath;

import pl.pstkm.graph.Graph;
import pl.pstkm.graph.Vertex;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

/**
 * Created by Piotrek on 2016-01-09.
 */
public class InputData {

    private Graph graph;
    private HashMap<String, Demand> demands;
    private HashMap<String, Configuration> configurations;

    public InputData() {
    }

    public InputData(String file) {
        importFromFile(file);
    }


    public void importFromFile(String dataFileName) {


        try {
            // 1. read the file and put the content in the buffer
            FileReader input = new FileReader(dataFileName);
            BufferedReader bufRead = new BufferedReader(input);

            boolean isFirstLine = true;
            boolean isSecondLine = true;
            boolean isThirdLine = true;
            int j = 0;
            demands = new HashMap<String, Demand>();

            String line2;    // String that holds current file line

            // 2. Read first line
            line2 = bufRead.readLine();
            while (line2 != null) {
                // 2.1 skip the empty line
                if (line2.trim().equals("")) {
                    line2 = bufRead.readLine();
                    j++;
                    continue;
                }

                // 2.2 generate nodes and edges for the graph
                if (isFirstLine) {
                    //2.2.1 obtain the number of nodes in the graph
                    isFirstLine = false;
                    String sciezka = line2.trim();
                    graph = new PSTKMGraph(sciezka);

                } else if (j == 1) {
                    isSecondLine = false;
                    String[] configurationArr = line2.trim().split("\\s");
                    configurations = new HashMap<String, Configuration>();

                    for (int i = 0; i < configurationArr.length; i++) {
                        Configuration configuration = null;
                        configuration = new Configuration(configurationArr[i]);
                        configurations.put(configurationArr[i], configuration);
                    }
                } else if (j == 2) {
                    String[] strList1 = line2.trim().split("\\s");

                    String demandId = strList1[0];
                    String startDemandId = strList1[1];
                    String endDemandId = strList1[2];
                    double weightDemand = Double.parseDouble(strList1[3]);
                    Demand demand = null;
                    demand = new Demand((WirelessNode) graph.getVertex(startDemandId), (WirelessNode) graph.getVertex(endDemandId), weightDemand);
                    demands.put(demandId, demand);


                } else if (j == 3) {
                    String[] strList2 = line2.trim().split("\\s");
                    String configurationId = strList2[0];
                    String apNodeId = strList2[1];
                    Configuration configuration = configurations.get(configurationId);
                    Vertex node = (Vertex) graph.getVertex(apNodeId);
                    node.addConfiguration(configuration);


                } else if (j == 4) {

                    String[] strList3 = line2.trim().split("\\s");
                    String configurationId = strList3[0];
                    String wNodeId = strList3[1];
                    Configuration configuration = configurations.get(configurationId);
                    Vertex node = (Vertex) graph.getVertex(wNodeId);
                    node.addConfiguration(configuration);

                }
                line2 = bufRead.readLine();
            }

            bufRead.close();
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    public Graph getGraph() {
        return graph;
    }

    public void setGraph(Graph graph) {
        this.graph = graph;
    }

    public HashMap<String, Demand> getDemands() {
        return demands;
    }

    public void setDemands(HashMap<String, Demand> demands) {
        this.demands = demands;
    }

    public HashMap<String, Configuration> getConfigurations() {
        return configurations;
    }

    public void setConfigurations(HashMap<String, Configuration> configurations) {
        this.configurations = configurations;
    }
}
