package main.java.com.activationFunctions;

/*
 * An implementation of the Sigmoid activation function.
 * 
 * Author: Dylan Lasher
 */

import main.java.com.deepNeuralNetwork.Matrix;

public class Sigmoid implements ActivationFunctions
{
    public Matrix forward(Matrix Z) 
	{
        return Z.sigmoid();
    }

    public Matrix backward(Matrix dA, Matrix Z) 
    {
        // S = 1 / (1 + e^(-Z))
        Matrix S = Z.sigmoid();

        // dZ = dA * s * (1-s)
        Matrix dZ = dA.mulEW(S).mulEW(S.oneMinus());

        return dZ;
    }
}
