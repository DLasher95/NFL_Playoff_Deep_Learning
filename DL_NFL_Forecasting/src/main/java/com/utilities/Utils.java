package main.java.com.utilities;

/*
 * Utilities to be used later.
 * 
 * Author: Dylan Lasher
 */

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public abstract class Utils 
{
	private Utils(){}

    public static int[] shuffleArray(int n, long randSeed) 
    {
        int[] a = new int[n];
        for (int i = 0; i < n; i++) 
        {
            a[i] = i;
        }
        
        Random rand = new Random(randSeed);
        for (int i = 0; i < n; i++) 
        {
            int j = i + rand.nextInt(n - i);
            int tmp = a[i];
            a[i] = a[j];
            a[j] = tmp;
        }
        return a;
    }

    public static void splitDataSet(List<Item> items, double splitPercentage, long randSeed, List<Item> out1, List<Item> out2) 
    {
        splitDataSet(Item.toMap(items), splitPercentage, randSeed, out1, out2);
    }

    public static void splitDataSet(Map<Integer, List<Item>> itemsPerLabel, double splitPercentage, long randSeed, List<Item> out1, List<Item> out2) 
    {
        Random rand = new Random(randSeed);
        for (int label : itemsPerLabel.keySet()) 
        {
            for (Item item : itemsPerLabel.get(label)) 
            {
                if(rand.nextFloat() < splitPercentage) 
                {
                    out1.add(item);
                } else {
                    out2.add(item);
                }
            }
        }
    }

    public static int minSamplesCount(Map<Integer, List<Item>> itemsPerLabel) 
    {
        int min = Integer.MAX_VALUE;
        for (List<Item> items : itemsPerLabel.values()) 
        {
            min = Math.min(min, items.size());
        }
        return min;
    }

    public static double[] oneHotVec(int label, int totalLabels) 
    {
        double[] vec = new double[totalLabels];
        vec[label] = 1;
        return vec;
    }

    public static Map<Integer, Double> computeSamplesDiversity(List<Item> items) 
    {
        int m = items.size();
        Map<Integer, Double> labelMap = new HashMap<>();
        for (Item item : items) 
        {
        	Double currentObj = labelMap.get(item.getLabel());
            double total = 0;
            if(currentObj != null) 
            {
                total = currentObj;
            }
            labelMap.put(item.getLabel(), total + 1);
        }
        for (Map.Entry<Integer, Double> e : labelMap.entrySet()) 
        {
            e.setValue(e.getValue() / m * 100f);
        }
        return labelMap;
    }

    public static void printSamplesDiversity(List<Item> items) 
    {
        Map<Integer, Double> diversity = computeSamplesDiversity(items);
        
        for (Map.Entry<Integer, Double> e : diversity.entrySet()) 
        {
            System.out.println(" - " + e.getKey() + ": " + e.getValue() + "%");
        }
    }
}
