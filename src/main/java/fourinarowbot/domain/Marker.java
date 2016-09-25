package fourinarowbot.domain;

public class Marker {

    private final MarkerColor color;
    private final Coordinates coordinates;

    public Marker(final MarkerColor color, final Coordinates coordinates) {
        this.color = color;
        this.coordinates = coordinates;
    }

    public MarkerColor getColor() {
        return color;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }
}
