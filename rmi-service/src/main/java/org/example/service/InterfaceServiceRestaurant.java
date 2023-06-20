package org.example.service;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface InterfaceServiceRestaurant extends Remote {
    String recupererRestaurants() throws RemoteException;
    void reserverTable(String nom, String prenom, int nbConvives, String coordonneesTelephoniques) throws RemoteException;
}
