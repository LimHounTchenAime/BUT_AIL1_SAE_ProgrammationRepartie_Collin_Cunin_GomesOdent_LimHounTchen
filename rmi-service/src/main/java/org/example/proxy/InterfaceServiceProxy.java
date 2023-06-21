package org.example.proxy;

import org.example.restaurant.InterfaceServiceRestaurant;

import java.rmi.Remote;

public interface InterfaceServiceProxy extends Remote {
    void enregistrerRestaurant(InterfaceServiceRestaurant r);
    void enregistrerEtablissement();
}
