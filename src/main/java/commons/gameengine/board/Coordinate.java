package commons.gameengine.board;

import java.io.Serializable;

public class Coordinate implements Serializable {

    private int x;
    private int y;

    private Coordinate() {
        // For JSON-serialization
    }

    public Coordinate(final int x, final int y) {
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
    public boolean equals(Object o) {
        if (o instanceof Coordinate) {
            Coordinate other = (Coordinate) o;
            return other.getX() == x && other.getY() == y;
        }
        return false;
    }

    @Override
    public int hashCode() {
        int tmp = y + (x+1)/2;
        return x + tmp * tmp;
    }

    @Override
    public String toString() {
        return "Coordinate{" +
               "x=" + x +
               ", y=" + y +
               '}';
    }
}
