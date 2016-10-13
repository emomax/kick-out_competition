package treasurehunter.gameengine;

import commons.gameengine.Action;
import commons.gameengine.Coordinate;
import treasurehunter.domain.Direction;

public class Move implements Action {
    Coordinate coordinate;
    Direction direction;

    public void setCoordinate(Coordinate coordinate) {
        this.coordinate = coordinate;
    }

    @Override
    public Coordinate getCoordinates() {
        return coordinate;
    }

    @Override
    public String getMethod() {
        return "move";
    }

    public void setMove(Direction direction) {
        this.direction = direction;
    }

    public Direction getMove() {
        return direction;
    }
}
