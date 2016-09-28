package fourinarowbot.gameengine;

import fourinarowbot.board.Board;
import fourinarowbot.domain.Coordinates;
import fourinarowbot.domain.MarkerColor;

public interface GameEngine {

    Coordinates getCoordinatesForNextMakerToPlace(Board board, MarkerColor myColor);
}
