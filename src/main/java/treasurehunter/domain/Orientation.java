package treasurehunter.domain;

import java.io.Serializable;

public enum Orientation implements Serializable {
    UP(0, -1),
    DOWN(0, 1),
    LEFT(-1, 0),
    RIGHT(1, 0);

    private int xDirection;
    private int yDirection;

    Orientation(int xDirection, int yDirection) {
        this.xDirection = xDirection;
        this.yDirection = yDirection;
    }

    public int xDirection() {
        return xDirection;
    }

    public int yDirection() {
        return yDirection;
    }

    // For serialization
    public String getOrientation() {
        return this.toString();
    }
}
