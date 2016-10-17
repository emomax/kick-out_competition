package treasurehunter.gameengine;

import java.util.Random;

import commons.board.Board;
import commons.board.PlayerColor;
import commons.gameengine.Action;
import commons.gameengine.Coordinate;
import commons.gameengine.GameEngine;
import treasurehunter.TreasureHunterApplication;
import treasurehunter.board.Tile;
import treasurehunter.board.TreasureHunterBoard;
import treasurehunter.domain.Move;
import treasurehunter.domain.Orientation;
import treasurehunter.domain.TreasureHunterAction;

import static treasurehunter.board.Tile.TileState;

public class SmartRandomTreasureHunterGameEngine implements GameEngine {
    @Override
    public Action getNextMove(final Board board, final PlayerColor myColor) {
        final TreasureHunterBoard gameBoard = (TreasureHunterBoard) board;
        Tile me = findMe(gameBoard, myColor);

        return getRandomAction(me, gameBoard);
    }

    private Tile findMe(TreasureHunterBoard board, PlayerColor myColor) {
        for (int i = 0; i < board.getNumberOfCols(); i++) {
            for (int j = 0; j < board.getNumberOfRows(); j++) {
                if (board.getTile(i, j).getState() == TileState.valueOf(myColor.toString())) {
                    return board.getTile(i, j);
                }
            }
        }

        return null;
    }

    public Action getRandomAction(Tile me, TreasureHunterBoard board) {
        TreasureHunterAction action      = new TreasureHunterAction();
        Orientation          myDirection = me.getOrientation();
        Coordinate nextPosition = new Coordinate(
                me.getCoordinate().getX() + myDirection.xDirection(),
                me.getCoordinate().getY() + myDirection.yDirection());

        Random  rng           = new Random();
        int     selection     = rng.nextInt(6);
        boolean shouldGoForth = !board.isOutsideBoard(nextPosition) && selection > 1;


        if (shouldGoForth) {
            action.setMove(Move.MOVE_FORWARD);
        }
        else if (selection == 0) {
            action.setMove(Move.ROTATE_LEFT);
        }
        else if (selection == 1) {
            action.setMove(Move.ROTATE_RIGHT);
        }
        else {
            action.setMove(Move.ROTATE_LEFT);
        }

        return action;
    }

    // Run this main to start the game
    public static void main(final String[] args) {
        final TreasureHunterApplication fourInARowApplication = new TreasureHunterApplication(new SmartRandomTreasureHunterGameEngine());

        // Run game once
        fourInARowApplication.runGameOnce();
    }
}
