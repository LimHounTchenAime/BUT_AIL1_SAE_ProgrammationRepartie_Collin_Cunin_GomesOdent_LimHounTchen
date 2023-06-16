import db.Restaurant;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

public class RestaurantServiceImpl extends UnicastRemoteObject implements RestaurantService {
    private final List<Restaurant> restaurantList;

    public RestaurantServiceImpl() throws RemoteException {
        super();
        restaurantList = new ArrayList<>();
    }

    @Override
    public boolean registerToCentralService(String serviceName) throws RemoteException {
        // Implémentation de l'enregistrement sur le service central
        // Retourne true si l'enregistrement a réussi, sinon false
        throw new Error("TODO");
    }

    @Override
    public List<Restaurant> getAllRestaurants() throws RemoteException {
        return restaurantList;
    }

    @Override
    public boolean reserveTable(String nom, String prenom, int nombreConvives, String coordonneesTelephoniques) throws RemoteException {
        // Implémentation de la réservation de table
        // Retourne true si la réservation a réussi, sinon false
        throw new Error("TODO");
    }
}
