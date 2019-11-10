package NeuralNetJAVA;

public class Main {

    public static void main(String[] args) {
        Network network = new Network();
        System.out.println(network);
        Integer[] inputs = {1,2,3};
        network.trainNetwork();
    }
}