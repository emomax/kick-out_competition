package treasurehunter.domain;

import commons.gameengine.Action;
import commons.gameengine.Coordinate;
import treasurehunter.domain.Move;

public class TreasureHunterAction implements Action {
    Coordinate                 coordinate;
    treasurehunter.domain.Move move;

    @Override
    public Coordinate getCoordinates() {
        return coordinate;
    }

    @Override
    public String getMethod() {
        return "move";
    }

    public void setMove(Move direction) {
        this.move = direction;
    }

    public Move getMove() {
        return move;
    }
}
