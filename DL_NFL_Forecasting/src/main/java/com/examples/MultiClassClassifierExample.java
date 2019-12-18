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
 * An example of a multi-class classifier
 * 
 * Author: Dylan Lasher
 */

public class MultiClassClassifierExample 
{
	public static void main(String[] args) 
	{
        new MultiClassClassifierExample().run();
    }
    
    private void run() 
    {
        long randSeed = 1234;
        int labelsCount = 4;
        int samplesCount = 1000;

        /**
         * Create N random points between (-1, 1) distributed in 4 groups, one for each quadrant:
         * 0: x < 0, y < 0
         * 1: x > 0, y < 0
         * 2: x < 0, y > 0
         * 3: x > 0, y > 0
         */
        List<Item> trainSet = new ArrayList<>();
        Random rand = new Random(randSeed);
        for (int i = 0; i < samplesCount; i++) 
        {
            double px = -1 + rand.nextFloat() * 2;
            double py = -1 + rand.nextFloat() * 2;
            int label = (px < 0 ? 0 : 1) + (py < 0 ? 0 : 2);
            trainSet.add(new Item(new double[]{px, py}, label));
        }

        // Test the set (samples of each group)
        List<Item> testSet = new ArrayList<>(Arrays.asList(
                //0: x < 0, y < 0
                new Item(new double[]{-0.1f, -1}, 0),
                new Item(new double[]{-0.3f, -0.4f}, 0),

                //1: x > 0, y < 0
                new Item(new double[]{0.1f, -0.7f}, 1),
                new Item(new double[]{0.3f, -0.9f}, 1),

                //2: x < 0, y > 0
                new Item(new double[]{-0.1f, 1}, 2),
                new Item(new double[]{-0.3f, 0.4f}, 2),

                //3: x > 0, y > 0
                new Item(new double[]{0.1f, 0.7f}, 3),
                new Item(new double[]{0.3f, 0.9f}, 3)
        ));

        // Print sample distributions
        System.out.println("Train set diversity:");
        Utils.printSamplesDiversity(trainSet);
        System.out.println("Test set diversity:");
        Utils.printSamplesDiversity(testSet);

        // Convert to X,Y matrices
        Matrix trainX = Item.toX(trainSet);
        Matrix trainY = Item.toYoneHot(trainSet, labelsCount);
        Matrix testX = Item.toX(testSet);
        Matrix testY = Item.toYoneHot(testSet, labelsCount);

        // Train classifier
        deepNeuralNetwork classifier = new deepNeuralNetwork(
                randSeed,
                new int[]{2, 10, 10, labelsCount}, // network layers
                128, //mini batch size
                2000, // epochs ToDo - Stop when no significant improvement
                0.075f, // learning rate
                0.015f, // L2 lambda regularization
                deepNeuralNetwork.RELU, // Hidden layers activation function (always this)
                deepNeuralNetwork.SOFTMAX, // Output layer activation function (always this)
                deepNeuralNetwork.MULTI_CLASS_CROSS_ENTROPY // Loss function
        );
        classifier.train(trainX, trainY, trainX, trainY, true);

        // Predict, train, and test set
        Matrix trainYpred = classifier.predict(trainX);
        Matrix testYpred = classifier.predict(testX);
        System.out.println("Train set performance: " + StatsPredict.computeAccuracy(trainY, trainYpred) * 100f);
        System.out.println("Test set performance: " + StatsPredict.computeAccuracy(testY, testYpred) * 100f);
        
    }
}
