package main.java.com.utilities;

/*
 * Represents item being used for training/testing
 * 
 * Author: Dylan Lasher
 */

import main.java.com.deepNeuralNetwork.Matrix;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Item 
{
	private final double[] features;
    private final int label;

    public Item(double[] features, int label) 
    {
        this.features = features;
        this.label = label;
    }

    public int getLabel() 
    {
        return label;
    }

    public double[] getFeatures() 
    {
        return features;
    }

    public Matrix toX() 
    {
        return new Matrix(this.features.length, 1, this.features);
    }

    public Matrix toY() 
    {
        return new Matrix(this.label);
    }

    public Matrix toYoneHot(int labels) 
    {
        double[] oneHotVec = Utils.oneHotVec(this.label, labels);
        return Matrix.columnVec(oneHotVec);
    }

    public static Matrix toX(List<Item> items) 
    {
        List<Matrix> list = new ArrayList<>(items.size());
        for (Item item : items) 
        {
            list.add(item.toX());
        }
        return Matrix.appendColumns(list);
    }

    public static Matrix toY(List<Item> items) 
    {
        List<Matrix> list = new ArrayList<>(items.size());
        for (Item item : items) 
        {
            list.add(item.toY());
        }
        return Matrix.appendColumns(list);
    }

    public static Matrix toYoneHot(List<Item> items, int labels) 
    {
        List<Matrix> list = new ArrayList<>(items.size());
        for (Item item : items) 
        {
            list.add(item.toYoneHot(labels));
        }
        return Matrix.appendColumns(list);
    }

    public static Map<Integer, List<Item>> toMap(List<Item> items) 
    {
        Map<Integer, List<Item>> map = new HashMap<>();
        for (Item item : items) 
        {
            List<Item> list = map.get(item.label);
            if(list == null) 
            {
                list = new ArrayList<>();
                map.put(item.label, list);
            }
            list.add(item);
        }
        return map;
    }
}
