package fourinarowbot.gameengine;

import fourinarowbot.board.BoardGameBoard;
import commons.gameengine.Coordinates;
import fourinarowbot.domain.MarkerColor;

public interface GameEngine {

    Coordinates getCoordinatesForNextMakerToPlace(BoardGameBoard board, MarkerColor myColor);
}
