package org.example.proxy2.client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class Appel {
    public static void main(String[] args) {
        try {
            // Remplacez l'URL par l'adresse de votre serveur proxy
            URL url = new URL("http://localhost:8000/proxy");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);

            // Écrivez les paramètres de la requête dans le corps
            OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());
            writer.write("serviceName=LeDistributeur&param1=value1");
            writer.flush();

            // Lisez la réponse du serveur proxy
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }

            writer.close();
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
