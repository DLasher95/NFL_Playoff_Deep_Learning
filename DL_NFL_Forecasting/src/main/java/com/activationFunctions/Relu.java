package main.java.com.activationFunctions;

/*
 * An implementation of the Relu activation function.
 * 
 * Author: Dylan Lasher
 */

import main.java.com.deepNeuralNetwork.Matrix;

public class Relu implements ActivationFunctions 
{
	@Override
    public Matrix forward(Matrix Z) 
	{
        return Z.relu();
    }

    @Override
    public Matrix backward(Matrix dA, Matrix Z) 
    {
        //Create mask where non-positive values are 0
        Matrix mask = Z.greater(0f);

        return dA.mulEW(mask);
    }
}
