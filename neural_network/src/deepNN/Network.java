package NeuralNetJAVA;

import java.sql.Array;
import java.util.*;

public class Network {

    private int num_input;
    private Double[] inputs;
    private Double learning_step = 0.5;
    private Double mean_square_error;
    private Double[] trainOutput = {0.8};
    private ArrayList<Integer> amount_of_node_per_layer = new ArrayList<>();
    private ArrayList<Node> nodes;
    private ArrayList<ArrayList> layer = new ArrayList<>();
    private String activationFunc;
    private String[] activationFunctions =  {"sigmoid","ReLU","tanH"};


    /*
        Architecture : [ [ .....,( nodeN ) ], [....], .... ]
     */
    public Network(Double...inputs) {
        // INPUTS INIT
        this.inputs = inputs;

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
        for (int k = 0; k < this.amount_of_node_per_layer.size(); k++){
            nodes = new ArrayList<>(); // to reset the nodes and flush in layer
            System.out.println("** "+this.amount_of_node_per_layer.get(k)+" nodes created for layer "+layer_index);

            //** ============= CREATE N NODES FOR LAYER X =============**//
            for (int i = 0; i < this.amount_of_node_per_layer.get(k); i++){
                // Nbre de weights = currentLayerNodes * nextLayerNodes
                int num_weights = this.amount_of_node_per_layer.get(k);
                int total_num_weights = k < this.amount_of_node_per_layer.size()-1 ?
                        this.amount_of_node_per_layer.get(k)*this.amount_of_node_per_layer.get(k+1) :
                        this.amount_of_node_per_layer.get(k)*this.amount_of_node_per_layer.get(k-1);
                int currentNodeWeightsCount = (int) total_num_weights/num_weights;
                Node node = new Node(currentNodeWeightsCount);
                nodes.add(node);

                // SET INPUTS OF INPUT LAYER => TEST
                if (layer_index == 0){
                    node.setInput(this.inputs[i]);
                    node.setOutput(this.inputs[i]);
                }

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
        System.out.println("\t* Poids assigné au node num°"+node.getNode_id()+ " => w="+weight);
    }

    public Double[] forward() {
        Double output = 0.0;
        int outputIndex = 0;
        int index = this.amount_of_node_per_layer.size()-1;
        Double[] outputs = new Double[this.amount_of_node_per_layer.get(index)];

        System.out.println("HIDDEN LAYERS :\n**=========================**");
        for (int i = 1; i < this.layer.size(); i++) { //START AT LAYER 1 (NOT INPUTS)

            //** ============= EACH HIDDEN LAYER NODES =============**//
            for (int j = 0; j < this.layer.get(i).size(); j++){ //ALL NODES OF EACH HIDDEN LAYERS
                Node currentHiddenNode = (Node)this.layer.get(i).get(j);
                System.out.println("Node "+currentHiddenNode.getNode_id());

                //** ============= EACH PREVIOUS LAYER NODES =============**//
                for (int k = 0; k < this.layer.get(i-1).size(); k++) {
                    Node previousLayerNode = (Node)this.layer.get(i-1).get(k);
                    System.out.println(previousLayerNode.get_weights()[j]);

                    //** ============= DOT PRODUCT =============**//
                    Double currentNodeBias = currentHiddenNode.getBias();
                    // set les values de l'input layer
                    /*
                    Double currentNodeInput = previousLayerNode.getOutput() != null ?
                                            previousLayerNode.getOutput() : 2.0;
                     */
                    Double currentNodeInput = previousLayerNode.getOutput();
                    output += currentNodeInput * previousLayerNode.get_weights()[j];
                }
                // SET INPUT OF NODE => avec ou sans le dot product ?
                currentHiddenNode.setInput(output);

                //ACTIVATION : for example Sigmoid
                output = useActivationFunction("sigmoid", output);
                currentHiddenNode.setOutput(output);
                System.out.println("\t** Flushed output : "+output);

                // IF OUTPUTS LAYER => flush outputs
                if (i == this.layer.size()-1){
                    outputs[outputIndex] = output;
                    outputIndex++;
                }
            }
            System.out.println("**=========================**");
        }
        // LAST LAYER OUTPUTS
        // return output to calculate MSE
        return outputs;
    }

    /*
        Net training :
            * back-propagation of weights for each node with square error between
            neural_output and train_output => try to minimze MSE value
     */
    public void trainNetwork(Integer...inputs) {
        Double[] outputs = forward();
        System.out.println("Network Outputs : ");
        for (Double output: outputs) {
            System.out.println("\t*** "+output);
        }
        Double mse = MSEError(outputs, this.trainOutput);
        /*
            gradient descent => partial derivative => chain rule
            for each node en partant de l'erreur total
            display all network
            start by end of array
        */
        for (int i = this.layer.size()-1; i >= 0; i--){
            for (int j = this.layer.get(i).size()-1; j >= 0; j--){
                Node currentNode = (Node)this.layer.get(i).get(j);

                //**========= EACH WEIGHT OF NODE ==========**//
                for (int k = 0; k < currentNode.get_weights().length; k++){
                    Double updatedWeight = gradientDescentOn(currentNode, mse, k, this.learning_step);
                    currentNode.setWeightsAtIndex(k, updatedWeight);
                }
            }
        }
        System.out.println("New Weights: \n"+this.layer);
        this.mean_square_error = mse;
    }

    private Double gradientDescentOn(Node node, Double mse, int NodeWeightIndex, Double step) {
        Double deltaErrorTotalNetOut = mse;
        Double deltaNetOutNetInput = node.getOutput() * (1 - node.getOutput());
        Double deltaNetInputInputWeight = node.getInput() * node.get_weights()[NodeWeightIndex];
        Double updateWeight = node.get_weights()[NodeWeightIndex] - step * deltaErrorTotalNetOut;
        return updateWeight;
    }

    public Double MSEError(Double[] netOutput, Double[] trainOutput) {
        /*
            Si plusieurs netOutput => faire la somme des erreurs
         */
        Double error = 0.0;
        for (int i = 0; i < trainOutput.length; i++)
        {
            error += trainOutput[i] - netOutput[i];
        }
        Double mse = 0.5*(Math.pow(error, 2));
        return mse;
    }

    public Double useActivationFunction(String actFunc, Double val) {
        /*
        System.out.println("Activation functions you can choose : ");
        for (String func: this.activationFunctions) {
            System.out.println("\t* "+func);
        }
        */
        switch (actFunc.toLowerCase()) {
            case "sigmoid": return 1/(1+Math.exp(-val));
            case "tanh": return Math.atan(val);
            case "relu": return Math.max(0, val);
        }
        return 0.0;
    }

    public ArrayList<Integer> returnNetwork() {
        return this.amount_of_node_per_layer;
    }

    @Override
    public String toString(){
        return "Nodes per layer: " + this.amount_of_node_per_layer + "\n" +
                    ">> Layers : " + this.layer + "\n"+
                    "MSE : "+this.mean_square_error;
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

    public Double getMSE(){
        return this.mean_square_error;
    }

}
