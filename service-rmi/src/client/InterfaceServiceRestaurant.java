package client;

import java.rmi.Remote;

public interface InterfaceServiceRestaurant extends Remote {
    public String recupereRestaurants();
    public void reserverTable(String nom, String prenom, int nbConvives, String coordonneesTelephoniques);
}
