import java.io.Serializable;
import java.sql.*;

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

    private Restaurant(int id, String nom, String adresse, double latitude, double longitude) {
        this.id = id;
        this.nom = nom;
        this.adresse = adresse;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public int getId() {
        return id;
    }

    public String getNom() {
        return this.nom;
    }

    public String getAdresse() {
        return this.adresse;
    }

    public double getLatitude() {
        return this.latitude;
    }

    public double getLongitude() {
        return this.longitude;
    }

    public static void createTable(){
        Connection connection = DBConnection.getConnexion();
        try
        {
            assert connection != null;
            connection.createStatement().executeUpdate("""
                        create table restaurants(
                        id int(11) NOT NULL AUTO_INCREMENT,
                        nom varchar(40) NOT NULL,
                        adresse varchar(200) NOT NULL,
                        PRIMARY KEY(id))
                        """);
        }
        catch(SQLException e)
        {
            System.out.println("La table existe déjà !");
            e.printStackTrace();
        }

    }

    private void saveNew()
    {
        try
        {
            Connection db = DBConnection.getConnexion();
            String sql = "INSERT INTO restaurants (nom, adresse, latitude, longitude) VALUES(?, ?, ?, ?)";
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
}
