package main.java.com.execute;

import main.java.com.deepNeuralNetwork.deepNeuralNetwork;
import main.java.com.deepNeuralNetwork.Matrix;
import main.java.com.utilities.Utils;
import main.java.com.utilities.StatsPredict;
import main.java.com.utilities.Item;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.io.*;
import java.net.URL;

public class Execute {

	public static void main(String[] args) throws Exception
	{
        Execute main = new Execute();
        main.run();
    }
	
 	final boolean Normalize = true;
 	final long randSeed = 1234;
 	Random rand = new Random(randSeed);
    
	 private void run() throws Exception
	    {
	        MatrixFromToolKit data = GetToolkitMatrixFromArff("main/resources/nflplayoffs-norank.arff", false, true);
	        int numberToTrainStart = 0; 
	        int numberToTrain =  (int)(data.rows() * 0.80);
	        int numberToValidateStart = numberToTrain;
	        int numberToValidate = (int)(data.rows() * 0.10);
	        int numberToTestStart = numberToValidateStart + numberToValidate;
	        int numberToTest = data.rows()-numberToTrain - numberToValidate;
	        
	        MatrixFromToolKit TrainAndValSet = new MatrixFromToolKit(data,numberToTrainStart,0, numberToTrain + numberToValidate, data.cols());
	        TrainAndValSet.shuffle(rand);
	        MatrixFromToolKit TrainSet = new MatrixFromToolKit(TrainAndValSet,numberToTrainStart,0, numberToTrain, data.cols());
	        MatrixFromToolKit ValSet = new MatrixFromToolKit(TrainAndValSet,numberToValidateStart,0, numberToValidate, data.cols());
	        MatrixFromToolKit TestSet = new MatrixFromToolKit(data,numberToTestStart,0, numberToTest, data.cols());
	        // Convert to X,Y matrices
	        Matrix trainX = ConvertMatrix( new MatrixFromToolKit(TrainSet,0,0,TrainSet.rows(),TrainSet.cols()-1));
	        Matrix trainY = ConvertMatrix( new MatrixFromToolKit(TrainSet,0,TrainSet.cols()-1,TrainSet.rows(),1));
	        Matrix valX = ConvertMatrix( new MatrixFromToolKit(ValSet,0,0,ValSet.rows(),ValSet.cols()-1));
	        Matrix valY = ConvertMatrix( new MatrixFromToolKit(ValSet,0,ValSet.cols()-1,ValSet.rows(),1));
	        Matrix testX = ConvertMatrix( new MatrixFromToolKit(TestSet,0,0,TestSet.rows(),TestSet.cols()-1));
	        Matrix testY = ConvertMatrix( new MatrixFromToolKit(TestSet,0,TestSet.cols()-1,TestSet.rows(),1));

	        // Train binary classifier
	        deepNeuralNetwork classifier = new deepNeuralNetwork(
	        		randSeed,
	                new int[]{trainX.rows(),trainX.rows()*2,trainX.rows()*2,trainY.rows()}, // network layers
	                40, //mini batch size
	                100, // epochs ToDo - Stop when no significant improvement
	                0.1, // learning rate
	                0.025, // L2 lambda regularization
	                deepNeuralNetwork.RELU, // Hidden layers activation function (always this)
	                deepNeuralNetwork.SIGMOID, // Output layer activation function
	                deepNeuralNetwork.BINARY_CROSS_ENTROPY // Loss function
	        );
	        classifier.train(trainX,trainY, valX, valY, true);

	        // Predict, train, and test set
	        Matrix trainYpred = classifier.predict(trainX);
	        Matrix valYpred = classifier.predict(valX);
	        Matrix testYpred = classifier.predict(testX);
	        StatsPredict trainStats = new StatsPredict(trainY, trainYpred);
	        StatsPredict valStats = new StatsPredict(valY,valYpred);
	        StatsPredict testStats = new StatsPredict(testY, testYpred);
	        System.out.println("Performance of training set: " + trainStats.toString());
	        System.out.println("Performance of validation set: " + valStats.toString());
	        System.out.println("Performance of testing set: " + testStats.toString());    
	    }
	 
	 public Matrix ConvertMatrix( MatrixFromToolKit Mat) {
		 Matrix output = new Matrix(Mat.cols(), Mat.rows());
		 for(int i =0; i<Mat.rows(); i++) {
			 for(int j=0; j<Mat.cols(); j++) {
				 output.set(j,i,Mat.get(i, j));
			 }
		 }
		 return output;
	 }
	 
	 private MatrixFromToolKit GetToolkitMatrixFromArff(String resourceLocation, boolean randomize, boolean normalize)  throws Exception{
		 ClassLoader classLoader = getClass().getClassLoader();
        URL resource = classLoader.getResource(resourceLocation);
        File file= new File(
      		resource.getFile()
  		);
        MatrixFromToolKit data = new MatrixFromToolKit();
        data.loadArff(file.toString());
        if(randomize) {
            data.shuffle(rand);	
        }
        if(normalize) {
        	data.normalize();
        }
        return data;
	 }
}
