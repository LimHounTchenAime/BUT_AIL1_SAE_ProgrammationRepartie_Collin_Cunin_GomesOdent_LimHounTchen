package org.example.proxy;

import java.rmi.Remote;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ServiceProxy implements Remote {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/database_name";
    private static final String DB_USERNAME = "username";
    private static final String DB_PASSWORD = "password";

    private static void registerRestaurant(String serviceName, String address, double longitude, double latitude) throws SQLException {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD)) {
            String query = "INSERT INTO restaurant (name, address, longitude, latitude) VALUES (?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, serviceName);
            statement.setString(2, address);
            statement.setDouble(3, longitude);
            statement.setDouble(4, latitude);
            statement.executeUpdate();

            System.out.println("Service enregistré dans la base de données.");
        }
    }
}
