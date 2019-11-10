package NeuralNetJAVA;

import java.sql.Array;
import java.util.*;

public class Network {

    private ArrayList<Integer> amount_of_node_per_layer = new ArrayList<>();
    private ArrayList<Node> nodes;
    private ArrayList<ArrayList> layer = new ArrayList<>();
    private int num_input;
    private String activationFunc;
    private String[] activationFunctions =  {"sigmoid","ReLU","tanH"};


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

    public Double forward() {
        Double output = 0.0;
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
                    Double currentNodeInput = 2.0; //previousLayerNode.getInput();
                    output += currentNodeInput * previousLayerNode.get_weights()[j];
                }
                //ACTIVATION : for example Sigmoid

                output = useActivationFunction("sigmoid", output);
                currentHiddenNode.setOutput(output);
                System.out.println("\t** Flushed output : "+output);
            }
            System.out.println("**=========================**");
        }
        // return output to calculate MSE
        return output;
    }

    /*
        code => forward training :
            1) sum of input for each hidden layer nodes
            2) back-propagation of weights for each node with square error between
            neural_output and train_output => MSE
     */
    public void trainNetwork(Integer...inputs) {
        Double output = forward();
        System.out.println("Network Output : "+output);
    }

    public Double MSEError(Double netOutput, Double trainOutput) {
        /*
        Si plusieurs netOutput => faire la somme des erreurs
         */
        Double error = trainOutput - netOutput;
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
            case "sigmoid":
                return 1/(1+Math.exp(-val));
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
