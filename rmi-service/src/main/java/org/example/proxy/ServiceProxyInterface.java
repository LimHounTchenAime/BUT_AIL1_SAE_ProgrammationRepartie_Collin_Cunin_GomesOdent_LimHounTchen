package org.example.proxy;

import java.rmi.Remote;
import java.sql.SQLException;

public interface ServiceProxyInterface extends Remote {
    void enregistrerRestaurant(InterfaceServiceRestaurant r);
    void enregistrerEtablissement();
}
