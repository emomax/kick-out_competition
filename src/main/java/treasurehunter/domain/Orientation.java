package treasurehunter.domain;

public enum Orientation {
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
}
