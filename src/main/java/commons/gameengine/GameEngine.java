package commons.gameengine;

import commons.board.Board;
import commons.board.PlayerColor;

public interface GameEngine {
    Action getNextMove(Board board, PlayerColor color);
}
