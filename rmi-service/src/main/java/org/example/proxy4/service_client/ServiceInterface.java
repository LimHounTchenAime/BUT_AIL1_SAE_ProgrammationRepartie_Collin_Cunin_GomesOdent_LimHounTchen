package org.example.proxy4.service_client;

import raytracer.Image;
import raytracer.Scene;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.ServerNotActiveException;

public interface ServiceInterface extends Remote
{
    public Image catchData(String url) throws RemoteException, ServerNotActiveException;
}