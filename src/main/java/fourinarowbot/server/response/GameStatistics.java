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

    public void print() {
        boardStates.stream()
                .map(boardState -> new BoardImpl(boardState.getMarkers()))
                .forEach(BoardImpl::print);
        System.out.println("Red wins:    " + redWins);
        System.out.println("Yellow wins: " + yellowWins);
        System.out.println("Draws:       " + draws);
    }
}
