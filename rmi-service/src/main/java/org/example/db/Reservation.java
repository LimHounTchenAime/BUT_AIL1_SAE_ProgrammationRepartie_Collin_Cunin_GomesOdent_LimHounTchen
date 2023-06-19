package org.example.db;

import java.sql.*;

public class Reservation {

    private int id;
    private final String nom;
    private final String prenom;
    private final int nbPers;
    private final String tel;

    public Reservation(String nom, String prenom, int nbPers, String tel){
        this.id = -1;
        this.nom = nom;
        this.prenom = prenom;
        this.nbPers = nbPers;
        this.tel = tel;
    }

    public static void createTable(){
        Connection connection = DBConnection.getConnexion();
        try
        {
            assert connection != null;
            connection.createStatement().executeUpdate("""
                    create table reservation(
                        id int PRIMARY KEY NOT NULL AUTO_INCREMENT,
                        nom varchar(50) NOT NULL,
                        prenom varchar(50) NOT NULL,
                        nbPers int NOT NULL,
                        tel varchar(10) NOT NULL
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
            db.createStatement().executeUpdate("DROP TABLE reservation");
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
            String sql = "INSERT INTO reservation (nom, prenom, nbPers, tel) VALUES(?, ?, ?, ?)";
            assert db != null;
            PreparedStatement statement = db.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, this.nom);
            statement.setString(2, this.prenom);
            statement.setInt(3, this.nbPers);
            statement.setString(4, this.tel);
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
            String sql = "UPDATE reservation SET nom = ?, prenom = ?, nbPers = ?, tel = ? WHERE id = ?";
            assert db != null;
            PreparedStatement statement = db.prepareStatement(sql);
            statement.setString(1, this.nom);
            statement.setString(2, this.prenom);
            statement.setInt(3, this.nbPers);
            statement.setString(4, this.tel);
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
}
