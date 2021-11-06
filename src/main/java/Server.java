import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Server {

    public Server() {
        Registry registry = null;
        try{
            registry = LocateRegistry.createRegistry(1099);
            MatrixCalculator obj = new MatrixCalculatorImpl();
            Naming.rebind("rmi://localhost:1099/MatrixCalculatorService", obj);
            System.out.println("Connected");
            System.out.println("Waiting for client...");

        } catch (Exception e) {
            System.out.println("Trouble: " + e);

        }
    }


    public static void main(String[] args) {
        new Server();
    }

}
