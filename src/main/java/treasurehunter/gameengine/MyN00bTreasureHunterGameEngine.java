package treasurehunter.gameengine;

import commons.gameengine.board.PlayerColor;

import treasurehunter.TreasureHunterApplication;
import treasurehunter.board.TreasureHunterBoard;
import treasurehunter.domain.Move;

public class MyN00bTreasureHunterGameEngine extends TreasureHunterGameEngine {
    public Move getNextMove(final TreasureHunterBoard board, final PlayerColor myColor) {
        // TODO implement logic!
        return null;
    }

    // Run this main to start the game
    public static void main(final String[] args) {
        final TreasureHunterApplication treasureHunterApplication = new TreasureHunterApplication(new MyN00bTreasureHunterGameEngine());

        // Run game once
        treasureHunterApplication.runGameOnce();
    }
}
