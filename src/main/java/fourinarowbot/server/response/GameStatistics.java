package fourinarowbot.server.response;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import commons.gameengine.board.BoardState;
import commons.gameengine.board.PlayerColor;
import fourinarowbot.SearchResult;
import fourinarowbot.board.FourInARowbotBoard;
import fourinarowbot.domain.Marker;

public class GameStatistics implements Serializable {

    private int draws;
    private int redWins;
    private int yellowWins;
    private final List<BoardState<Marker>> boardStates = new ArrayList<>();
    private long redPlayerGameTime;
    private long yellowPlayerGameTime;

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
    public List<BoardState<Marker>> getBoardStates() {
        return boardStates;
    }

    public void updateStatistics(final SearchResult searchResult, final BoardState<Marker> boardState) {
        if (searchResult.isDraw()) {
            draws++;
        }
        else if (searchResult.getWinnerPlayerColor() == PlayerColor.RED) {
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

    public void print(final String redPlayerName, final String yellowPlayerName) {
        boardStates.stream()
                .map(boardState -> new FourInARowbotBoard(boardState.getCells()))
                .forEach(FourInARowbotBoard::print);
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
