package fourinarowbot.domain;

import java.io.Serializable;

import commons.gameengine.Coordinate;

public class Marker implements Serializable {

    private MarkerColor color;
    private Coordinate  coordinates;

    private Marker() {
        // For JSON-serialization
    }

    public Marker(final MarkerColor color, final Coordinate coordinates) {
        this.color = color;
        this.coordinates = coordinates;
    }

    public MarkerColor getColor() {
        return color;
    }

    public Coordinate getCoordinates() {
        return coordinates;
    }
}
