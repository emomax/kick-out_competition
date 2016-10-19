package fourinarowbot.domain;

import java.io.Serializable;

import commons.gameengine.board.Coordinate;
import commons.gameengine.Positionable;
import commons.gameengine.board.PlayerColor;
import commons.network.server.Parameterable;

public class Marker implements Positionable, Parameterable, Serializable {

    private PlayerColor color;
    private Coordinate  coordinates;

    private Marker() {
        // For JSON-serialization
    }

    public Marker(final PlayerColor color, final Coordinate coordinates) {
        this.color = color;
        this.coordinates = coordinates;
    }

    public PlayerColor getColor() {
        return color;
    }

    @Override
    public Coordinate getCoordinates() {
        return coordinates;
    }

    @Override
    public String toGetValueString() {
        return "x=" + coordinates.getX() + "&y=" + coordinates.getY();
    }
}
