package treasurehunter;

import treasurehunter.board.PlayerColor;

/**
 * Created by maxjonsson on 2016-10-13.
 */
public class GameResult {
    public static GameResult ResultWithoutWinner() {
        return new GameResult();
    }

    public boolean isGameOver() {
        return false;
    }

    public PlayerColor getWinnerColor() {
        return PlayerColor.RED;
    }

    public boolean isDraw() {
        return false;
    }

    public GameResult getGameStatus() {
        return new GameResult();
    }
}
