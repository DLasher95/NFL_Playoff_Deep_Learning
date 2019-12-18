package main.java.com.loss;

/*
 * Interface for loss function
 * 
 * Author: Dylan Lasher
 */

import main.java.com.deepNeuralNetwork.Matrix;

public interface LossFunction 
{
	double computeCost(Matrix Y, Matrix AL);
    Matrix computeCostGradient(Matrix Y, Matrix AL);
}
