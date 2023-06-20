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

    static class ProxyHandler implements HttpHandler {
        public void handle(HttpExchange exchange) throws IOException {
            try {
                // Récupération de l'URL demandée
                URI requestURI = exchange.getRequestURI();
                URL targetURL = new URL(requestURI.toString());

                // Création d'une connexion HTTP(S) vers l'URL cible
                HttpURLConnection connection = targetURL.getProtocol().equals("https")
                        ? (HttpsURLConnection) targetURL.openConnection()
                        : (HttpURLConnection) targetURL.openConnection();

                // Transfert des données entre le client et le serveur cible
                try (InputStream inputStream = connection.getInputStream()) {
                    // Récupération du code de réponse et des en-têtes
                    int responseCode = connection.getResponseCode();
                    Headers responseHeaders = exchange.getResponseHeaders();
                    responseHeaders.putAll(connection.getHeaderFields());

                    // Envoi de la réponse au client
                    exchange.sendResponseHeaders(responseCode, 0);
                    try (OutputStream outputStream = exchange.getResponseBody()) {
                        byte[] buffer = new byte[4096];
                        int bytesRead;
                        while ((bytesRead = inputStream.read(buffer)) != -1) {
                            outputStream.write(buffer, 0, bytesRead);
                        }
                    }
                }
            } catch (IOException e) {
                // Gestion des erreurs
                int errorCode = 500;
                String errorMessage = "Erreur interne du serveur: " + e.getMessage();

                exchange.sendResponseHeaders(errorCode, errorMessage.length());
                try (OutputStream outputStream = exchange.getResponseBody()) {
                    outputStream.write(errorMessage.getBytes());
                }
            }
        }

    }
}
