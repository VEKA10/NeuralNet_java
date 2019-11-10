package NeuralNetJAVA;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Network {

    private ArrayList<Integer> amount_of_node_per_layer = new ArrayList<>();
    private ArrayList<Node> nodes;
    private ArrayList<ArrayList> layer = new ArrayList<>();
    private int num_input;
    private String[] activationFunctions = {"sigmoïd","ReLU",
                                            "binaryStepfunc",
                                            "linear","tanH",
                                            "leakyReLU","softmax","swich"};


    /*
        Architecture : [ [ .....,( nodeN ) ], [....], .... ]
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
        for (int k = 0; k < this.amount_of_node_per_layer.size()-1; k++){
            //for (int value: this.amount_of_node_per_layer) {
            nodes = new ArrayList<>(); // to reset the nodes and flush in layer
            System.out.println("** "+this.amount_of_node_per_layer.get(k)+" nodes created for layer "+layer_index);
            for (int i = 0; i<this.amount_of_node_per_layer.get(k); i++){
                // Nbre de weights = currentLayerNodes * nextLayerNodes
                int num_weights = this.amount_of_node_per_layer.get(k);
                int total_num_weights = this.amount_of_node_per_layer.get(k)*this.amount_of_node_per_layer.get(k+1);
                int currentNodeWeightsCount = (int) total_num_weights/num_weights;
                Node node = new Node(currentNodeWeightsCount);
                nodes.add(node);
                //** ============= NODE TO WEIGHTS: 1-N relation =============**//
                for (int j = 0; j < currentNodeWeightsCount; j++){
                    this.connectNodeToRandomWeights(node); //nodes.get(nodes.size()-1)
                }
            }
            layer.add(nodes);
            layer_index++;
        }
    }

    /*
        1 weight for each node, random init
     */
    public void connectNodeToRandomWeights(Node node) {
        double weight = Math.random() * (1 - 0);
        node.setWeight(weight);
        System.out.println("    * Poids assigné au node num°"+node.getNode_id()+ " => w="+weight);
    }

    public void forward() {
        Double output = 0.0;
        System.out.println("**=========================**\nEach Layer : ");
        for (int i = 1; i < this.layer.size(); i++) { //START AT LAYER 1 (NOT INPUTS)
            for (int j = 0; j < this.layer.get(i).size(); j++){ //ALL NODES OF EACH HIDDEN LAYERS
                Node currentHiddenNode = (Node)this.layer.get(i).get(j);
                System.out.println(currentHiddenNode.getNode_id());

                for (int k = 0; k < this.layer.get(i-1).size(); k++) {
                    Node previousLayerNode = (Node)this.layer.get(i-1).get(k);
                    System.out.println(previousLayerNode.get_weights()[j]);
                    //** ============= DOT PRODUCT =============**//
                    Double currentNodeBias = currentHiddenNode.getBias();
                    Double currentNodeInput = 2.0; //previousLayerNode.getInput();
                    output += currentNodeInput * previousLayerNode.get_weights()[j];
                }
                //ACTIVATION : for example Sigmoid
                output = 1/(1+Math.exp(-output));
                currentHiddenNode.setOutput(output);
                System.out.println("\t** Flushed output : "+output);
            }
        }
    }

    /*
        code => forward training :
            1) sum of input for each hidden layer nodes
            2) back-propagation of weights for each node with square error between
            neural_output and train_output => MSE
     */
    public void trainNetwork() {
        // 1) forward => outputs * each weight <=> dot product => famille vectorielle Vect(a1,..., aN)

    }

    public void addActivationFunction(String actFunc) {
        System.out.println("Activation functions you can choose : ");
        for (String func: this.activationFunctions) {
            System.out.println("\t* "+func);
        }
    }

    public ArrayList<Integer> returnNetwork() {
        return this.amount_of_node_per_layer;
    }

    @Override
    public String toString(){
        return "Nodes per layer: " + this.amount_of_node_per_layer + "\n" +
                ">> Layers : " + this.layer;
    }

    /*
    GETTERS || SETTERS
     */
    public void setAmount_of_node_per_layer(int amount) {
        this.amount_of_node_per_layer.add(amount);
    }

    public ArrayList<Integer> getAmount_of_node_per_layer() {
        return this.amount_of_node_per_layer;
    }
}
