package org.example.testDonnéesBloquées;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class LancerService {
    public static void main(String[] args) {
        try {
            ServiceHttpClient service = new ServiceHttpClient();
            InterfaceServiceHttpClient serv = (InterfaceServiceHttpClient) UnicastRemoteObject.exportObject(service, 0);
            Registry registry = LocateRegistry.createRegistry(1099);
            registry.rebind("httpClientService", serv);
        } catch (RemoteException e) {
            System.out.println(e.getMessage());;
        }
    }
}
