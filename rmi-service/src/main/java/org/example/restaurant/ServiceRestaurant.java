package org.example.restaurant;

import org.example.db.Reservation;
import org.example.db.Restaurant;
import org.json.JSONArray;

import java.rmi.Remote;

public class ServiceRestaurant implements Remote, InterfaceServiceRestaurant {
    public String recupererRestaurants(){

        JSONArray restaurantsJSON = Restaurant.findAll();
        assert restaurantsJSON != null;
        return restaurantsJSON.toString();
    }
    public void reserverTable(String nom, String prenom, int nbPers, String tel) {

        Reservation r = new Reservation(nom, prenom, nbPers, tel);
        r.save();
    }
}
