package fourinarowbot.server.response;

import java.util.ArrayList;
import java.util.List;

import fourinarowbot.SearchResult;
import fourinarowbot.board.BoardImpl;
import fourinarowbot.board.BoardState;
import fourinarowbot.domain.MarkerColor;

public class GameStatistics {

    private int draws;
    private int redWins;
    private int yellowWins;
    private final List<BoardState> boardStates = new ArrayList<>();

    // For JSON-serialization
    public int getDraws() {
        return draws;
    }

    // For JSON-serialization only
    public int getRedWins() {
        return redWins;
    }

    // For JSON-serialization only
    public int getYellowWins() {
        return yellowWins;
    }

    // For JSON-serialization only
    public List<BoardState> getBoardStates() {
        return boardStates;
    }

    public void updateStatistics(final SearchResult searchResult, final BoardState boardState) {
        if (searchResult.isDraw()) {
            draws++;
        }
        else if (searchResult.getWinnerMarkerColor() == MarkerColor.RED) {
            redWins++;
        }
        else {
            yellowWins++;
        }
        boardStates.add(boardState);
    }

    public void print(final String redPlayerName, final String yellowPlayerName) {
        boardStates.stream()
                .map(boardState -> new BoardImpl(boardState.getMarkers()))
                .forEach(BoardImpl::print);
        System.out.println("___________________________________");
        System.out.println("");
        System.out.println(" " + redWins + "  " + redPlayerName + " (Red) wins");
        System.out.println(" " + yellowWins + "  " + yellowPlayerName + " (Yellow) wins");
        System.out.println(" " + draws + "  Draws");
        System.out.println("___________________________________");
        System.out.println("");
    }
}
