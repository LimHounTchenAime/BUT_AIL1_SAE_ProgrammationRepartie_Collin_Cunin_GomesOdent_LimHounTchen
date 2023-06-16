import java.io.Serializable;

public record Restaurant(String nom, String adresse, double latitude, double longitude) implements Serializable {

}
