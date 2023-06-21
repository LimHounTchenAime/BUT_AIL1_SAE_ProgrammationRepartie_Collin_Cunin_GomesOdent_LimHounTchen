package org.example.testDonnéesBloquées;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;

public class ServiceHttpClient implements InterfaceServiceHttpClient{
    public Map<String, String> makeHttpRequest(String url) throws RemoteException {
        Map<String, String> response = new HashMap<>();

        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();

        try {
            HttpResponse<String> httpResponse = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            int statusCode = httpResponse.statusCode();
            String contentType = httpResponse.headers().firstValue("Content-Type").orElse("");
            String responseBody = httpResponse.body();

            response.put("statusCode", String.valueOf(statusCode));
            response.put("contentType", contentType);
            response.put("responseBody", responseBody);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            response.put("error", e.getMessage());
        }

        return response;
    }
}
