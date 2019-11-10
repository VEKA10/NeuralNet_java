package NeuralNetJAVA;

public class Node {

    private Double input;
    private Double output;
    private Double[] weights;
    private Double bias;
    private static Integer id = 0;
    private Integer node_id;

    {
        this.id++;
        this.node_id = this.id;
    }

    public Node(int num_weights) {
        // on connait le nombre d'output <=> nombre de weights par node
        this.weights = new Double[num_weights];
        this.bias = 1.0;
    }

    @Override
    public String toString() {
        String stringWeights = "";
        for (double w: this.weights) {
            stringWeights += String.valueOf(w)+" || ";
        }
        return "\n\t\t\tNode: "+"id: "+this.node_id + " || "+
                "weight: "+stringWeights;
    }

    /*
    GETTERS || SETTERS
     */
    public void setInput(Double input) {
        this.input = input;
    }

    public Double getInput() {
        return this.input;
    }

    public Double getOutput() {
        return this.output;
    }

    public void setOutput(Double output){
        this.output = output;
    }

    public void setBias(double bias) {
        this.bias = bias;
    }

    public Double getBias() {
        return this.bias;
    }

    public void setWeight(double w) {
        // add weight to last memory case
        for (int i = 0; i < this.weights.length; i++){
            if (this.weights[i] == null) { //dès que case est NULL => flush weight value and OUT
                this.weights[i] = w;
                break;
            }
        }
    }

    public Double[] get_weights() {
        return this.weights;
    }

    public Integer getNode_id() {
        return this.node_id;
    }
}

