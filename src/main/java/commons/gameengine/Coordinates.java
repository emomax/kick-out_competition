package commons.gameengine;

import java.io.Serializable;

public class Coordinates implements Serializable {

    private int x;
    private int y;

    private Coordinates() {
        // For JSON-serialization
    }

    public Coordinates(final int x, final int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public String toString() {
        return "Coordinates{" +
               "x=" + x +
               ", y=" + y +
               '}';
    }
}
