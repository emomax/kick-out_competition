package treasurehunter;

import commons.gameengine.board.PlayerColor;
import treasurehunter.board.Tile;
import treasurehunter.board.TreasureHunterBoard;

public class GameResult {
    public static GameResult ResultWithoutWinner() {
        return new GameResult();
    }

    private int redTreasures = 0;
    private int yellowTreasures = 0;
    private int treasuresLeft = 9; // Always odd number, to guarantee a winner

    public GameResult() {}

    public GameResult(TreasureHunterBoard board) {

    }

    public boolean noMoreTreasures() {
        return (getTreasuresLeft() == 0);
    }

    public int getTreasuresLeft() {
        return treasuresLeft;
    }


    public PlayerColor getWinnerPlayerColor() {
        if (redTreasures > yellowTreasures) {
            return PlayerColor.RED;
        }
        else  {
            return PlayerColor.YELLOW;
        }
    }

    public boolean isDraw() {
        return false;
    }

    public static boolean isGameOver(TreasureHunterBoard board) {
        return getTreasuresLeft(board) == 0;
    }

    public static int getTreasuresLeft(TreasureHunterBoard board) {
        int treasures = 0;

        for (int x = 0; x < board.getNumberOfCols(); x++) {
            for (int y = 0; y < board.getNumberOfRows(); y++) {
                if ( board.getCells()[x][y].getState() == Tile.TileState.TREASURE) {
                    treasures++;
                }
            }
        }

        return treasures;
    }

    public GameResult getGameStatus() {
        return new GameResult();
    }

    public void printScore() {
        System.out.println("Yellow score: " + yellowTreasures);
        System.out.println("Red score: " + redTreasures);
    }
}