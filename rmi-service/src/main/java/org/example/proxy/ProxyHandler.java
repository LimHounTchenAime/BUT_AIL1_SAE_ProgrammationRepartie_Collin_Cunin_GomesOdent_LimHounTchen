package org.example.proxy;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

public class ProxyHandler implements HttpHandler {
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
