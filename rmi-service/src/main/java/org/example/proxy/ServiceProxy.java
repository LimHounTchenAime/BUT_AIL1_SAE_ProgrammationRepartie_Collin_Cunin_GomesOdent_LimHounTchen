package org.example.proxy;

public class ServiceProxy implements InterfaceServiceProxy {
    InterfaceServiceRestaurant restaurant = null;

    public void enregistrerRestaurant(InterfaceServiceRestaurant r){
        this.restaurant = r;
    }

    public void enregistrerEtablissement(){

    }
}
