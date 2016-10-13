package fourinarowbot.gameengine;

import commons.gameengine.Coordinate;
import fourinarowbot.board.BoardGameBoard;
import fourinarowbot.domain.MarkerColor;

public interface GameEngine {

    Coordinate getCoordinatesForNextMakerToPlace(BoardGameBoard board, MarkerColor myColor);
}
