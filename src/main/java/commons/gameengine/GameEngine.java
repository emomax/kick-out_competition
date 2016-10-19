package commons.gameengine;

import commons.gameengine.Action;
import commons.network.server.Parameterable;
import commons.gameengine.board.Board;
import commons.gameengine.board.PlayerColor;

public interface GameEngine {
    Action<? extends Parameterable> getNextMove(Board board, PlayerColor color);
}
