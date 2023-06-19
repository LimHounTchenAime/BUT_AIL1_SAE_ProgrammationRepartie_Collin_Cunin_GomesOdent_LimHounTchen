package org.example.db;

public class MainCreerDonnees {

    public static void main(String[] args) {

        Restaurant.deleteTable();

        Restaurant.createTable();

        Restaurant[] restaurants = {
                new Restaurant("Restaurant Pidélice",
                        "25 Rue Raymond Poincaré, 54000 Nancy",
                        48.69016,
                        6.17223),
                new Restaurant("Terroirs - Table & Cave",
                        "19 Rue Sergent Blandan, 54000 Nancy",
                        48.68154,
                        6.16620),
                new Restaurant("Le bouche à oreille",
                        "42 Rue des Carmes, 54000 Nancy",
                        48.69084,
                        6.18103),
                new Restaurant("A la Table du Bon Roi Stanislas",
                        "7 Rue Gustave Simon, 54000 Nancy",
                        48.69392,
                        6.18093),
                new Restaurant("Au Coin de la Rue",
                        "19 Pl. du Colonel Fabien, 54000 Nancy",
                        48.69574,
                        6.17951),
        };

        Restaurant r;
        for (Restaurant restaurant : restaurants){
            r = restaurant;
            r.save();
        }

        Reservation.deleteTable();

        Reservation.createTable();

    }

}
