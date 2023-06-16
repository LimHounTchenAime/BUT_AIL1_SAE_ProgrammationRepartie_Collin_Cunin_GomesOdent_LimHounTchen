import db.Restaurant;

import java.rmi.Naming;
import java.util.List;

public class RMIClient {
    public static void main(String[] args) {
        try {
            RestaurantService restaurantService = (RestaurantService) Naming.lookup("rmi://localhost/RestaurantService");

            // Enregistrement sur le service central
            boolean registered = restaurantService.registerToCentralService("RestaurantService");
            System.out.println("Registered: " + registered);

            // Récupération de toutes les coordonnées de restaurants de Nancy
            List<Restaurant> restaurants = restaurantService.getAllRestaurants();
            for (Restaurant restaurant : restaurants) {
                System.out.println("Nom: " + restaurant.getNom());
                System.out.println("Adresse: " + restaurant.getAdresse());
                System.out.println("Latitude: " + restaurant.getLatitude());
                System.out.println("Longitude: " + restaurant.getLongitude());
                System.out.println("------------------------");
            }

            // Réservation d'une table
            boolean reservationSuccess = restaurantService.reserveTable("NomClient", "PrenomClient", 4, "0123456789");
            System.out.println("Reservation success: " + reservationSuccess);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
