package fourinarowbot.server.response;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import commons.gameengine.board.BoardState;
import fourinarowbot.domain.Marker;

public class GameSummaryResponse {

    private UUID                     uuid;
    private String                   gameName;
    private String                   redPlayerName;
    private String                   yellowPlayerName;
    private int                      draws;
    private int                      redWins;
    private int                      yellowWins;
    private List<BoardState<Marker>> boardStates = new ArrayList<>();
    private long   redPlayerGameTime;
    private long   yellowPlayerGameTime;
    private String gameStartDate;

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(final UUID uuid) {
        this.uuid = uuid;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(final String gameName) {
        this.gameName = gameName;
    }

    public String getRedPlayerName() {
        return redPlayerName;
    }

    public void setRedPlayerName(final String redPlayerName) {
        this.redPlayerName = redPlayerName;
    }

    public String getYellowPlayerName() {
        return yellowPlayerName;
    }

    public void setYellowPlayerName(final String yellowPlayerName) {
        this.yellowPlayerName = yellowPlayerName;
    }

    public int getDraws() {
        return draws;
    }

    public void setDraws(final int draws) {
        this.draws = draws;
    }

    public int getRedWins() {
        return redWins;
    }

    public void setRedWins(final int redWins) {
        this.redWins = redWins;
    }

    public int getYellowWins() {
        return yellowWins;
    }

    public void setYellowWins(final int yellowWins) {
        this.yellowWins = yellowWins;
    }

    public List<BoardState<Marker>> getBoardStates() {
        return boardStates;
    }

    public void setBoardStates(final List<BoardState<Marker>> boardStates) {
        this.boardStates = boardStates;
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

    public String getGameStartDate() {
        return gameStartDate;
    }

    public void setGameStartDate(final String gameStartDate) {
        this.gameStartDate = gameStartDate;
    }
}
