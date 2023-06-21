import java.awt.Color;
import java.io.Serializable;

public class Dessin implements Serializable {
    public int x, y;
    public Color c;

    public String toString(){
        return "Dessin : ("+this.x+", "+this.y+") couleur = "+this.c ; 
    }   
}