package treasurehunter.board;

import java.io.Serializable;

import commons.gameengine.Coordinate;
import treasurehunter.domain.Orientation;

public class Tile implements Serializable {
    private TileState   state;
    private Coordinate  coordinate;
    private Orientation direction = Orientation.UP;

    private Tile() {

    }

    public Tile(Coordinate coordinate) {
        this(coordinate, TileState.EMPTY);
    }

    public Tile(Coordinate coordinate, TileState state) {
        this.coordinate = coordinate;
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

    public Coordinate getCoordinate() {
        return coordinate;
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
