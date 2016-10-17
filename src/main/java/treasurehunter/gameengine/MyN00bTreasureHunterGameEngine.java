package treasurehunter.gameengine;

import commons.board.Board;
import commons.board.PlayerColor;
import commons.gameengine.Action;
import commons.gameengine.GameEngine;

import treasurehunter.TreasureHunterApplication;
import treasurehunter.board.TreasureHunterBoard;
import treasurehunter.domain.TreasureHunterAction;

public class MyN00bTreasureHunterGameEngine implements GameEngine {
    @Override
    public Action getNextMove(final Board board, final PlayerColor myColor) {
        final TreasureHunterBoard  gameBoard = (TreasureHunterBoard) board;
        final TreasureHunterAction whatToDo  = new TreasureHunterAction();

        // TODO implement logic! Remember to set a Move for your Action.

        return whatToDo;
    }

    // Run this main to start the game
    public static void main(final String[] args) {
        final TreasureHunterApplication fourInARowApplication = new TreasureHunterApplication(new MyN00bTreasureHunterGameEngine(), true);

        // Run game once
        fourInARowApplication.runGameOnce();
    }
}
