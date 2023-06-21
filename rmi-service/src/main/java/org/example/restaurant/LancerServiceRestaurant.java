package org.example.restaurant;

import org.example.proxy.InterfaceServiceProxy;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class LancerServiceRestaurant {
    public static void main(String[] args) {
        try{
            ServiceRestaurant service = new ServiceRestaurant();
            InterfaceServiceRestaurant sr = (InterfaceServiceRestaurant) UnicastRemoteObject.exportObject(service, 0);
            Registry registryProxy = LocateRegistry.getRegistry(args[0], 1098);
            InterfaceServiceProxy sP = (InterfaceServiceProxy) registryProxy.lookup("ServiceProxy");
            sP.enregistrerRestaurant(sr);
            System.out.println("service restaurant enregistré");
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
}
