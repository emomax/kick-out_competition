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

        if (!nextIsOK(me, board)) {
            return Move.ROTATE_LEFT;
        }
        else if (treasuresAheadOfMe(me, board)) {
            return Move.MOVE_FORWARD;
        }

        return getRandomMove(me, board);
    }

    private boolean nextIsOK(Tile me, TreasureHunterBoard board) {
        if (me.getOrientation() == Orientation.DOWN) {
            if (me.getCoordinates().getY() != board.getNumberOfRows() - 1 &&
                board.getCells()[me.getCoordinates().getX()][me.getCoordinates().getY() + 1].getState() != TileState.WALL) {
                return true;
            }
        }
        if (me.getOrientation() == Orientation.UP) {
            if (me.getCoordinates().getY() != 0 &&
                board.getCells()[me.getCoordinates().getX()][me.getCoordinates().getY() - 1].getState() != TileState.WALL) {
                return true;
            }
        }
        if (me.getOrientation() == Orientation.RIGHT) {
            if (me.getCoordinates().getX() != board.getNumberOfCols() - 1 &&
                board.getCells()[me.getCoordinates().getX() + 1][me.getCoordinates().getY()].getState() != TileState.WALL) {
                return true;
            }
        }
        if (me.getOrientation() == Orientation.LEFT) {
            if (me.getCoordinates().getX() != 0 &&
                board.getCells()[me.getCoordinates().getX() - 1][me.getCoordinates().getY()].getState() != TileState.WALL) {
                return true;
            }

        }

        return false;
    }

    private boolean treasuresAheadOfMe(Tile me, TreasureHunterBoard board) {
        if (me.getOrientation() == Orientation.LEFT) {
            return lookLeft(me, board);
        }
        else if (me.getOrientation() == Orientation.RIGHT) {
            return lookRight(me, board);
        }
        else if (me.getOrientation() == Orientation.DOWN) {
            return lookDown(me, board);
        }
        else {
            return lookUp(me, board);
        }
    }

    private boolean lookDown(Tile me, TreasureHunterBoard board) {
        int nextY = me.getCoordinates().getY() + 1;
        while (nextY < board.getNumberOfRows()) {
            if (board.getTile(me.getCoordinates().getX(), nextY).getState() == TileState.TREASURE) {
                return true;
            }
            nextY++;
        }

        return false;
    }

    private boolean lookUp(Tile me, TreasureHunterBoard board) {
        int nextY = me.getCoordinates().getY() - 1;
        while (nextY >= 0) {
            if (board.getTile(me.getCoordinates().getX(), nextY).getState() == TileState.TREASURE) {
                return true;
            }
            nextY--;
        }

        return false;
    }

    private boolean lookRight(Tile me, TreasureHunterBoard board) {
        int nextX = me.getCoordinates().getX() + 1;
        while (nextX < board.getNumberOfCols()) {
            if (board.getTile(nextX, me.getCoordinates().getY()).getState() == TileState.TREASURE) {
                return true;
            }
            nextX++;
        }

        return false;
    }
    private boolean lookLeft(Tile me, TreasureHunterBoard board) {
        int nextX = me.getCoordinates().getX() - 1;
        while (nextX >= 0) {
            if (board.getTile(nextX, me.getCoordinates().getY()).getState() == TileState.TREASURE) {
                return true;
            }
            nextX--;
        }

        return false;
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
