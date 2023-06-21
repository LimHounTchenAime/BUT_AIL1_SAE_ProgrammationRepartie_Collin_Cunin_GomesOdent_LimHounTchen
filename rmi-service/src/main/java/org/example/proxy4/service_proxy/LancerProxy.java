package org.example.proxy4.service_proxy;

import java.rmi.server.UnicastRemoteObject;
import java.rmi.AccessException;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;


public class LancerProxy {
    public static void main(String[] args) {
        try {
            // Creating service
            Proxy object = new Proxy(args[0]);
            ProxyInterface rd = (ProxyInterface)UnicastRemoteObject.exportObject(object,4553);

            // Creating Registry and adding service
            Registry reg = LocateRegistry.createRegistry(Integer.parseInt(args[0]));
            reg.rebind("distributeur", rd);
            System.out.println("Service and Registry started");
        }
        catch (AccessException a) {
            System.out.println("Failed to access regsitry : " + a.getMessage());
        }
        catch(RemoteException r){
            System.out.println("Error in getting registry" + r.getMessage());
        }
    }
}