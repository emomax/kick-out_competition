package fourinarowbot.server.response;

import java.util.ArrayList;
import java.util.Date;
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
    private long redPlayerGameTime;
    private long yellowPlayerGameTime;
    private final Date gameStartDate = new Date();

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

    public long getRedPlayerGameTime() {
        return redPlayerGameTime;
    }

    public void setRedPlayerGameTime(final long redPlayerGameTime) {
        this.redPlayerGameTime = redPlayerGameTime;
    }

    public long getYellowPlayerGameTime() {
        return yellowPlayerGameTime;
    }

    public void setYellowPlayerGameTime(final long yellowPlayerGameTime) {
        this.yellowPlayerGameTime = yellowPlayerGameTime;
    }

    public Date getGameStartDate() {
        return gameStartDate;
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
        System.out.println("");
        System.out.println(redPlayerName + " total play time: " + redPlayerGameTime);
        System.out.println(yellowPlayerName + " total play time: " + yellowPlayerGameTime);
        System.out.println("___________________________________");
        System.out.println("");
    }
}
