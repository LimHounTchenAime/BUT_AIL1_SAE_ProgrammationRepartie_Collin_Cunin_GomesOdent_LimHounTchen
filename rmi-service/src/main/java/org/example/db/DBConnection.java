package org.example.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBConnection
{

    private Connection connection;
    private static DBConnection instance;


    public static Connection getConnexion() {
        try
        {
            if(DBConnection.getInstance().connection == null){ DBConnection.instance = new DBConnection();}
            return DBConnection.getInstance().connection;
        }
        catch(SQLException e)
        {
            System.out.println("Impossible de se connecter à la base de donnée !");
            e.printStackTrace();
        }
        return null;
    }

    public static DBConnection getInstance() throws SQLException {
        if(DBConnection.instance == null){ DBConnection.instance = new DBConnection();}
        return DBConnection.instance;
    }

    private DBConnection() throws SQLException {

        DBConnection.instance = this;
        DBConnection.connect();

    }


    private static void connect() throws SQLException {

        // variables a modifier en fonction de la base
        String userName = "cunin57u";
        String password = "aled475";
        //Attention, sous MAMP, le port est 8889
        String portNumber = "3306";

        String dbName = "cunin57u";

        Properties connectionProps = new Properties();
        connectionProps.put("user", userName);
        connectionProps.put("password", password);
        String urlDB = "jdbc:mariadb://webetu.iutnc.univ-lorraine.fr:" + portNumber + "/" + dbName;
        DBConnection.getInstance().connection = DriverManager.getConnection(urlDB, connectionProps);
    }


}