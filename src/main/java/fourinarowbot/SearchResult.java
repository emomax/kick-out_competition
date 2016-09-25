package fourinarowbot;

import fourinarowbot.domain.MarkerColor;

public class SearchResult {
    private final MarkerColor winnerMarkerColor;
    private final boolean     draw;

    public SearchResult(final MarkerColor winnerMarkerColor, final boolean draw) {
        this.winnerMarkerColor = winnerMarkerColor;
        this.draw = draw;
    }

    public static SearchResult resultWithWinner(final MarkerColor winnerMarkerColor) {
        return new SearchResult(winnerMarkerColor, false);
    }

    public static SearchResult resultWithoutWinner() {
        return new SearchResult(null, false);
    }

    public static SearchResult drawResult() {
        return new SearchResult(null, true);
    }

    public boolean isWinnerFound() {
        return winnerMarkerColor != null;
    }

    public MarkerColor getWinnerMarkerColor() {
        return winnerMarkerColor;
    }

    public boolean isDraw() {
        return draw;
    }

    public boolean isGameOver() {
        return isWinnerFound() || draw;
    }
}
