package org.example.service;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class LancerServiceCentral {
    public static void main(String[] args) {
        try{
            ServiceRestaurant service = new ServiceRestaurant();
            InterfaceServiceRestaurant sr = (InterfaceServiceRestaurant) UnicastRemoteObject.exportObject(service, 0);
            Registry registry = LocateRegistry.createRegistry(1099);
            registry.rebind("ServiceRestaurant", sr);
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
}
