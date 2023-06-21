package org.example.service_client;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.ServerNotActiveException;


public interface ServiceInterface extends Remote {
    public int catchData(String url) throws RemoteException, ServerNotActiveException;
}