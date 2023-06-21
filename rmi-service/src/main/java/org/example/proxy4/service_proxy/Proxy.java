package org.example.proxy4.service_proxy;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.HashMap;
import java.util.Map;

public class Proxy implements ProxyInterface, HttpHandler {

    private final Map<String, String> services = new HashMap<>();

    public Proxy(String registryHost) {
        // Ajouter les services RMI enregistrés et leurs adresses ici
        services.put("LeDistributeur", registryHost); // "rmi://localhost:1099/LeDistributeur"
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
        String rmiUrl = services.get(serviceName);
        if (rmiUrl != null) {
            try {
                Registry registry = LocateRegistry.getRegistry();
                ServiceDistributeur service = (ServiceDistributeur) registry.lookup(rmiUrl);
                int result = service.method();
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
    public synchronized void registerService(ServiceInterface serviceInterface) throws RemoteException {
        synchronized (services){
            services.add(serviceInterface);
            System.out.println(serviceInterface);
            System.out.println("Service added");
        }


    }

    @Override
    public synchronized void deleteService(ServiceInterface serviceInterface) throws RemoteException {
        synchronized (services){
            services.remove(serviceInterface);
            System.out.println("Service deleted");
        }

    }


}
