package org.example.service_proxy;

import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.rmi.*;
import java.rmi.registry.*;
import java.rmi.server.*;

public class LancerProxy {
    public static void main(String[] args) {
        try {
            // Creating service
            Proxy object = new Proxy(args[0]);
            ProxyInterface rd = (ProxyInterface) UnicastRemoteObject.exportObject(object, 4553);

            // Creating Registry and adding service
            Registry reg = LocateRegistry.createRegistry(Integer.parseInt(args[0]));
            reg.rebind("distributeur", rd);
            System.out.println("Service and Registry started");

            // Creating and starting HTTP server
            HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
            server.createContext("/proxy", object);
            server.start();
        }
        catch (AccessException a) {
            System.out.println("Failed to access registry : " + a.getMessage());
        }
        catch(RemoteException r){
            System.out.println("Error in getting registry" + r.getMessage());
        }
        catch(IOException e) {
            System.out.println("Error in starting HTTP server" + e.getMessage());
        }
    }
}
