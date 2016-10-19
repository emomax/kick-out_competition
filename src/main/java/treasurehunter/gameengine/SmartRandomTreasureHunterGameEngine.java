package treasurehunter.gameengine;

import java.util.Random;

import commons.gameengine.board.PlayerColor;
import commons.gameengine.board.Coordinate;
import treasurehunter.TreasureHunterApplication;
import treasurehunter.board.Tile;
import treasurehunter.board.TreasureHunterBoard;
import treasurehunter.domain.Move;
import treasurehunter.domain.Orientation;

import static treasurehunter.board.Tile.TileState;

public class SmartRandomTreasureHunterGameEngine extends TreasureHunterGameEngine {
    @Override
    public Move getNextMove(final TreasureHunterBoard board, final PlayerColor myColor) {
        Tile me = findMe(board, myColor);
        return getRandomMove(me, board);
    }

    private Tile findMe(TreasureHunterBoard board, PlayerColor myColor) {
        System.out.println("Looking for my color! - " + myColor.toString());
        for (int i = 0; i < board.getNumberOfCols(); i++) {
            for (int j = 0; j < board.getNumberOfRows(); j++) {
                if (board.getTile(i, j).getState() == TileState.valueOf(myColor.toString())) {
                    System.out.println("Found me!");
                    return board.getTile(i, j);
                }
            }
        }

        return null;
    }

    public Move getRandomMove(Tile me, TreasureHunterBoard board) {
        Orientation          myDirection = me.getOrientation();

        Coordinate nextPosition = new Coordinate(
                me.getCoordinates().getX() + myDirection.xDirection(),
                me.getCoordinates().getY() + myDirection.yDirection());

        Random  rng           = new Random();
        int     selection     = rng.nextInt(6);
        boolean shouldGoForth = !board.isOutsideBoard(nextPosition) && selection > 1;


        if (shouldGoForth) {
            return Move.MOVE_FORWARD;
        }
        else if (selection == 0) {
            return Move.ROTATE_LEFT;
        }
        else if (selection == 1) {
            return Move.ROTATE_RIGHT;
        }
        else {
            return Move.ROTATE_LEFT;
        }
    }

    // Run this main to start the game
    public static void main(final String[] args) {
        final TreasureHunterApplication treasureHunterApplication = new TreasureHunterApplication(new SmartRandomTreasureHunterGameEngine());

        // Run game once
        treasureHunterApplication.runGameOnce();
    }
}
