package org.example.restaurant;

import org.example.proxy.InterfaceServiceRestaurant;

import java.rmi.Remote;

public interface InterfaceServiceProxy extends Remote {
    void enregistrerRestaurant(InterfaceServiceRestaurant r);
    void enregistrerEtablissement();
}
