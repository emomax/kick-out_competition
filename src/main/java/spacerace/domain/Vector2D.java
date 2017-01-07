package spacerace.domain;

import java.io.Serializable;

public class Vector2D implements Serializable {
    private double x;
    private double y;

    public Vector2D() {
        // For JSON conversion
    }

    public Vector2D(final double x, final double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public void setX(final double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(final double y) {
        this.y = y;
    }

    @Override
    public String toString() {
        return "[" + x + "," + y + "]";
    }
}
