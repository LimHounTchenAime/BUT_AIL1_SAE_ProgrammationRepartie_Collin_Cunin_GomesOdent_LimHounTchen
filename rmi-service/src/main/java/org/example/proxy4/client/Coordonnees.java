import java.io.Serializable;

public class Coordonnees implements Serializable {
    public int x;
    public int y;
    public int l;
    public int h;

    public Coordonnees(int x, int y, int l, int h){
        this.x = x;
        this.y = y;
        this.l = l;
        this.h = h;
    }
}
