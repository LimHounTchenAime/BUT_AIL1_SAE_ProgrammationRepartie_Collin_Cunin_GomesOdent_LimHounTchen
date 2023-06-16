import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface RestaurantService extends Remote {
    boolean registerToCentralService(String serviceName) throws RemoteException;
    List<Restaurant> getAllRestaurants() throws RemoteException;
    boolean reserveTable(String nom, String prenom, int nombreConvives, String coordonneesTelephoniques) throws RemoteException;
}
