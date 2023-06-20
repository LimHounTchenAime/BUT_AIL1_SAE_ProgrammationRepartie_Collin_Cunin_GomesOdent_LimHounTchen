package org.example.proxy;

public class ServiceProxy implements ServiceProxyInterface {
    InterfaceServiceRestaurant restaurant = null;

    public void enregistrerRestaurant(InterfaceServiceRestaurant r){
        this.restaurant = r;
    }

    public void enregistrerEtablissement(){

    }
}
