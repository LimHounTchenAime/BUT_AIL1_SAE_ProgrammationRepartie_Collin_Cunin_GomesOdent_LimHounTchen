package service;

import db.Restaurant;
import org.json.JSONArray;

import java.rmi.Remote;
import java.sql.*;
import java.util.ArrayList;

public class ServiceRestaurant implements Remote {
    public JSONArray recupereRestaurants(){

        ArrayList<Restaurant> restaurants = Restaurant.findAll();

        return new JSONArray(restaurants);
    }
    public void reserverTable(String nom, String prenom, int nbConvives, String coordonneesTelephoniques) {
        try {

            // Créer une requête SQL pour insérer la réservation
            String sql = "INSERT INTO reservations (nom, prenom, nb_convives, coordonnees_telephoniques) VALUES (?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);

            // Définir les paramètres de la requête
            stmt.setString(1, nom);
            stmt.setString(2, prenom);
            stmt.setInt(3, nbConvives);
            stmt.setString(4, coordonneesTelephoniques);

            // Exécuter la requête d'insertion
            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("La réservation a été effectuée avec succès !");
            } else {
                System.out.println("Erreur lors de la réservation. Veuillez réessayer.");
            }

            // Fermer les ressources
            stmt.close();
            conn.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
