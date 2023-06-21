package org.example.proxy;

import org.example.restaurant.InterfaceServiceRestaurant;

import java.io.Serializable;
import java.rmi.RemoteException;

public class ServiceProxy implements InterfaceServiceProxy, Serializable {
    InterfaceServiceRestaurant restaurant = null;
    @Override
    public void enregistrerRestaurant(InterfaceServiceRestaurant r) throws RemoteException {
        this.restaurant = r;
    }

    @Override
    public void enregistrerEtablissement() throws RemoteException{

    }

    public InterfaceServiceRestaurant getRestaurant(){
        return restaurant;
    }
}
