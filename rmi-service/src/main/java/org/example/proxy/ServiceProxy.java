package org.example.proxy;

import java.io.Serializable;

public class ServiceProxy implements org.example.restaurant.InterfaceServiceProxy, Serializable {
    org.example.restaurant.InterfaceServiceRestaurant restaurant = null;

    public void enregistrerRestaurant(org.example.restaurant.InterfaceServiceRestaurant r){
        this.restaurant = r;
    }

    public void enregistrerEtablissement(){

    }
}
