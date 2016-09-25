package fourinarowbot.gameengine;

import fourinarowbot.Board;
import fourinarowbot.domain.Coordinates;

public interface GameEngine {

    Coordinates getCoordinatesForNextMakerToPlace(Board board);
}
