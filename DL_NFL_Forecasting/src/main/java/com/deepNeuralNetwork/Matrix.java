package main.java.com.deepNeuralNetwork;

/*
 * For linear algebra operations:
 * 
 * An n x M matrix.
 * Data stored in single double array, row by row
 * 
 * Author: Dylan Lasher
 */

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.Random;

public class Matrix 
{
	// Meta essentials
	private static final NumberFormat FORMAT = new DecimalFormat("0.####");
    public static final double EPSILON = 0.000001f;
    
    private final double[] data;
    private final int rows;
    private final int cols;
    
    // Methods and Overloading
    public Matrix(int rows, int cols) 
    {
        if(rows < 1 || cols < 1)
            error("Recheck shape of matrix: (" + rows + ", " + cols + ")");
        this.rows = rows;
        this.cols = cols;
        this.data = new double[rows * cols];
    }
    
    public Matrix(int rows, int cols, double[] data) 
    {
        this(rows, cols);
        if(data.length != this.data.length)
            error("Recheck length of data: " + data.length);
        this.set(data);
    }
    
    public Matrix(double value) 
    {
        this(1, 1, new double[]{value});
    }
    
    public Matrix(double[][] values) 
    {
        this(values.length, values[0].length);
        for (int i = 0; i < values.length; i++) 
        {
            if(values[i].length != this.cols)
                error("Recheck shape: " + Arrays.toString(values));
            this.setRow(i, values[i]);
        }
    }
    
    public static Matrix fromValue(int rows, int cols, double v) 
    {
        Matrix m = new Matrix(rows, cols);
        Arrays.fill(m.data, v);
        return m;
    }
    
    public static Matrix zeros(int rows, int cols) 
    {
        return fromValue(rows, cols, 0);
    }
    
    public static Matrix ones(int rows, int cols) 
    {
        return fromValue(rows, cols, 1);
    }
    
    public static Matrix random(int rows, int cols, long randSeed) 
    {
        return new Matrix(rows, cols).apply(new RandomOp(randSeed));
    }

    public static Matrix columnVec(double[] values) 
    {
        Matrix m = new Matrix(values.length, 1);
        for (int row = 0; row < m.rows; row++) 
        {
            m.set(row, 0, values[row]);
        }
        return m;
    }
    
    
    // Inner-methods
    public void set(int row, int col, double v) 
    {
        this.data[this.pos(row, col)] = v;
    }
    
    private void set(double[] values) 
    {
        System.arraycopy(values, 0, this.data, 0, values.length);
    }
    
    private void setRow(int row, double[] values) 
    {
        System.arraycopy(values, 0, this.data, rowStart(row), this.cols);
    }
    
    private int pos(int row, int col) 
    {
        return row * this.cols + col;
    }
    
    private int rowStart(int row) 
    {
        return row * this.cols;
    }
    
    private int rowEnd(int row) 
    {
        return rowStart(row) + this.cols;
    }
    
    private Matrix emptyCopy() 
    {
        return new Matrix(this.rows, this.cols);
    }
    
    private static void copyRow(Matrix src, int srcRow, Matrix dst, int dstRow) 
    {
        System.arraycopy(src.data, src.rowStart(srcRow), dst.data, dst.rowStart(dstRow), src.cols);
    }
    
    private static void copyColumn(Matrix src, int srcCol, Matrix dst, int dstCol) 
    {
        for (int row = 0; row < src.rows; row++) 
        {
            dst.set(row, dstCol, src.get(row, srcCol));
        }
    }
    
    private static void error(String msg) 
    {
        throw new RuntimeException(msg);
    }
    
    // Getter-Setter Instantiation
    public double get(int row, int col) 
    {
        if(row < 0 || row >= this.rows)
            error("Recheck rows: " + row);
        if(col < 0 || col >= this.cols)
            error("Recheck columns: " + col);
        return this.data[pos(row, col)];
    }
    
    public int rows() 
    {
        return this.rows;
    }
    
    public int cols() 
    {
        return this.cols;
    }
    
    public Matrix apply(ElementWiseOp op) 
    {
        return Matrix.apply(this, op);
    }

    public Matrix apply(ElementWiseBoolOp op) 
    {
        return Matrix.apply(this, op);
    }

    public Matrix mul(double s) 
    {
        return apply(new MulOp(s));
    }
    
    public Matrix add(double s) 
    {
        return apply(new AddOp(s));
    }
    
    public Matrix sub(double s) 
    {
        return apply(new SubOp(s));
    }
    
    public Matrix div(double s) 
    {
        return apply(new DivOp(s));
    }
    
    public Matrix scalarMinus(double s) 
    {
        return apply(new ScalarMinusOp(s));
    }
    
    public Matrix oneMinus() 
    {
        return apply(ScalarMinusOp.ONE_MINUS);
    }
    
    public Matrix log() 
    {
        return apply(LogOp.INSTANCE);
    }

    public Matrix exp() 
    {
        return apply(ExpOp.INSTANCE);
    }
    
    public Matrix sigmoid() 
    {
        return apply(SigmoidOp.INSTANCE);
    }
    
    public Matrix relu() 
    {
        return apply(ReluOp.INSTANCE);
    }
    
    public Matrix greater(double v) 
    {
        return apply(new GreaterOp(v));
    }
    
    public Matrix lower(double v) 
    {
        return apply(new LowerOp(v));
    }

    public Matrix eq(double v, double epsilon) 
    {
        return apply(new EqualsOp(v, epsilon));
    }

    public Matrix eq(double v) 
    {
        return eq(v, EPSILON);
    }
    
    public Matrix pow(double s) 
    {
        return apply(new PowerOp(s));
    }
    
    public Matrix square() 
    {
        return apply(PowerOp.SQ_INSTANCE);
    }
    
    public Matrix sqrt() 
    {
        return apply(SqrtOp.INSTANCE);
    }
    
    public Matrix mul(Matrix m) 
    {
        return Matrix.mul(this, m);
    }
    
    public Matrix add(Matrix m) 
    {
        return Matrix.add(this, m);
    }
    
    public Matrix sub(Matrix m) 
    {
        return Matrix.sub(this, m);
    }
    
    public Matrix mulEW(Matrix m) 
    {
        return Matrix.mulEW(this, m);
    }
    
    public Matrix divEW(Matrix m) 
    {
        return Matrix.divEW(this, m);
    }
    
    public Matrix transpose() 
    {
        return Matrix.transpose(this);
    }
    
    public Matrix broadcastCol(int cols) 
    {
        return Matrix.broadcastCol(this, cols);
    }
    
    public Matrix broadcastRow(int rows) 
    {
        return Matrix.broadcastRow(this, rows);
    }

    public Matrix sumColumns() 
    {
        return Matrix.sumColumns(this);
    }
    
    public Matrix sumRows() 
    {
        return Matrix.sumRows(this);
    }
    
    public double sum() 
    {
        return Matrix.sum(this);
    }

    public Matrix maxPerRow() 
    {
        return Matrix.maxPerRow(this);
    }

    public Matrix maxPerColumn() 
    {
        return Matrix.maxPerColumn(this);
    }

    public Matrix minPerRow() 
    {
        return Matrix.minPerRow(this);
    }

    public Matrix minPerColumn() 
    {
        return Matrix.minPerColumn(this);
    }

    public double max() 
    {
        return Matrix.max(this);
    }

    public double min() 
    {
        return Matrix.min(this);
    }

    public Matrix clamp(double v) 
    {
        return apply(new ClampOp(v));
    }

    public Matrix clampToZero() 
    {
        return apply(ClampOp.CLAMP_ZERO);
    }

    @Override
    public String toString() 
    {
        StringBuilder sb = new StringBuilder(this.data.length * 2);
        sb.append("Shape(").append(this.rows).append(", ").append(this.cols).append(")\n");
        sb.append("[");
        int maxRows = Math.min(this.rows, 6);
        int maxCols = Math.min(this.cols, 10);
        for (int row = 0; row < maxRows; row++) 
        {
            sb.append("[");
            for (int col = 0; col < maxCols; col++) 
            {
                if(col > 0) 
                {
                    sb.append(", ");
                }
                sb.append(FORMAT.format(get(row, col)));
            }
            if(this.cols > maxCols)
                sb.append(", ...");
            sb.append("]");
            if(row < this.rows - 1)
                sb.append("\n");
        }
        if(this.rows > maxRows)
            sb.append("...");
        sb.append("]");
        
        return sb.toString();
    }

    @Override
    public boolean equals(Object obj) 
    {
        if (this == obj) 
        {
            return true;
        }
        if (obj == null) 
        {
            return false;
        }
        if (getClass() != obj.getClass()) 
        {
            return false;
        }
        final Matrix m = (Matrix) obj;
        if(m.cols != this.cols || m.rows != this.rows)
            return false;
        
        return Arrays.equals(this.data, m.data);
    }

    
    
    
    // Static Methods
    public static Matrix broadcastCol(Matrix m, int cols) 
    {
        if(m.cols > 1)
            error("Recheck broadcast, it is not for multiple columns");
        if(cols < 1)
            error("Recheck broadcast number: " + cols);
        
        Matrix r = new Matrix(m.rows, cols);
        for (int col = 0; col < cols; col++) 
        {
            Matrix.copyColumn(m, 0, r, col);
        }
        return r;
    }
    
    public static Matrix broadcastRow(Matrix m, int rows) 
    {
        if(m.rows > 1)
            error("Recheck broadcast, it is not for multiple columns");
        if(rows < 1)
            error("Recheck broadcast number: " + rows);
        
        Matrix r = new Matrix(rows, m.cols);
        for (int row = 0; row < rows; row++) 
        {
            Matrix.copyRow(m, 0, r, row);
        }
        return r;
    }
    
    public static Matrix sumColumns(Matrix m) 
    {
        Matrix r = new Matrix(m.rows, 1);
        for (int row = 0; row < m.rows; row++) 
        {
            double sum = 0;
            for (int col = 0; col < m.cols; col++) 
            {
                sum += m.get(row, col);
            }
            r.set(row, 0, sum);
        }
        return r;
    }
    
    public static Matrix sumRows(Matrix m) 
    {
        Matrix r = new Matrix(1, m.cols);
        for (int col = 0; col < m.cols; col++) 
        {
            double sum = 0;
            for (int row = 0; row < m.rows; row++) 
            {
                sum += m.get(row, col);
            }
            r.set(0, col, sum);
        }
        return r;
    }
    
    public static double sum(Matrix m) 
    {
        double sum = 0;
        for (int row = 0; row < m.rows; row++) 
        {
            for (int col = 0; col < m.cols; col++) 
            {
                sum += m.get(row, col);
            }
        }
        return sum;
    }

    public static Matrix maxPerColumn(Matrix m) 
    {
        Matrix r = new Matrix(1, m.cols);
        for (int col = 0; col < m.cols; col++) 
        {
            double max = Float.NEGATIVE_INFINITY;
            for (int row = 0; row < m.rows; row++) 
            {
                max = Math.max(max, m.get(row, col));
            }
            r.set(0, col, max);
        }
        return r;
    }

    public static Matrix maxPerRow(Matrix m) 
    {
        Matrix r = new Matrix(m.rows, 1);
        for (int row = 0; row < m.rows; row++) 
        {
            double max = Float.NEGATIVE_INFINITY;
            for (int col = 0; col < m.cols; col++) 
            {
                max = Math.max(max, m.get(row, col));
            }
            r.set(row, 0, max);
        }
        return r;
    }

    public static Matrix minPerColumn(Matrix m) 
    {
        Matrix r = new Matrix(1, m.cols);
        for (int col = 0; col < m.cols; col++) 
        {
            double min = Float.POSITIVE_INFINITY;
            for (int row = 0; row < m.rows; row++) 
            {
                min = Math.min(min, m.get(row, col));
            }
            r.set(0, col, min);
        }
        return r;
    }

    public static Matrix minPerRow(Matrix m) 
    {
        Matrix r = new Matrix(m.rows, 1);
        for (int row = 0; row < m.rows; row++) 
        {
            double min = Float.POSITIVE_INFINITY;
            for (int col = 0; col < m.cols; col++) 
            {
                min = Math.min(min, m.get(row, col));
            }
            r.set(row, 0, min);
        }
        return r;
    }

    public static double max(Matrix m) 
    {
        double max = Float.NEGATIVE_INFINITY;
        for (int row = 0; row < m.rows; row++) 
        {
            for (int col = 0; col < m.cols; col++) 
            {
                max = Math.max(max, m.get(row, col));
            }
        }
        return max;
    }

    public static double min(Matrix m) 
    {
        double min = Float.POSITIVE_INFINITY;
        for (int row = 0; row < m.rows; row++) 
        {
            for (int col = 0; col < m.cols; col++) 
            {
                min = Math.min(min, m.get(row, col));
            }
        }
        return min;
    }
    
    public static Matrix apply(Matrix m, ElementWiseOp op) 
    {
        Matrix r = m.emptyCopy();
        for (int row = 0; row < m.rows; row++) 
        {
            for (int col = 0; col < m.cols; col++) 
            {
                r.set(row, col, op.apply(m.get(row, col)));
            }
        }
        return r;
    }
    
    public static Matrix apply(Matrix a, Matrix b, ElementWise2MatOp op) 
    {
        if(!sameShape(a, b))
            error("Recheck shapes-> a: " + a + ", b: " + b);
        
        Matrix r = a.emptyCopy();
        for (int row = 0; row < a.rows; row++) 
        {
            for (int col = 0; col < a.cols; col++) 
            {
                r.set(row, col, op.apply(a.get(row, col), b.get(row, col)));
            }
        }
        return r;
    }

    public static Matrix apply(Matrix m, ElementWiseBoolOp op) 
    {
        Matrix r = m.emptyCopy();
        for (int row = 0; row < m.rows; row++) 
        {
            for (int col = 0; col < m.cols; col++) 
            {
                boolean result = op.apply(m.get(row, col));
                r.set(row, col, result ? 1f : 0f);
            }
        }
        return r;
    }

    public static Matrix apply(Matrix a, Matrix b, ElementWiseBoolMat2Op op) 
    {
        if(!sameShape(a, b))
            error("Recheck shapes-> a: " + a + ", b: " + b);

        Matrix r = a.emptyCopy();
        for (int row = 0; row < a.rows; row++) 
        {
            for (int col = 0; col < a.cols; col++) 
            {
                boolean result = op.apply(a.get(row, col), b.get(row, col));
                r.set(row, col, result ? 1f : 0f);
            }
        }
        return r;
    }
    
    public static Matrix mul(Matrix a, Matrix b) 
    {
        if(a.cols != b.rows)
            error("Recheck shapes-> a: " + a + ", b: " + b);
        
        Matrix c = new Matrix(a.rows, b.cols);
        for (int row = 0; row < a.rows; row++) 
        {
            for (int col = 0; col < b.cols; col++) 
            {
                c.data[c.pos(row, col)] = rowColumnDot(a, row, b, col);
            }
        }
        return c;
    }
    
    private static double rowColumnDot(Matrix a, int row, Matrix b, int col) 
    {
        double dot = 0;
        for (int i = 0; i < a.cols; i++) 
        {
            dot += a.get(row, i) * b.get(i, col);
        }
        return dot;
    }
    
    public static Matrix add(Matrix a, Matrix b) 
    {
        return Matrix.apply(a, b, AddMatOp.INSTANCE);
    }
    
    public static Matrix sub(Matrix a, Matrix b) 
    {
        return Matrix.apply(a, b, SubMatOp.INSTANCE);
    }
    
    public static boolean sameShape(Matrix a, Matrix b) 
    {
        return a.rows == b.rows && a.cols == b.cols;
    }
    
    public static Matrix mulEW(Matrix a, Matrix b) 
    {
        return Matrix.apply(a, b, MulMatEWOp.INSTANCE);
    }
    
    public static Matrix divEW(Matrix a, Matrix b) 
    {
        return Matrix.apply(a, b, DivMatEWOp.INSTANCE);
    }

    public static Matrix greaterEW(Matrix a, Matrix b) 
    {
        return Matrix.apply(a, b, GreaterMatOp.INSTANCE);
    }

    public static Matrix lowerEW(Matrix a, Matrix b) 
    {
        return Matrix.apply(a, b, LowerMatOp.INSTANCE);
    }

    public static Matrix eqEW(Matrix a, Matrix b) 
    {
        return Matrix.apply(a, b, EqualsMatOp.INSTANCE);
    }

    public static Matrix eqEW(Matrix a, Matrix b, double epsilon) 
    {
        return Matrix.apply(a, b, new EqualsMatOp(epsilon));
    }

    public static Matrix transpose(Matrix m) 
    {
        Matrix t = new Matrix(m.cols, m.rows);
        for (int row = 0; row < m.rows; row++) 
        {
            for (int col = 0; col < m.cols; col++) 
            {
                t.set(col, row, m.get(row, col));
            }
        }
        return t;
    }
    
    public static Matrix appendColumns(Collection<Matrix> list) 
    {
        int rows = 0;
        int cols = 0;
        for (Matrix m : list) 
        {
            if(rows == 0) 
            {
                rows = m.rows;
            } else {
                if(m.rows != rows)
                    error("Recheck the number of rows: " + m);
            }
            cols += m.cols;
        }
        
        Matrix r = new Matrix(rows, cols);
        int colIdx = 0;
        for (Matrix m : list) 
        {
            for (int col = 0; col < m.cols; col++) 
            {
                Matrix.copyColumn(m, col, r, colIdx);
                colIdx++;
            }
        }
        return r;
    }
    
    public static Matrix getColumns(Matrix m, int[] indices) 
    {
        if(indices == null || indices.length == 0)
            error("Invalid indices: " + Arrays.toString(indices));
        
        Matrix r = new Matrix(m.rows, indices.length);
        for (int i = 0; i < indices.length; i++) 
        {
            int col = indices[i];
            if(col < 0 || col >= m.cols)
                error("Invalid column index: " + col);
            Matrix.copyColumn(m, col, r, i);
        }
        return r;
    }

    public static Matrix getColumn(Matrix m, int col) 
    {
        return Matrix.getColumns(m, new int[]{col});
    }

    public static Matrix getRows(Matrix m, int[] indices) 
    {
        if(indices == null || indices.length == 0)
            error("Recheck indices: " + Arrays.toString(indices));
        
        Matrix r = new Matrix(indices.length, m.cols);
        for (int i = 0; i < indices.length; i++) 
        {
            int row = indices[i];
            if(row < 0 || row >= m.cols)
                error("Recheck index of row: " + row);
            Matrix.copyRow(m, row, r, i);
        }
        return r;
    }

    public static Matrix getRow(Matrix m, int row) 
    {
        return Matrix.getRows(m, new int[]{row});
    }
    
    
    
    
    // Methods for elements
    public interface ElementWiseOp 
    {
        double apply(double v);
    }
    
    public static class RandomOp implements ElementWiseOp 
    {
        private final Random rand;
        public RandomOp(long randSeed) 
        {
            this.rand = new Random(randSeed);
        }
        
        @Override
        public double apply(double v) 
        {
            return (double)rand.nextGaussian();
        }
    }
    
    public static abstract class ScalarOp implements ElementWiseOp 
    {
        protected final double s;
        public ScalarOp(double s) 
        {
            this.s = s;
        }
    }
    
    public static class MulOp extends ScalarOp 
    {
        public MulOp(double s) 
        {
            super(s);
        }
        @Override
        public double apply(double v) 
        {
            return v * s;
        }
    }
    
    public static class AddOp extends ScalarOp 
    {
        public AddOp(double s) 
        {
            super(s);
        }
        
        @Override
        public double apply(double v) 
        {
            return v + s;
        }
    }
    
    public static class SubOp extends ScalarOp 
    {
        public SubOp(double s) 
        {
            super(s);
        }
        
        @Override
        public double apply(double v) 
        {
            return v - s;
        }
    }
    
    public static class DivOp extends ScalarOp 
    {
        public DivOp(double s) 
        {
            super(s);
        }
        
        @Override
        public double apply(double v) 
        {
            return v / s;
        }
    }

    public static class PowerOp extends ScalarOp 
    {
        public static final ElementWiseOp SQ_INSTANCE = new PowerOp(2);
        public PowerOp(double s) 
        {
            super(s);
        }
        
        @Override
        public double apply(double v) 
        {
            return (double)Math.pow(v, s);
        }
    }
    
    public static class SqrtOp implements ElementWiseOp 
    {
        public static final ElementWiseOp INSTANCE = new SqrtOp();
        
        @Override
        public double apply(double v) 
        {
            return (double)Math.sqrt(v);
        }
        
    }
    
    public static class ScalarMinusOp extends ScalarOp 
    {
        public static final ElementWiseOp ONE_MINUS = new ScalarMinusOp(1);
        public ScalarMinusOp(double s) 
        {
            super(s);
        }
        
        @Override
        public double apply(double v) 
        {
            return s - v;
        }
    }
    
    public static class SigmoidOp implements ElementWiseOp 
    {
        public static final ElementWiseOp INSTANCE = new SigmoidOp();
        
        @Override
        public double apply(double v) 
        {
            return 1f / (1f + (double)Math.exp(-v));
        }
    }
    
    public static class ReluOp implements ElementWiseOp 
    {
        public static final ElementWiseOp INSTANCE = new ReluOp();
        
        @Override
        public double apply(double v) 
        {
            return Math.max(0, v);
        }
    }
    
    public static class LogOp implements ElementWiseOp 
    {
        public static final ElementWiseOp INSTANCE = new LogOp();
        
        @Override
        public double apply(double v) 
        {
            return (double)Math.log(v);
        }
    }

    public static class ExpOp implements ElementWiseOp 
    {
        public static final ElementWiseOp INSTANCE = new ExpOp();
        
        @Override
        public double apply(double v) 
        {
            return (double)Math.exp(v);
        }
    }

    public static class ClampOp extends ScalarOp 
    {
        public static final ElementWiseOp CLAMP_ZERO = new ClampOp(EPSILON);
        public ClampOp(double s) 
        {
            super(s);
        }
        
        @Override
        public double apply(double v) 
        {
            return Math.max(v, s);
        }
    }

    public interface ElementWise2MatOp 
    {
        double apply(double a, double b);
    }
    
    public static class AddMatOp implements ElementWise2MatOp 
    {
        public static final ElementWise2MatOp INSTANCE = new AddMatOp();
        
        @Override
        public double apply(double a, double b) 
        {
            return a + b;
        }
    }
    
    public static class SubMatOp implements ElementWise2MatOp 
    {
        public static final ElementWise2MatOp INSTANCE = new SubMatOp();
        
        @Override
        public double apply(double a, double b) 
        {
            return a - b;
        }
    }
    
    public static class MulMatEWOp implements ElementWise2MatOp 
    {
        public static final ElementWise2MatOp INSTANCE = new MulMatEWOp();
        
        @Override
        public double apply(double a, double b) 
        {
            return a * b;
        }
    }
    
    public static class DivMatEWOp implements ElementWise2MatOp 
    {
        public static final ElementWise2MatOp INSTANCE = new DivMatEWOp();
        
        @Override
        public double apply(double a, double b) 
        {
            return a / b;
        }
    }

    public interface ElementWiseBoolOp 
    {
        boolean apply(double v);
    }

    public static abstract class ScalarBoolOp implements ElementWiseBoolOp 
    {
        protected final double s;
        public ScalarBoolOp(double s) 
        {
            this.s = s;
        }
    }

    public static class GreaterOp extends ScalarBoolOp 
    {
        public GreaterOp(double s) 
        {
            super(s);
        }
        
        @Override
        public boolean apply(double v) 
        {
            return v > s;
        }
    }

    public static class LowerOp extends ScalarBoolOp 
    {
        public LowerOp(double s) 
        {
            super(s);
        }
        
        @Override
        public boolean apply(double v) 
        {
            return v < s;
        }
    }

    public static class EqualsOp implements ElementWiseBoolOp 
    {
        private final double epsilon;
        private final double s;
        public EqualsOp(double s, double epsilon) 
        {
            this.epsilon = epsilon;
            this.s = s;
        }
        
        @Override
        public boolean apply(double v) 
        {
            return Math.abs(v - s) < epsilon;
        }
    }

    public interface ElementWiseBoolMat2Op 
    {
        boolean apply(double a, double b);
    }

    public static class GreaterMatOp implements ElementWiseBoolMat2Op 
    {
        public static final ElementWiseBoolMat2Op INSTANCE = new GreaterMatOp();
        
        @Override
        public boolean apply(double a, double b) 
        {
            return a > b;
        }
    }

    public static class LowerMatOp implements ElementWiseBoolMat2Op 
    {
        public static final ElementWiseBoolMat2Op INSTANCE = new LowerMatOp();
        
        @Override
        public boolean apply(double a, double b) 
        {
            return a < b;
        }
    }

    public static class EqualsMatOp implements ElementWiseBoolMat2Op 
    {
        public static final ElementWiseBoolMat2Op INSTANCE = new EqualsMatOp(EPSILON);
        private final double epsilon;
        public EqualsMatOp(double epsilon) 
        {
            this.epsilon = epsilon;
        }
        
        @Override
        public boolean apply(double a, double b) 
        {
            return Math.abs(a - b) < epsilon;
        }
    }
}
