package main.java.com.loss;

/*
 * Implementation of the binary cross entropy loss function.
 * 
 * Author: Dylan Lasher
 */

import main.java.com.deepNeuralNetwork.Matrix;

public class BinaryCrossEntropyLoss implements LossFunction
{
	 @Override
	    public double computeCost(Matrix Y, Matrix AL) 
	 {
	        int m = Y.cols();

	        // Cross-entropy cost = -1/m * sum(Y * log(AL) + (1-Y) * log(1-AL))
	        double cost = Matrix.add(
	                Matrix.mulEW(Y, AL.clampToZero().log()),
	                Matrix.mulEW(Y.oneMinus(), AL.oneMinus().clampToZero().log())
	        ).sumColumns().mul(-1f/m).get(0,0);

	        return cost;
	    }

	    @Override
	    public Matrix computeCostGradient(Matrix Y, Matrix AL) 
	    {
	        // Loss derivative: dAL = - ((Y / AL) - ((1-Y) / (1-AL)))
	        Matrix dAL = Matrix.divEW(Y, AL).sub(Matrix.divEW(Y.oneMinus(), AL.oneMinus())).mul(-1f);

	        return dAL;
	    }
}
