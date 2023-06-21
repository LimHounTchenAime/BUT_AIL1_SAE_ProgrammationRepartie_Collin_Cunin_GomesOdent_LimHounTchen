package org.example.proxy4.service_proxy;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ProxyInterface extends Remote {

    /**
     * permet de recuperer un service de calcule
     * @return
     */
    public CalculInterface queryService() throws RemoteException;


    /**
     * permet d'ajouter ou supprimer des ServiceCalcule
     */
    public void registerService(CalculInterface serviceCalcule) throws RemoteException;
    public void deleteService(CalculInterface serviceCalcule) throws RemoteException;
}
