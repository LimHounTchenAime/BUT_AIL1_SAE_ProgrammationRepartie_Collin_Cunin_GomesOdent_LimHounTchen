package org.example.client;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ClientRestaurant {
    public static void main(String[] args) {
        try {
            Registry registry = LocateRegistry.getRegistry(args[0], 1099);
            InterfaceServiceRestaurant sr = (InterfaceServiceRestaurant) registry.lookup("ServiceRestaurant");
            String restaurants = sr.recupererRestaurants();
            System.out.println("resto : " + restaurants);
            sr.reserverTable("Cena", "John", 0, "0675859203");
        } catch(Exception e){
            System.out.println("Problème client : " + e.getMessage());
            e.printStackTrace();
        }
    }
}
