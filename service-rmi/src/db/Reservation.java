package db;

import java.sql.*;

public class Reservation {

    private int id;
    private String nom;
    private String prenom;
    private int nbPers;
    private String tel;

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

    private void saveNew() {
        try
        {
            Connection db = DBConnection.getConnexion();
            String sql = "INSERT INTO reservation (nom, prenom, nbPers, tel) VALUES(?, ?, ?, ?)";
            assert db != null;
            PreparedStatement statement = db.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, this.nom);
            statement.setString(2, this.prenom);
            statement.setDouble(3, this.nbPers);
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
}
