package main.java.com.activationFunctions;

/*
 * Activation function interface
 * 
 * Author: Dylan Lasher
 */

import main.java.com.deepNeuralNetwork.Matrix;

public interface ActivationFunctions 
{
	Matrix forward(Matrix z);
	Matrix backward(Matrix dA, Matrix z);
}