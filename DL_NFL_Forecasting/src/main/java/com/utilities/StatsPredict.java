package main.java.com.utilities;

/*
 * Utilize prediction stats for evaluation.
 */

import main.java.com.deepNeuralNetwork.Matrix;

public class StatsPredict 
{
	private int truePositives;
    private int falsePositives;
    private int trueNegatives;
    private int falseNegatives;
    private double precision;
    private double recall;
    private double accuracy;
    private double f1;

    public StatsPredict(Matrix Y, Matrix Yhat) 
    {
    	// Initial instantiation
        int m = Y.cols();
        truePositives = 0;
        falsePositives = 0;
        trueNegatives = 0;
        falseNegatives = 0;
        
        for (int col = 0; col < m; col++) 
        {
            double y = Y.get(0, col);
            double yhat = Yhat.get(0, col);
            if(y == 1 && yhat == 1) 
            {
                truePositives++;
            } else if(y == 0 && yhat == 1) 
            {
                falsePositives++;
            } else if(y == 1 && yhat == 0) 
            {
                falseNegatives++;
            } else {
                trueNegatives++;
            }
        }
        
        precision = (double)truePositives / (truePositives + falsePositives);
        recall = (double)truePositives / (truePositives + falseNegatives);
        accuracy = (double)(truePositives + trueNegatives) / (truePositives + trueNegatives + falsePositives + falseNegatives);
        f1 = 2 * (precision * recall) / (precision + recall);
    }

    public static double computeAccuracy(Matrix Y, Matrix Yhat) 
    {
        int m = Y.cols();
        int K = Y.rows();

        double totalGood = Matrix.eqEW(Y, Yhat).sum();
        double totalPoints = K * m;
        return totalGood / totalPoints;
    }

    public int getTruePositives() 
    {
        return truePositives;
    }

    public int getFalsePositives() 
    {
        return falsePositives;
    }

    public int getTrueNegatives() 
    {
        return trueNegatives;
    }

    public int getFalseNegatives() 
    {
        return falseNegatives;
    }

    public double getPrecision() 
    {
        return precision;
    }

    public double getRecall() 
    {
        return recall;
    }

    public double getAccuracy() 
    {
        return accuracy;
    }

    public double getF1() 
    {
        return f1;
    }

    @Override
    public String toString() 
    {
        StringBuilder sb = new StringBuilder(50);
        sb.append("Accuracy: ").append(accuracy * 100f);
        sb.append(", Precision: ").append(precision * 100f);
        sb.append(", Recall: ").append(recall * 100f);
        sb.append(", F1: ").append(f1 * 100f);
        return sb.toString();
    }
}
