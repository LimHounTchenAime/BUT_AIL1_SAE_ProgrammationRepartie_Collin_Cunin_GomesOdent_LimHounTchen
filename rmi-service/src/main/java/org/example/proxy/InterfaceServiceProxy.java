package org.example.proxy;

import org.example.restaurant.InterfaceServiceRestaurant;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface InterfaceServiceProxy extends Remote {
    void enregistrerRestaurant(InterfaceServiceRestaurant r) throws RemoteException;
    void enregistrerEtablissement() throws RemoteException;
}
