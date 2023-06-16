package service;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class LancerServiceCentral {
    public static void main(String[] args) {
        try{
            ServiceRestaurant service = new ServiceRestaurant();
            InterfaceServiceRestaurant sR = (InterfaceServiceRestaurant) UnicastRemoteObject.exportObject(service, 0);
            Registry registry = LocateRegistry.createRegistry(1099);
            registry.rebind("ServiceRaytracing", sR);
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
}
