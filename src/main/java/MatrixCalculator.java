import java.rmi.Remote;
import java.rmi.RemoteException;

public interface MatrixCalculator extends Remote{
    Matrix multiply(Matrix a, Matrix b) throws RemoteException;
    Matrix multiply(Matrix a, double b) throws RemoteException;
}
