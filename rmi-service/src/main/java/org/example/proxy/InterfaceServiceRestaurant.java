package org.example.proxy;

import org.json.JSONArray;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface InterfaceServiceRestaurant extends Remote {
    JSONArray recupererRestaurants() throws RemoteException;
    void reserverTable(String nom, String prenom, int nbConvives, String coordonneesTelephoniques) throws RemoteException;
}
