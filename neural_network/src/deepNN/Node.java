package deepNN;

public class Node {

    private Float input;
    private Float output;
    private Double weight;
    private Integer bias;
    private static Integer id = 0;
    private Integer node_id;

    {
        this.id++;
        this.node_id = this.id;
    }

    public Node() {
        this.bias = 1;
    }

    public Float getInput() {
        return this.input;
    }

    public Float getOutput() {
        return this.output;
    }

    public void setOutput(float output){
        this.output = output;
    }

    public void setWeight(double w) {
        this.weight = w;
    }

    public Integer getNode_id() {
        return this.node_id;
    }

    public String toString() {
        return "Node id: "+this.node_id;
    }
}
