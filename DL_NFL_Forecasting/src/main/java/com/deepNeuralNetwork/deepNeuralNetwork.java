package main.java.com.deepNeuralNetwork;

/*
 * Implementation of a deep neural network
 * with H hidden layers.
 * 
 * Author: Dylan Lasher
 */

import main.java.com.activationFunctions.ActivationFunctions;
import main.java.com.activationFunctions.Relu;
import main.java.com.activationFunctions.Sigmoid;
import main.java.com.activationFunctions.Softmax;
import main.java.com.loss.BinaryCrossEntropyLoss;
import main.java.com.loss.LossFunction;
import main.java.com.loss.MultiClassCrossEntropyLoss;
import main.java.com.utilities.Utils;
import main.java.com.utilities.StatsPredict;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class deepNeuralNetwork 
{
	public static final ActivationFunctions RELU = new Relu();
    public static final ActivationFunctions SIGMOID = new Sigmoid();
    public static final ActivationFunctions SOFTMAX = new Softmax();
    public static final LossFunction BINARY_CROSS_ENTROPY = new BinaryCrossEntropyLoss();
    public static final LossFunction MULTI_CLASS_CROSS_ENTROPY = new MultiClassCrossEntropyLoss();

    private final int[] layerDims;
    private final long randSeed;
    private final int miniBatchSize;
    private final int iterations;
    private final double learningRate;
    private final double lambda;
    private final ActivationFunctions hiddenActivationFunc;
    private final ActivationFunctions outputActivationFunc;
    private final LossFunction lossFunction;
    private Map<String, Matrix> parameters;

    /**
     * Create neural network
     * @param randSeed 
     * @param layerDims - array for each layer - includes all layers
     * @param miniBatchSize - mini batch size for gradient decent
     * @param iterations - number of epochs
     * @param learningRate 
     * @param lambda L2 
     * @param hiddenActivationFunc - activation function for hidden layers
     * @param outputActivationFunc - activation function for the output layer
     * @param lossFunction - loss function for output layer
     */
    public deepNeuralNetwork(long randSeed, int[] layerDims, int miniBatchSize, int iterations, double learningRate,
                             double lambda, ActivationFunctions hiddenActivationFunc, ActivationFunctions outputActivationFunc,
                             LossFunction lossFunction) 
    {
        this.layerDims = layerDims;
        this.randSeed = randSeed;
        this.miniBatchSize = miniBatchSize;
        this.iterations = iterations;
        this.learningRate = learningRate;
        this.lambda = lambda;
        this.hiddenActivationFunc = hiddenActivationFunc;
        this.outputActivationFunc = outputActivationFunc;
        this.lossFunction = lossFunction;
    }

    /**
     * Train samples
     * @param X features
     * @param Y labels
     * @param printCost - true if you want to print cost during each iteration
     */
    public void train(Matrix TrainX, Matrix TrainY, Matrix ValX, Matrix ValY,boolean debug) 
    {
        // Initialization
        long currentSeed = randSeed;
        this.parameters = initializeParameters(this.layerDims, currentSeed);
        
        // Gradient descent
        List<CacheItem> caches = new ArrayList<>(this.layerDims.length - 1);
        Map<String, Matrix> grads = new HashMap<>(this.layerDims.length - 1);
        Map<String, Matrix> bestParameters = new HashMap<>(this.parameters);
        List<MiniBatch> miniBatches = new ArrayList<>(TrainX.cols() / this.miniBatchSize + 1);
//        double mse = Float.MAX_VALUE;
        double accuracy = 0;
        boolean improving = true;
        int TimesNotImproving = 0;
        int i= 0;
        while (improving) 
        {
            grads.clear();
            miniBatches.clear();
            
            // Create mini batches
            currentSeed += 1;
            randomMiniBatches(TrainX, TrainY, this.miniBatchSize, currentSeed, miniBatches);
            
            // Iterate through all mini batches
            double cost = Float.MAX_VALUE;
            for (MiniBatch miniBatch : miniBatches) 
            {
                caches.clear();
                
                // Propagate forward
                Matrix AL = modelForward(miniBatch.X, this.parameters, caches, this.hiddenActivationFunc, this.outputActivationFunc);

                // Cost calculation
                cost = computeCost(AL, miniBatch.Y, this.lambda, this.parameters, this.lossFunction);

                // Propagate backward
                grads = modelBackward(AL, miniBatch.Y, caches, grads, this.lambda, this.lossFunction, this.hiddenActivationFunc, this.outputActivationFunc);

                // Update parameters
                updateParameters(this.parameters, grads, this.learningRate);
            }
            
//            double currentMSE = computeMSE(ValX,ValY);
//            if(mse < currentMSE) {
//            	if(TimesNotImproving >= this.iterations){
//            		improving = false;
//            	}
//            	else {
//            		TimesNotImproving++;
//            	}
//            	
//            }
//            else {
//            	mse = currentMSE;
//            	bestParameters = new HashMap<>(this.parameters);
//            	TimesNotImproving = 0;
//            }
            Matrix predict = predict(ValX);
            double currentAcc = StatsPredict.computeAccuracy(ValY,predict);
            if(accuracy >= currentAcc) {
            	if(TimesNotImproving >= this.iterations){
            		improving = false;
            	}
            	else {
            		TimesNotImproving++;
            	}
            	
            }
            else {
            	accuracy = currentAcc;
            	bestParameters = new HashMap<>(this.parameters);
            	TimesNotImproving = 0;
            }
            i++;
            
            // Print cost
            if(debug) 
            {
                if(i % 100 == 0) 
                {
                    System.out.println("Cost after iteration " + i + ": " + cost);
//                    System.out.println("MSE after iteration " + i + ": " + currentMSE);
                    System.out.println("Accuracy after iteration " + i + ": " + currentAcc);
                }
            } 
        }
        this.parameters = bestParameters;
    }
    
    public double computeMSE(Matrix X,Matrix Y) {
    	
        List<CacheItem> caches = new ArrayList<>();
        double sse = 0;
        Matrix AL = modelForward(X, parameters, caches, this.hiddenActivationFunc, this.outputActivationFunc);
        AL.sub(Y);
        for(int col = 0; col < AL.cols(); ++col) {
        	sse += Math.pow(AL.get(0,	col),2);
        }
    	return sse/X.cols();
    }
    // Use trained model and X input to predict Y output
    public Matrix predict(Matrix X) 
    {
        List<CacheItem> caches = new ArrayList<>();
        
        Matrix AL = modelForward(X, parameters, caches, this.hiddenActivationFunc, this.outputActivationFunc);
        Matrix prediction;
        if(AL.rows() == 1) 
        {
            // AL > 0.5
            prediction = AL.greater(0.5f);
        } else {
            Matrix max = AL.maxPerColumn().broadcastRow(AL.rows());
            prediction = Matrix.eqEW(AL, max);
        }

        return prediction;
    }
    
    private Map<String, Matrix> initializeParameters(int[] layerDims, long randSeed) 
    {
        Map<String, Matrix> parameters = new HashMap<>((layerDims.length - 1) * 2);
        for (int l = 1; l < layerDims.length; l++) 
        {
            String layerIdx = String.valueOf(l);
            int rows = layerDims[l];
            int cols = layerDims[l - 1];
            parameters.put("W" + layerIdx, Matrix.random(rows, cols, randSeed).mul(0.01f));
            parameters.put("b" + layerIdx, Matrix.zeros(rows, 1));
        }
        return parameters;
    }
    
    // Forward propagation and AL computation
    private Matrix modelForward(Matrix X, Map<String, Matrix> parameters, List<CacheItem> caches,
                                 ActivationFunctions hiddenActivation, ActivationFunctions outputActivation) 
    {
        Matrix A = X;
        int L = parameters.size() / 2;
        
        // Simple linear activation for all but last layer
        for (int l = 1; l < L; l++) 
        {
            Matrix Aprev = A;
            String layerIdx = String.valueOf(l);
            Matrix W = parameters.get("W" + layerIdx);
            Matrix b = parameters.get("b" + layerIdx);
            A = linearActivationForward(Aprev, W, b, hiddenActivation, caches);
        }
        
        // Simpole linear activation for last layer
        Matrix WL = parameters.get("W" + L);
        Matrix bL = parameters.get("b" + L);
        Matrix AL = linearActivationForward(A, WL, bL, outputActivation, caches);
        
        return AL;
    }
    
    private Matrix linearActivationForward(Matrix A_prev, Matrix W, Matrix b, ActivationFunctions activation, List<CacheItem> caches) 
    {
        Matrix Z = linearForward(A_prev, W, b);
        LinearCache linearCache = new LinearCache(A_prev, W, b);
        
        Matrix A = activation.forward(Z);
        ActivationCache activationCache = new ActivationCache(Z);
        
        caches.add(new CacheItem(linearCache, activationCache));
        return A;
    }
    
    private Matrix linearForward(Matrix A, Matrix W, Matrix b) 
    {
        Matrix WxA = W.mul(A);
        Matrix Z = WxA.add(b.broadcastCol(WxA.cols()));
        return Z;
    }
    
    // Calculate loss
    private double computeCost(Matrix AL, Matrix Y, double lambda, Map<String, Matrix> parameters, LossFunction lossFunction) 
    {
        int m = Y.cols();
        int L = parameters.size() / 2;

        // Calculate cost using loss function
        double crossEntropyCost = lossFunction.computeCost(Y, AL);
        
        // L2 regularization cost
        double l2RegCost = 0;
        for (int l = 1; l < L; l++) 
        {
            Matrix W = parameters.get("W" + l);
            l2RegCost += W.square().sum();
        }
        l2RegCost *= lambda / (2f * m);
        
        // Net cost
        double cost = crossEntropyCost + l2RegCost;
        
        return cost;
    }
    
    // Backpropagation
    private Map<String, Matrix> modelBackward(Matrix AL, Matrix Y, List<CacheItem> caches, Map<String, Matrix> grads,
                                               double lambda, LossFunction lossFunction, ActivationFunctions hiddenActivation,
                                               ActivationFunctions outputActivation) 
    {
        int L = caches.size();
        CacheItem cache;
        String layerIdx;
        BackpropResult res;

        // Calculate gradient of loss function
        Matrix dAL = lossFunction.computeCostGradient(Y, AL);

        // Calculate output layer gradient
        cache = caches.get(L - 1);
        res = linearActivationBackward(dAL, cache, outputActivation, lambda);
        layerIdx = String.valueOf(L);
        grads.put("dA" + layerIdx, res.dA);
        grads.put("dW" + layerIdx, res.dW);
        grads.put("db" + layerIdx, res.db);
        
        // Calculate remaining layer gradients
        for (int l = L - 2; l >= 0; l--) 
        {
            layerIdx = String.valueOf(l + 1);
            cache = caches.get(l);
            Matrix dA_current = grads.get("dA" + (l + 2));
            res = linearActivationBackward(dA_current, cache, hiddenActivation, lambda);
            grads.put("dA" + layerIdx, res.dA);
            grads.put("dW" + layerIdx, res.dW);
            grads.put("db" + layerIdx, res.db);
        }
        
        return grads;
    }
    private BackpropResult linearActivationBackward(Matrix dA, CacheItem cache, ActivationFunctions activation, double lambda) 
    {
        Matrix dZ = activation.backward(dA, cache.activationCache.Z);
        return linearBackward(dZ, cache.linearCache, lambda); 
    }
    private BackpropResult linearBackward(Matrix dZ, LinearCache cache, double lambda) 
    {
        int m = cache.Aprev.cols();
        Matrix dW = dZ.mul(cache.Aprev.transpose()).mul(1f/m).add(cache.W.mul(lambda / m));
        Matrix db = dZ.sumColumns().mul(1f/m);
        Matrix dAprev = cache.W.transpose().mul(dZ);
        
        return new BackpropResult(dAprev, dW, db);
    }
    
    // Use gradient to update parameters
    private void updateParameters(Map<String, Matrix> parameters, Map<String, Matrix> grads, double learningRate) 
    {
        int L = parameters.size() / 2;
        for (int l = 1; l <= L; l++) 
        {
            String layerIdx = String.valueOf(l);
            Matrix W = parameters.get("W" + layerIdx);
            Matrix b = parameters.get("b" + layerIdx);
            Matrix dW = grads.get("dW" + layerIdx);
            Matrix db = grads.get("db" + layerIdx);
            W = W.sub(dW.mul(learningRate));
            b = b.sub(db.mul(learningRate));
            parameters.put("W" + layerIdx, W);
            parameters.put("b" + layerIdx, b);
        }
    }
    
    // Split input into random miniature batches.
    public void randomMiniBatches(Matrix X, Matrix Y, int miniBatchSize, long randSeed, List<MiniBatch> miniBatches) 
    {
        // Shuffle sample indices
        int m = Y.cols();
        int[] indices = Utils.shuffleArray(m, randSeed);
        
        // Assemble mini-batches
        int completeBatches = m / miniBatchSize;
        int[] batchIndices = new int[miniBatchSize];
        for (int i = 0; i < completeBatches; i++) 
        {
            System.arraycopy(indices, i * miniBatchSize, batchIndices, 0, batchIndices.length);
            Matrix batchX = Matrix.getColumns(X, batchIndices);
            Matrix batchY = Matrix.getColumns(Y, batchIndices);
            miniBatches.add(new MiniBatch(batchX, batchY));
        }
        
        // Assemble incomplete batch
        int pendingSize = m % miniBatchSize;
        if(pendingSize != 0) 
        {
            batchIndices = new int[pendingSize];
            System.arraycopy(indices, completeBatches * miniBatchSize, batchIndices, 0, batchIndices.length);
            Matrix batchX = Matrix.getColumns(X, batchIndices);
            Matrix batchY = Matrix.getColumns(Y, batchIndices);
            miniBatches.add(new MiniBatch(batchX, batchY));
        }
    }
    
    private static class CacheItem 
    {
        public final LinearCache linearCache;
        public final ActivationCache activationCache;
        public CacheItem(LinearCache linearCache, ActivationCache activationCache) 
        {
            this.linearCache = linearCache;
            this.activationCache = activationCache;
        }
    }
    
    private static class LinearCache 
    {
        public final Matrix Aprev;
        public final Matrix W;
        public final Matrix b;
        public LinearCache(Matrix Aprev, Matrix W, Matrix b) 
        {
            this.Aprev = Aprev;
            this.W = W;
            this.b = b;
        }
    }
    
    private static class ActivationCache 
    {
        public final Matrix Z;
        public ActivationCache(Matrix Z) 
        {
            this.Z = Z;
        }
    }
    
    private static class BackpropResult 
    {
        public final Matrix dA;
        public final Matrix dW;
        public final Matrix db;
        public BackpropResult(Matrix dA, Matrix dW, Matrix db) 
        {
            this.dA = dA;
            this.dW = dW;
            this.db = db;
        }  
    }
    
    private static class MiniBatch 
    {
        public final Matrix X;
        public final Matrix Y;
        public MiniBatch(Matrix X, Matrix Y) 
        {
            this.X = X;
            this.Y = Y;
        }
        
    }
}
