package main.java.com.examples;

import main.java.com.deepNeuralNetwork.deepNeuralNetwork;
import main.java.com.deepNeuralNetwork.Matrix;
import main.java.com.utilities.Utils;
import main.java.com.utilities.StatsPredict;
import main.java.com.utilities.Item;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/*
 * An example of a binary classifier
 * 
 * Author: Dylan Lasher
 */

public class BinaryClassifierExample 
{
	public static void main(String[] args) 
	{
        new BinaryClassifierExample().run();
    }
    
    private void run() 
    {
        long randSeed = 1234;
        int samplesCount = 1000;

        /**
         * Create N random points between (-1, 1) distributed in 2 groups:
         * 0: x < 0
         * 1: x > 0
         */
        List<Item> trainSet = new ArrayList<>();
        Random rand = new Random(randSeed);
        for (int i = 0; i < samplesCount; i++) 
        {
            double px = -1 + rand.nextFloat() * 2;
            double py = -1 + rand.nextFloat() * 2;
            int label = px < 0 ? 0 : 1;
            trainSet.add(new Item(new double[]{px, py}, label));
        }

        // Create a test set: a few samples of each group
        // You will input your .arff data with the toolkit.
        List<Item> testSet = new ArrayList<>(Arrays.asList(
                // 0: x < 0
                new Item(new double[]{-0.1f, -1}, 0),
                new Item(new double[]{-0.7f, 1}, 0),

                // 1: x > 0
                new Item(new double[]{0.2f, -1}, 1),
                new Item(new double[]{0.9f, 0.5f}, 1)
        ));

        // Print distribution of samples (both sets)
        System.out.println("Training set diversity:");
        Utils.printSamplesDiversity(trainSet);
        System.out.println("Testing set diversity:");
        Utils.printSamplesDiversity(testSet);

        // Convert to X,Y matrices
        Matrix trainX = Item.toX(trainSet);
        Matrix trainY = Item.toY(trainSet);
        Matrix testX = Item.toX(testSet);
        Matrix testY = Item.toY(testSet);

        // Train binary classifier
        deepNeuralNetwork classifier = new deepNeuralNetwork(
                randSeed,
                new int[]{2, 1}, // network layers
                128, // mini batch size
                2000, // epochs ToDo: Train to stop after no significant improvement
                0.075f, // learning rate
                0.7f, // L2 lambda regularization
                deepNeuralNetwork.RELU, // Hidden layers activation function, this should always be Relu
                deepNeuralNetwork.SIGMOID, // Output layer activation function, this should always be Sigmoid
                deepNeuralNetwork.BINARY_CROSS_ENTROPY // Loss function
        );
        classifier.train(trainX, trainY, trainX, trainY, true);

        // Predict, train, and test set
        Matrix trainYpred = classifier.predict(trainX);
        Matrix testYpred = classifier.predict(testX);
        StatsPredict trainStats = new StatsPredict(trainY, trainYpred);
        StatsPredict testStats = new StatsPredict(testY, testYpred);
        System.out.println("Performance of training set: " + trainStats.toString());
        System.out.println("Performance of testing set: " + testStats.toString());
        
    }
}
