import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ClientDonnees {
    public static void main(String[] args) throws Exception {
        Registry registry = LocateRegistry.getRegistry("localhost", 1099);
        InterfaceServiceDonnees interfaceServiceDonnees = (InterfaceServiceDonnees) registry.lookup("ServiceDonnees");
        String data = interfaceServiceDonnees.fetchData();
        System.out.println("Données reçues du serveur : " + data);
    }
}
