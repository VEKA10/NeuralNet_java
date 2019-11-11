package NeuralNetJAVA;

public class Main {

    public static void main(String[] args) {
        Double[] inputs = {0.5,0.3};
        Network network = new Network(inputs);
        System.out.println(network);
        Double MSE;

        for (int i = 0; i < 10000; i++) {
            network.trainNetwork();
        }
    }
}