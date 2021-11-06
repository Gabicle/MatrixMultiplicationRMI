import java.io.Serializable;

public class Matrix implements Serializable {
    private int row;
    private int column;
    private double[][] vals;

    public Matrix(double[][] val) {
        this.vals = val;
        row = val.length;
        column = val[0].length;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public double[][] getVals() {
        return vals;
    }

    public void setVals(double[][] vals) {
        this.vals = vals;
    }
}
