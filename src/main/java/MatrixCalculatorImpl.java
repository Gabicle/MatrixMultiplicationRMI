import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class MatrixCalculatorImpl extends UnicastRemoteObject implements MatrixCalculator {

    public MatrixCalculatorImpl() throws Exception{

    }

    @Override
    public Matrix multiply(Matrix a, Matrix b) throws RemoteException {
        double[][] matA = a.getVals();
        double[][] matB = b.getVals();

        int row = a.getRow();
        int col = b.getColumn();


        double[][] val = new double[row][col];

        for (int i=0; i < row; i++){
            for (int j=0; j< col; j++){
                for (int k=0; k<a.getColumn(); k++){
                    val[i][j] += matB[k][j] * matA[i][k];
                }
            }
        }

        return new Matrix(val);
    }

    @Override
    public Matrix multiply(Matrix a, double b) throws RemoteException {
        double[][] valsA = a.getVals();

        int row = a.getRow();
        int column = a.getColumn();

        double[][] values = new double[row][column];

        for (int i = 0 ; i<row ; i++){
            for (int j = 0 ; j<column ; j++){
                values[i][j] = valsA[i][j] * b;
            }
        }

        return new Matrix(values);
    }
}
