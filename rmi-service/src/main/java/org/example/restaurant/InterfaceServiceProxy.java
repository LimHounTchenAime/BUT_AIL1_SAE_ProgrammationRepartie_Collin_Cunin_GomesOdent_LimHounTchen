package org.example.restaurant;

import java.rmi.Remote;

public interface InterfaceServiceProxy extends Remote {
    void enregistrerRestaurant(InterfaceServiceRestaurant r);
    void enregistrerEtablissement();
}
