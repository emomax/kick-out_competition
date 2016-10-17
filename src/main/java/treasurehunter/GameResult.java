package treasurehunter;

import commons.board.PlayerColor;

public class GameResult {
    public static GameResult ResultWithoutWinner() {
        return new GameResult();
    }

    private static int redTreasures = 0;
    private static int yellowTreasures = 0;
    private static int treasuresLeft = 11; // Always odd number, to guarantee a winner

    public static void collectTreasure(PlayerColor player) {
        if (player == PlayerColor.RED) {
            redTreasures++;
        }
        else if (player == PlayerColor.YELLOW) {
            yellowTreasures++;
        }
        else {
            throw new RuntimeException("Unknown player color: " + player);
        }

        treasuresLeft--;
    }

    public boolean noMoreTreasures() {
        return (getTreasuresLeft() == 0);
    }

    public static int getTreasuresLeft() {
        return treasuresLeft;
    }


    public PlayerColor getWinnerColor() {
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

    public GameResult getGameStatus() {
        return new GameResult();
    }

    public void printScore() {
        System.out.println("Yellow score: " + yellowTreasures);
        System.out.println("Red score: " + redTreasures);
    }
}
