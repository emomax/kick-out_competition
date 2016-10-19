package fourinarowbot.gameengine;

import commons.gameengine.board.Board;
import commons.gameengine.board.PlayerColor;
import commons.gameengine.board.Coordinate;
import commons.gameengine.Action;
import commons.gameengine.GameEngine;
import fourinarowbot.board.FourInARowbotBoard;
import fourinarowbot.domain.FourInARowAction;
import fourinarowbot.domain.Marker;

public abstract class FourInARowbotGameEngine implements GameEngine {
    @Override
    public Action<Marker> getNextMove(Board board, PlayerColor myColor) {
        Marker nextMove = new Marker(myColor, getCoordinatesForNextMakerToPlace((FourInARowbotBoard) board, myColor));

        return new FourInARowAction(nextMove);
    }

    public abstract Coordinate getCoordinatesForNextMakerToPlace(final FourInARowbotBoard board, final PlayerColor myColor);
}
