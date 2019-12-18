package test;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import main.java.com.deepNeuralNetwork.Matrix;

import static org.junit.Assert.assertEquals;

/**
 * Unit tests for linear algebra functionality in the Matrix class.
 *
 * Author: Dylan Lasher
 */
public class Unit_Test 
{
    
    private static final double EPSILON = 0.0001f;
    
    
    public Unit_Test() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }
    
    @Test
    public void testMatrixMulElementWise() 
    {
        Matrix a = new Matrix(new double[][]{
            {1, 2, 3},
            {4, 5, 6}
        });
        Matrix expected = new Matrix(new double[][]{
                {1, 4, 9},
                {16, 25, 36}
            });
        Matrix res = a.mulEW(a);
        assertEquals(expected, res);
    }
    
    @Test
    public void testMatrixDivElementWise()
    {
        Matrix a = new Matrix(new double[][]{
            {1, 2, 3},
            {4, 5, 6}
        });
        Matrix expected = Matrix.ones(a.rows(), a.cols());
        Matrix res = a.divEW(a);
        assertEquals(expected, res);
    }
    
    @Test
    public void testMulScalar() 
    {
        Matrix a = new Matrix(new double[][]{
            {1, 2, 3},
            {4, 5, 6},
        });
        Matrix res = a.mul(2);
        Matrix expected = new Matrix(new double[][]{
            {2, 4, 6},
            {8, 10, 12},
        });
        assertEquals(expected, res);
    }
    
    @Test
    public void testAddScalar() 
    {
        Matrix a = new Matrix(new double[][]{
            {1, 2, 3},
            {4, 5, 6},
        });
        Matrix res = a.add(10);
        Matrix expected = new Matrix(new double[][]{
            {11, 12, 13},
            {14, 15, 16},
        });
        assertEquals(expected, res);
    }
    
    @Test
    public void testSubScalar() 
    {
        Matrix a = new Matrix(new double[][]{
            {1, 2, 3},
            {4, 5, 6},
        });
        Matrix res = a.sub(10);
        Matrix expected = new Matrix(new double[][]{
            {1-10, 2-10, 3-10},
            {4-10, 5-10, 6-10},
        });
        assertEquals(expected, res);
    }
    
    @Test
    public void testShape() 
    {
        Matrix a = new Matrix(new double[][]{
            {1, 2, 3},
            {4, 5, 6},
            {7, 8, 9},
            {10, 11, 12}
        });
        
        assertEquals(4, a.rows());
        assertEquals(3, a.cols());
        assertEquals(9, a.get(2, 2), EPSILON);
        assertEquals(10, a.get(3, 0), EPSILON);
    }
    
    @Test
    public void testAddMatrix() 
    {
        Matrix a = new Matrix(new double[][]{
            {1, 2, 3},
            {4, 5, 6},
        });
        Matrix res = a.add(a);
        Matrix expected = new Matrix(new double[][]{
            {2, 4, 6},
            {8, 10, 12},
        });
        assertEquals(expected, res);
    }
    
    @Test
    public void testSubMatrix() 
    {
        Matrix a = new Matrix(new double[][]{
            {1, 2, 3},
            {4, 5, 6},
        });
        Matrix res = a.sub(a);
        Matrix expected = Matrix.zeros(a.rows(), a.cols());
        assertEquals(expected, res);
    }
    
    @Test
    public void testMatrixMul() 
    {
        Matrix a = new Matrix(new double[][]{
            {1, 2, 3},
            {4, 5, 6}
        });
        Matrix b = new Matrix(new double[][]{
                {7, 8},
                {9, 10},
                {11, 12},
            });
        Matrix expected = new Matrix(new double[][]{
                {58, 64},
                {139, 154}
            });
        Matrix c = Matrix.mul(a, b);
        assertEquals(expected, c);
    }
    
    @Test
    public void testBrodcastCol() 
    {
        Matrix a = new Matrix(new double[][]{
            {1},
            {1},
            {1},
        });
        Matrix expected = new Matrix(new double[][]{
                {1, 1, 1},
                {1, 1, 1},
                {1, 1, 1}
            });
        Matrix c = a.broadcastCol(3);
        assertEquals(expected, c);
    }
    
    @Test
    public void testSumColumns() 
    {
        Matrix a = new Matrix(new double[][]{
            {1, 2, 3},
            {4, 5, 6},
            {7, 8, 9},
            {10, 11, 12}
        });
        Matrix res = a.sumColumns();
        assertEquals(4, res.rows());
        assertEquals(1, res.cols());
        assertEquals(1+2+3, res.get(0, 0), EPSILON);
        assertEquals(4+5+6, res.get(1, 0), EPSILON);
        assertEquals(7+8+9, res.get(2, 0), EPSILON);
        assertEquals(10+11+12, res.get(3, 0), EPSILON);
    }
    
    @Test
    public void testTranspose() 
    {
        Matrix a = new Matrix(new double[][]{
            {1, 2, 3},
            {4, 5, 6}
        });
        Matrix expected = new Matrix(new double[][]{
                {1, 4},
                {2, 5},
                {3, 6}
            });
        Matrix res = a.transpose();
        assertEquals(expected, res);
    }
    
    @Test
    public void testGreater() 
    {
        Matrix a = new Matrix(new double[][]{
            {1, 2, 3},
            {4, 5, 6}
        });
        Matrix expected = new Matrix(new double[][]{
            {0, 0, 0},
            {0, 1, 1}
        });
        Matrix res = a.greater(4);
        assertEquals(expected, res);
    }
    
    @Test
    public void testLower() 
    {
        Matrix a = new Matrix(new double[][]{
            {1, 2, 3},
            {4, 5, 6}
        });
        Matrix expected = new Matrix(new double[][]{
            {1, 1, 1},
            {0, 0, 0}
        });
        Matrix res = a.lower(4);
        assertEquals(expected, res);
    }
    
    @Test
    public void testOneMinus() 
    {
        Matrix a = new Matrix(new double[][]{
            {1, 2, 3},
            {4, 5, 6}
        });
        Matrix expected = new Matrix(new double[][]{
            {1-1, 1-2, 1-3},
            {1-4, 1-5, 1-6}
        });
        Matrix res = a.oneMinus();
        assertEquals(expected, res);
    }
    
    @Test
    public void testLog() 
    {
        Matrix a = Matrix.fromValue(1, 1, 10);
        Matrix expected = Matrix.fromValue(1, 1, (double)Math.log(10));
        Matrix res = a.log();
        assertEquals(expected, res);
    }
    
    @Test
    public void testSigmoid() 
    {
        Matrix a = Matrix.fromValue(1, 1, 10);
        Matrix expected = Matrix.fromValue(1, 1, 1 / (1 + (double)Math.exp(-10)));
        Matrix res = a.sigmoid();
        assertEquals(expected, res);
    }
    
    @Test
    public void testRelu() 
    {
        Matrix a = new Matrix(new double[][]{
            {-30, -10, -1},
            {1, 5, 30}
        });
        Matrix expected = new Matrix(new double[][]{
            {0, 0, 0},
            {1, 5, 30}
        });
        Matrix res = a.relu();
        assertEquals(expected, res);
    }
    
}
