package fourinarowbot.gameengine;

import fourinarowbot.board.Board;
import fourinarowbot.domain.Coordinates;

public interface GameEngine {

    Coordinates getCoordinatesForNextMakerToPlace(Board board);
}
