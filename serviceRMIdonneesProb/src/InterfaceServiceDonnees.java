import java.rmi.Remote;
import java.rmi.RemoteException;

public interface InterfaceServiceDonnees extends Remote {
    void registerRestaurant(String name, String address, String contact) throws RemoteException;
    void registerHigherEducationEstablishment(String name, String address, String contact) throws RemoteException;
    String fetchData() throws RemoteException;
}
