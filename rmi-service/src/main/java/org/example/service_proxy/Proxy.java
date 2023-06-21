package org.example.service_proxy;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.io.OutputStream;
import java.rmi.*;
import java.util.HashMap;
import java.util.Map;
import org.example.service_client.ServiceInterface;

public class Proxy implements ProxyInterface, HttpHandler {

    private final Map<String, ServiceInterface> services = new HashMap<>();

    public Proxy(String registryHost) {
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        // Analysez la requête entrante (par exemple, en extrayant le nom du service RMI et les paramètres)
        Map<String, String> queryParams = parseQueryParameters(httpExchange.getRequestURI().getQuery());
        String serviceName = queryParams.get("serviceName");

        // Appelez le service RMI enregistré et récupérez la réponse
        String response = callRmiService(serviceName);

        // Répondez à la requête HTTP avec la réponse du service RMI
        httpExchange.getResponseHeaders().set("Content-Type", "application/json");
        httpExchange.sendResponseHeaders(200, response.getBytes().length);
        OutputStream os = httpExchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

    private Map<String, String> parseQueryParameters(String query) {
        Map<String, String> queryParams = new HashMap<>();
        if (query != null) {
            for (String param : query.split("&")) {
                String[] keyValue = param.split("=");
                if (keyValue.length > 1) {
                    queryParams.put(keyValue[0], keyValue[1]);
                }
            }
        }
        return queryParams;
    }

    private String callRmiService(String serviceName) {
        ServiceInterface service = services.get(serviceName);
        if (service != null) {
            try {
                int result = service.catchData("test"); //TODO: url to catch data
                return "{\"result\": " + result + "}";
            } catch (Exception e) {
                e.printStackTrace();
                return "{\"error\": \"Failed to call RMI service\"}";
            }
        } else {
            return "{\"error\": \"Service not found\"}";
        }
    }

    @Override
    public synchronized void registerService(String serviceName, ServiceInterface serviceInterface) throws RemoteException {
        synchronized (services){
            services.put(serviceName, serviceInterface);
            System.out.println("Service added: " + serviceName);
        }
    }

    @Override
    public synchronized void deleteService(ServiceInterface serviceInterface) throws RemoteException {
        synchronized (services){
            services.values().remove(serviceInterface);
            System.out.println("Service deleted");
        }
    }
}
