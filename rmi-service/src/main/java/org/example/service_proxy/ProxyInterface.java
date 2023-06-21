package org.example.service_proxy;

import org.example.service_client.ServiceInterface;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ProxyInterface extends Remote {
    /**
     * permet d'ajouter ou supprimer des ServiceCalcule
     */
    public void registerService(String serviceName, ServiceInterface serviceInterface) throws RemoteException;
    public void deleteService(ServiceInterface serviceCalcule) throws RemoteException;
}
