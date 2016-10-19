package treasurehunter.gameengine;

import commons.gameengine.board.Board;
import commons.gameengine.board.PlayerColor;
import commons.gameengine.Action;
import commons.gameengine.GameEngine;
import treasurehunter.board.TreasureHunterBoard;
import treasurehunter.domain.Move;
import treasurehunter.domain.TreasureHunterAction;

public abstract class TreasureHunterGameEngine implements GameEngine{
    @Override
    public Action<Move> getNextMove(Board board, PlayerColor myColor) {
        Move nextMove = getNextMove((TreasureHunterBoard) board, myColor);

        return new TreasureHunterAction(nextMove);
    }

    protected abstract Move getNextMove(final TreasureHunterBoard board, final PlayerColor color);
}
