package fourinarowbot;

import commons.gameengine.board.PlayerColor;

public class SearchResult {
    private final PlayerColor winnerPlayerColor;
    private final boolean     draw;

    public SearchResult(final PlayerColor winnerPlayerColor, final boolean draw) {
        this.winnerPlayerColor = winnerPlayerColor;
        this.draw = draw;
    }

    public static SearchResult resultWithWinner(final PlayerColor winnerPlayerColor) {
        return new SearchResult(winnerPlayerColor, false);
    }

    public static SearchResult resultWithoutWinner() {
        return new SearchResult(null, false);
    }

    public static SearchResult drawResult() {
        return new SearchResult(null, true);
    }

    public boolean isWinnerFound() {
        return winnerPlayerColor != null;
    }

    public PlayerColor getWinnerPlayerColor() {
        return winnerPlayerColor;
    }

    public boolean isDraw() {
        return draw;
    }

    public boolean isGameOver() {
        return isWinnerFound() || draw;
    }
}
