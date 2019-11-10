package NeuralNetJAVA;

public class Main {

    public static void main(String[] args) {
        Integer[] inputs = {1,2,3};
        Network network = new Network();
        System.out.println(network);
        Double MSE;

        for (int i = 0; i < 500; i++) {
            network.trainNetwork();
            network.printMSE();
        }
    }
}