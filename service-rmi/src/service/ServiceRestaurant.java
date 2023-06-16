package service;

import java.rmi.Remote;
import java.sql.*;

public class ServiceRestaurant implements Remote {
    public String recupereRestaurants(){
        String url = "jdbc:mysql://localhost:3306/ma_base_de_donnees";
        String username = "utilisateur";
        String password = "mot_de_passe";

        try {
            // Établir une connexion à la base de données
            Connection conn = DriverManager.getConnection(url, username, password);

            // Créer une requête SQL
            String sql = "SELECT nom, adresse, latitude, longitude FROM restaurants";
            Statement stmt = conn.createStatement();

            // Exécuter la requête
            ResultSet rs = stmt.executeQuery(sql);

            // Créer un StringBuilder pour construire la chaîne JSON manuellement
            StringBuilder jsonBuilder = new StringBuilder();
            jsonBuilder.append("[");

            // Parcourir les résultats de la requête
            while (rs.next()) {
                String nom = rs.getString("nom");
                String adresse = rs.getString("adresse");
                double latitude = rs.getDouble("latitude");
                double longitude = rs.getDouble("longitude");

                // Construire un objet JSON pour chaque restaurant
                jsonBuilder.append("{");
                jsonBuilder.append("\"nom\": \"" + nom + "\",");
                jsonBuilder.append("\"adresse\": \"" + adresse + "\",");
                jsonBuilder.append("\"latitude\": " + latitude + ",");
                jsonBuilder.append("\"longitude\": " + longitude);
                jsonBuilder.append("},");
            }

            // Supprimer la virgule finale
            if (jsonBuilder.length() > 1) {
                jsonBuilder.deleteCharAt(jsonBuilder.length() - 1);
            }

            jsonBuilder.append("]");

            // Fermer les ressources
            rs.close();
            stmt.close();
            conn.close();

            // Convertir le StringBuilder en chaîne JSON finale
            String jsonString = jsonBuilder.toString();

            // Afficher la chaîne JSON
            return jsonString;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public void reserverTable(String nom, String prenom, int nbConvives, String coordonneesTelephoniques) {
        String url = "jdbc:mysql://localhost:3306/ma_base_de_donnees";
        String username = "utilisateur";
        String password = "mot_de_passe";
        try {
            // Établir une connexion à la base de données
            Connection conn = DriverManager.getConnection(url, username, password);

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
