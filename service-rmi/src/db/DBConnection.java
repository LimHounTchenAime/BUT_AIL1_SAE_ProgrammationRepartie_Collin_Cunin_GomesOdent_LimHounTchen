package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBConnection
{

    private Connection connection;
    private static DBConnection instance;

    private String nomDB;




    public static Connection getConnexion() {
        try
        {
            if(DBConnection.getInstance().connection == null){ DBConnection.instance = new DBConnection();}
            return DBConnection.getInstance().connection;
        }
        catch(SQLException e)
        {
            System.out.println("Impossible de ce connecter à la base de donnée !");
            e.printStackTrace();
        }
        return null;
    }

    public static DBConnection getInstance() throws SQLException {
        if(DBConnection.instance == null){ DBConnection.instance = new DBConnection();}
        return DBConnection.instance;
    }

    public void setNomDB(String nomDB)
    {
        this.nomDB = nomDB;
        try {
            DBConnection.connect(this);
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
    }

    private DBConnection() throws SQLException {



        DBConnection.instance = this;
        this.nomDB = "restaurants";
        DBConnection.connect(DBConnection.instance);

    }


    private static void connect(DBConnection instance) throws SQLException {

        // variables a modifier en fonction de la base
        String userName = "root";
        String password = "root";
        String serverName = "localhost";
        //Attention, sous MAMP, le port est 8889
        String portNumber = "3306";
        String tableName = "restaurant";

        // iL faut une base nommee testPersonne !
        String dbName = DBConnection.getInstance().nomDB;

        Properties connectionProps = new Properties();
        connectionProps.put("user", userName);
        connectionProps.put("password", password);
        String urlDB = "jdbc:mysql://" + serverName + ":";
        urlDB += portNumber + "/" + dbName;
        DBConnection.getInstance().connection = DriverManager.getConnection(urlDB, connectionProps);
    }


}