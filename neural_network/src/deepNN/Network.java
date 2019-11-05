package deepNN;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Network {

    ArrayList<Integer> amount_of_node_per_layer = new ArrayList<>();
    ArrayList<Node> nodes = new ArrayList<>();
    int num_input;

    public Network() {
        int current_layer_amount;
        Scanner sc = new Scanner(System.in);
        while ((sc.hasNextInt())) {
            amount_of_node_per_layer.add(sc.nextInt());
        }
        // create the network : n nodes per layer
        int layer_index = 0;
        for (int value: this.amount_of_node_per_layer) {
            for (int i = 0; i<value; i++){
                Node node = new Node();
                nodes.add(node);
            }
            this.connectNodeToRandomWeight(nodes.get(nodes.size()-1));
            layer_index++;
            System.out.println(value+" nodes create for layer :"+layer_index);
        }
    }

    /*
        1 w for each node
        Initialisation => random
     */
    public void connectNodeToRandomWeight(Node node) {
        double weight = Math.random() * (0 - 2);
        for (int i = 0; i < this.nodes.size(); i++){
            if (this.nodes.get(i).getNode_id() == node.getNode_id()) {
                node.setWeight(weight);
                System.out.println("Poids assigné au node num°"+node.getNode_id());
            }
        }
    }

    public void printArrayNodes() {
        System.out.println(this.nodes);
    }

    public void trainNetwork() {
        //code => forward training :
        // 1)sum of input for each hidden layer nodes
        // 2)backpropagation of weigths for each node with square error between
        // neural outputs and train_output

    }

    public ArrayList<Integer> returnNetwork() {
        return this.amount_of_node_per_layer;
    }

    @Override
    public String toString(){
        return "Node per layer: " + this.amount_of_node_per_layer + "\n" +
                ">> Nodes array: " + this.nodes;
    }
}
