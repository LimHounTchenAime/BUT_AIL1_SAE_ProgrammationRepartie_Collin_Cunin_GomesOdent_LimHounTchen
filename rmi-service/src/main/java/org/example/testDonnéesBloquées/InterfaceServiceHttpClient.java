package org.example.testDonnéesBloquées;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Map;

public interface InterfaceServiceHttpClient extends Remote {
    Map<String, String> makeHttpRequest(String url) throws RemoteException;
}
