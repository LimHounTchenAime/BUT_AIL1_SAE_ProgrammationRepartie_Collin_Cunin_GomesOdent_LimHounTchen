import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class DataServiceImpl extends UnicastRemoteObject implements DataService {
    private StringBuilder restaurantsData;
    private StringBuilder establishmentsData;

    protected DataServiceImpl() throws RemoteException {
        super();
        restaurantsData = new StringBuilder();
        establishmentsData = new StringBuilder();
    }

    @Override
    public void registerRestaurant(String name, String address, String contact) throws RemoteException {
        String restaurantInfo = "Restaurant Name: " + name + "\n" +
                "Address: " + address + "\n" +
                "Contact: " + contact + "\n\n";
        restaurantsData.append(restaurantInfo);
    }

    @Override
    public void registerHigherEducationEstablishment(String name, String address, String contact) throws RemoteException {
        String establishmentInfo = "Establishment Name: " + name + "\n" +
                "Address: " + address + "\n" +
                "Contact: " + contact + "\n\n";
        establishmentsData.append(establishmentInfo);
    }

    @Override
    public String fetchData() throws RemoteException {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://www.data.gouv.fr/fr/datasets/r/5fb6d2e3-609c-481d-9104-350e9ca134fa"))
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            int statusCode = response.statusCode();
            String responseBody = response.body();
            String contentType = response.headers().firstValue("content-type").orElse("");

            return "Status Code: " + statusCode + "\n" +
                    "Response Body: " + responseBody + "\n" +
                    "Content-Type: " + contentType + "\n\n" +
                    "Registered Restaurants:\n" + restaurantsData.toString() + "\n" +
                    "Registered Higher Education Establishments:\n" + establishmentsData.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "Erreur lors de la récupération des données : " + e.getMessage();
        }
    }
}
