package org.example.restaurant;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import org.example.service_proxy.ProxyInterface;

public class LancerServiceRestaurant {
    public static void main(String[] args) {
        try {
            ServiceRestaurant service = new ServiceRestaurant();
            InterfaceServiceRestaurant sr = (InterfaceServiceRestaurant) UnicastRemoteObject.exportObject(service, 0);
            Registry registry = LocateRegistry.createRegistry(1099);
            registry.rebind("ServiceRestaurant", sr);

            Registry registryProxy = LocateRegistry.getRegistry(args[0], 1098);
            ProxyInterface sP = (ProxyInterface) registryProxy.lookup("ServiceProxy");
            sP.registerService("ServiceRestaurant", sr);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
