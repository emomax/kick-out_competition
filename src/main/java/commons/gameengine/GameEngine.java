package commons.gameengine;

import commons.board.Board;

/**
 * Created by maxjonsson on 2016-10-13.
 */
public interface GameEngine {
    Action getNextMove(Board board);
}
