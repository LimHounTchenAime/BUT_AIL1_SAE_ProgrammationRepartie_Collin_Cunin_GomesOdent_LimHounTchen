package org.example.service;

import org.example.db.Reservation;
import org.example.db.Restaurant;
import org.json.JSONArray;

import java.rmi.Remote;
import java.util.ArrayList;

public class ServiceRestaurant implements Remote, InterfaceServiceRestaurant {
    public JSONArray recupererRestaurants(){

        ArrayList<Restaurant> restaurants = Restaurant.findAll();
        return new JSONArray(restaurants);
    }
    public void reserverTable(String nom, String prenom, int nbPers, String tel) {

        Reservation r = new Reservation(nom, prenom, nbPers, tel);
        r.save();
    }
}
