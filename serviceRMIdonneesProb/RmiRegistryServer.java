import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RmiRegistryServer {
    public static void main(String[] args) throws Exception {
        // Création du Registry sur le port 1099
        Registry registry = LocateRegistry.createRegistry(1099);
        
        // Enregistrement du service RMI
        DataService dataService = new DataServiceImpl();
        registry.rebind("DataServiceImpl", dataService);
        
        System.out.println("Serveur RMI en écoute sur le port 1099...");
    }
}
