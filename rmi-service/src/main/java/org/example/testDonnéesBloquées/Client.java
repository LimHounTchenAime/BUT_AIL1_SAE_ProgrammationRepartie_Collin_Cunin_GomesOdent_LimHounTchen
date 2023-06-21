package org.example.testDonnéesBloquées;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Map;

public class Client {
    public static void main(String[] args) {
        try {
            Registry registry = LocateRegistry.getRegistry("localhost", 1099);
            InterfaceServiceHttpClient httpClientService = (InterfaceServiceHttpClient) registry.lookup("httpClientService");

            String url = "https://www.data.gouv.fr/fr/datasets/r/5fb6d2e3-609c-481d-9104-350e9ca134fa";
            Map<String, String> response = httpClientService.makeHttpRequest(url);

            System.out.println("Status Code: " + response.get("statusCode"));
            System.out.println("Content-Type: " + response.get("contentType"));
            System.out.println("Response Body:");
            System.out.println(response.get("responseBody"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
