package org.example.proxy;

import com.sun.net.httpserver.*;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URL;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Proxy {
    private static final int PROXY_PORT = 8000;
    private static final int RMI_PORT = 1099;

    public static void main(String[] args) throws IOException {
        // Création d'un serveur HTTP
        HttpServer server = HttpServer.create(new InetSocketAddress(PROXY_PORT), 0);

        // Définition du gestionnaire de requêtes
        server.createContext("/", new ProxyHandler());

        // Démarrage du serveur HTTP
        server.start();

        System.out.println("Le serveur proxy écoute sur le port " + PROXY_PORT);

        // Enregistrement du service RMI
        try {
            ServiceProxy sP = new ServiceProxy();
            Registry registryProxy = LocateRegistry.createRegistry(RMI_PORT);
            registryProxy.rebind("ServiceProxy", sP);

            System.out.println("Le service RMI est enregistré sur le port " + RMI_PORT);
        } catch (RemoteException e) {
            System.out.println("Erreur lors de l'enregistrement du service RMI : " + e.getMessage());
        }
    }
}
