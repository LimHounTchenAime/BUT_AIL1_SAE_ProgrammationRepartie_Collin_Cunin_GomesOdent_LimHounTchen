package org.example.proxy;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;

public class ProxyHandler implements HttpHandler {
    ServiceProxy service;
    public ProxyHandler(ServiceProxy service){
        this.service = service;
    }
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        // Récupérer les données de la base de données et les convertir en format JSON
        String jsonData = service.getRestaurant().recupererRestaurants();

        // Définir les en-têtes de réponse
        exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
        exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, POST");
        exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type, Authorization");
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.sendResponseHeaders(200, jsonData.getBytes().length);

        // Envoyer les données JSON en réponse
        OutputStream outputStream = exchange.getResponseBody();
        outputStream.write(jsonData.getBytes());
        outputStream.close();
    }

}
