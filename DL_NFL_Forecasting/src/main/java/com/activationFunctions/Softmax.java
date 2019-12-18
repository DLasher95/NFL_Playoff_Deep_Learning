package main.java.com.activationFunctions;

import main.java.com.deepNeuralNetwork.Matrix;

/*
 * Implementation of the Softmax activation function.
 * 
 * Author: Dylan Lasher
 */

public class Softmax implements ActivationFunctions
{
	@Override
    public Matrix forward(Matrix Z) 
	{
		Matrix max = Z.maxPerColumn().broadcastRow(Z.rows());
        Matrix expZ = Z.sub(max).exp();
        return expZ.divEW(expZ.sumRows().broadcastRow(expZ.rows()));
    }

    @Override
    public Matrix backward(Matrix dA, Matrix Z) 
    {
        Matrix S = forward(Z);
        Matrix dZ = dA.mulEW(S).mulEW(S.oneMinus());

        return dZ;
    }
}
