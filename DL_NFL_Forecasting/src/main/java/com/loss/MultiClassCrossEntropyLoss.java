package main.java.com.loss;

/*
 * Implementation of multi-Class cross entropy loss function.
 * 
 * Author: Dylan Lasher
 */
import main.java.com.deepNeuralNetwork.Matrix;

public class MultiClassCrossEntropyLoss implements LossFunction
{
	@Override
    public double computeCost(Matrix Y, Matrix AL) 
	{
        int m = Y.cols();

        // Cost = -1/m * sum(Y * log(AL))
        double cost = -1f/m * Y.mulEW(AL.clampToZero().log()).sum();

        return cost;
    }

    @Override
    public Matrix computeCostGradient(Matrix Y, Matrix AL) 
    {
        // Gradient = AL - Y
        return AL.sub(Y);
    }
}
