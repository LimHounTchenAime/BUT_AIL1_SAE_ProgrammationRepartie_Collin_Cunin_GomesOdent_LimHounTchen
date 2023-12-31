package org.example.db;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.sql.*;
import java.util.ArrayList;

public class Restaurant implements Serializable {

    private int id;
    private final String nom;
    private final String adresse;
    private final double latitude;
    private final double longitude;

    public Restaurant(String nom, String adresse, double latitude, double longitude){
        this.id = -1;
        this.nom = nom;
        this.adresse = adresse;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    private Restaurant(int id, String nom, String adresse, double latitude, double longitude)
    {
        this.id = id;
        this.nom = nom;
        this.adresse = adresse;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public static JSONArray findAll() {
        try {
            JSONArray json = new JSONArray();
            Connection connection = DBConnection.getConnexion();
            assert connection != null;
            Statement s = connection.createStatement();
            ResultSet rs = s.executeQuery("select * from restaurant");
            while (rs.next()) {
                JSONObject objet = new JSONObject();
                objet.put("id", rs.getInt("id"));
                objet.put("nom", rs.getString("nom"));
                objet.put("adresse", rs.getString("adresse"));
                objet.put("latitude", rs.getString("latitude"));
                objet.put("longitude", rs.getString("longitude"));
                json.put(objet);
            }
            return json;
        }
        catch(SQLException e)
        {
            System.out.println("Impossible de récupèrer les restaurants");
            e.printStackTrace();
            return null;
        }
    }

    public static void createTable(){
        Connection connection = DBConnection.getConnexion();
        try
        {
            assert connection != null;
            connection.createStatement().executeUpdate("""
                    create table restaurant(
                        id int PRIMARY KEY NOT NULL AUTO_INCREMENT,
                        nom varchar(50) NOT NULL,
                        adresse varchar(100) NOT NULL,
                        latitude decimal(9,5) NOT NULL,
                        longitude decimal(9,5) NOT NULL
                    )
                    """);
        }
        catch(SQLException e)
        {
            System.out.println("La table existe déjà !");
            e.printStackTrace();
        }

    }

    public static void deleteTable()
    {

        try
        {
            Connection db = DBConnection.getConnexion();
            assert db != null;
            db.createStatement().executeUpdate("DROP TABLE restaurant");
        }
        catch(SQLException e)
        {
            System.out.println("Supression impossible");
            e.printStackTrace();
        }

    }

    private void saveNew() {
        try
        {
            Connection db = DBConnection.getConnexion();
            String sql = "INSERT INTO restaurant (nom, adresse, latitude, longitude) VALUES(?, ?, ?, ?)";
            assert db != null;
            PreparedStatement statement = db.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, this.nom);
            statement.setString(2, this.adresse);
            statement.setDouble(3, this.latitude);
            statement.setDouble(4, this.longitude);
            statement.executeUpdate();
            ResultSet key = statement.getGeneratedKeys();
            key.next();
            this.id = key.getInt(1);
        }
        catch(SQLException e)
        {
            System.out.println("Une erreur est survenu lors de l'insertion");
            e.printStackTrace();
        }
    }

    private void update() {
        try
        {

            Connection db = DBConnection.getConnexion();
            String sql = "UPDATE restaurant SET nom = ?, adresse = ?, latitude = ?, longitude = ? WHERE id = ?";
            assert db != null;
            PreparedStatement statement = db.prepareStatement(sql);
            statement.setString(1, this.nom);
            statement.setString(2, this.adresse);
            statement.setDouble(3, this.latitude);
            statement.setDouble(4, this.longitude);
            statement.setInt(5, this.id);
            statement.executeUpdate();

        }
        catch(SQLException e)
        {
            System.out.println("Une erreur est survenu lors de l'insertion");
            e.printStackTrace();
        }
    }

    public void save() {
        if(this.id == -1)
        {
            this.saveNew();

        }
        else
        {
            this.update();
        }
    }

    @Override
    public String toString() {
        return "Restaurant{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", adresse='" + adresse + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }
}
