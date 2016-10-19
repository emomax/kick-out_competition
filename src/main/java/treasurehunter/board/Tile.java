package treasurehunter.board;

import java.io.Serializable;

import commons.gameengine.board.Coordinate;
import commons.gameengine.Positionable;
import treasurehunter.domain.Orientation;

public class Tile implements Serializable, Positionable {
    private TileState   state;
    private Coordinate  coordinates;
    private Orientation direction = Orientation.UP;

    private Tile() {

    }

    public Tile(Coordinate coordinate) {
        this(coordinate, TileState.EMPTY);
    }

    public Tile(Coordinate coordinate, TileState state) {
        this.coordinates = coordinate;
        this.state = state;
    }

    public TileState getState() {
        return state;
    }

    public void setState(TileState state) {
        this.state = state;
    }

    public void setOrientation(Orientation orientation) {
        this.direction = orientation;
    }

    public Orientation getOrientation() {
        return direction;
    }

    @Override
    public Coordinate getCoordinates() {
        return coordinates;
    }



    public enum TileState
    {
        EMPTY,
        WALL,
        TREASURE,
        RED,
        YELLOW,
    }
}
