package client;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class ClientRestaurant {
    public static void main(String[] args) {
        try {
            //récupérer l'annuaire du SC
            Registry registry = LocateRegistry.getRegistry(args[0], 1099);
            //récupérer la classe du SC
            InterfaceServiceRestaurant sR = (InterfaceServiceRestaurant) registry.lookup("ServiceRestaurant");
            sR.recupereRestaurants();
            sR.reserverTable("Cena", "John", 0, "0675859203");
            //InterfaceServiceCalcul noeudCalcul = (InterfaceServiceCalcul) UnicastRemoteObject.exportObject(serviceCalcul, 0);
        } catch(Exception e){
            System.out.println("Problème client : " + e.getMessage());
        }
    }
}
