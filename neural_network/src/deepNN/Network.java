package NeuralNetJAVA;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Network {

    private ArrayList<Integer> amount_of_node_per_layer = new ArrayList<>();
    private ArrayList<Node> nodes = new ArrayList<>();
    int num_input;


    /*
        Architecture : [ [ ( nodeI, weightI ) ] ]
     */
    public Network() {
        int count_node = 0; // layer 0 => inputs
        Scanner sc = new Scanner(System.in);
        System.out.print("Nodes in layer "+ count_node +" (inputs) (Enter a character to stop): ");
        while ((sc.hasNextInt())) {
            count_node++;
            System.out.print("Nodes in layer "+ count_node +" : ");
            amount_of_node_per_layer.add(sc.nextInt());
        }
        // create the network : n nodes per layer
        int layer_index = 0;
        for (int value: this.amount_of_node_per_layer) {
            System.out.println("** "+value+" nodes created for layer "+layer_index);
            for (int i = 0; i<value; i++){
                Node node = new Node();
                nodes.add(node);
                this.connectNodeToRandomWeight(nodes.get(nodes.size()-1));
            }
            layer_index++;
        }
    }

    /*
        1 weight for each node, random init
     */
    public void connectNodeToRandomWeight(Node node) {
        double weight = Math.random() * (1 - 0);
        node.setWeight(weight);
        System.out.println("    * Poids assigné au node num°"+node.getNode_id()+ " => w="+weight);
    }

    public void printArrayNodes() {
        System.out.println(this.nodes);
    }

    /*
        code => forward training :
            1) sum of input for each hidden layer nodes
            2) back-propagation of weights for each node with square error between
            neural_output and train_output => MSE
     */
    public void trainNetwork() {
        //code
    }

    public ArrayList<Integer> returnNetwork() {
        return this.amount_of_node_per_layer;
    }

    @Override
    public String toString(){
        return "Nodes per layer: " + this.amount_of_node_per_layer + "\n" +
                ">> Nodes array: " + this.nodes;
    }

    public void setAmount_of_node_per_layer(int amount) {
        this.amount_of_node_per_layer.add(amount);
    }

    public ArrayList<Integer> getAmount_of_node_per_layer() {
        return this.amount_of_node_per_layer;
    }
}
