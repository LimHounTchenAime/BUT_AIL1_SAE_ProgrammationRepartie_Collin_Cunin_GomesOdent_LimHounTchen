package org.example.proxy;

import java.rmi.Remote;
import java.sql.SQLException;

public interface ServiceProxyInterface extends Remote {
    void registerRestaurant(String serviceName, String address, double longitude, double latitude) throws SQLException;
}
