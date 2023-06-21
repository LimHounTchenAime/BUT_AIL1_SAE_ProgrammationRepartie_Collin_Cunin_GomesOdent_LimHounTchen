import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class DataClient {
    public static void main(String[] args) throws Exception {
        Registry registry = LocateRegistry.getRegistry("localhost", 1099);
        DataService dataService = (DataService) registry.lookup("DataServiceImpl");
        String data = dataService.fetchData();
        System.out.println("Données reçues du serveur : " + data);
    }
}
