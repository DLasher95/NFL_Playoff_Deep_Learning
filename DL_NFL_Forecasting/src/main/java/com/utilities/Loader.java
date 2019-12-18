package main.java.com.utilities;

/*
 * Load the data - ToDo: Adjust for .arff dataset, this is for practice.
 * 				   ToDo: Create implement normalization functionality
 */
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Loader
{
    public static List<Item> loadData() 
    {
    	// ***************************************
    	// Remember to change the path to the data for .arff implementaiton
        String xPath = "path of data";
        String yPath = "path of data";
        return Loader.loadData(xPath, yPath);
    }

    public static List<Item> loadData(String xPath, String yPath) 
    {
        List<Item> list = new ArrayList<>();

        InputStream inputX = Loader.class.getClass().getResourceAsStream(xPath);
        InputStream inputY = Loader.class.getClass().getResourceAsStream(yPath);
        if(inputX == null || inputY == null) 
        {
            return list;
        }

        BufferedReader rx = null;
        BufferedReader ry = null;
        try {
            rx = new BufferedReader(new InputStreamReader(inputX));
            ry = new BufferedReader(new InputStreamReader(inputY));
            String lineX, lineY;
            while((lineX = rx.readLine()) != null) 
            {
                lineX = lineX.trim();
                if(lineX.isEmpty())
                    continue;
                lineY = ry.readLine();

                String[] values = lineX.split(",");
                int label = Integer.parseInt(lineY.trim());
                if(label == 10) 
                {
                    label = 0;
                }
                double[] data = new double[values.length];
                for (int i = 0; i < values.length; i++) 
                {
                    data[i] = Float.parseFloat(values[i]);
                }
                list.add(new Item(data, label));
            }

            return list;

        } catch (Exception e) 
        {
            throw new RuntimeException("Error, recheck data file: " + xPath + " and " + yPath, e);
        } finally {
            if(rx != null) 
            {
                try {
                    rx.close();
                } catch (Exception e) {
                }
            }
            if(ry != null) 
            {
                try {
                    ry.close();
                } catch (Exception e) {
                }
            }
        }
    }
}
