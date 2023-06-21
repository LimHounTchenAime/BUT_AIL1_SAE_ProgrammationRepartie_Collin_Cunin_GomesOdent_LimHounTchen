package org.example.restaurant;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class LancerServiceRestaurant {
    public static void main(String[] args) {
        try{
            ServiceRestaurant service = new ServiceRestaurant();
            InterfaceServiceRestaurant sr = (InterfaceServiceRestaurant) UnicastRemoteObject.exportObject(service, 0);
            Registry registry = LocateRegistry.createRegistry(1099);
            registry.rebind("ServiceRestaurant", sr);
            Registry registryProxy = LocateRegistry.getRegistry(args[0], 1098);
            InterfaceServiceProxy sP = (InterfaceServiceProxy) registry.lookup("ServiceProxy");
            sP.enregistrerRestaurant((org.example.proxy.InterfaceServiceRestaurant) sr);
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
}
