package treasurehunter.server.response;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import commons.gameengine.board.BoardState;
import treasurehunter.board.Tile;
import treasurehunter.domain.PlayerMove;

public class GameSummaryResponse {

    private UUID             uuid;
    private int              redPlayerTreasures;
    private int              yellowPlayerTreasures;
    private int              totalTreasures;
    private long             redPlayerGameTime;
    private long             yellowPlayerGameTime;
    private String[][] initialBoardState;
    private List<String> boardStateUpdates;
    private String gameStartDate;
    private String gameName;
    private String redPlayerName;
    private String yellowPlayerName;

    private String gameOutcome;

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(final UUID uuid) {
        this.uuid = uuid;
    }

    public int getRedPlayerTreasures() {
        return redPlayerTreasures;
    }

    public void setRedPlayerTreasures(final int redPlayerTreasures) {
        this.redPlayerTreasures = redPlayerTreasures;
    }

    public int getYellowPlayerTreasures() {
        return yellowPlayerTreasures;
    }

    public void setYellowPlayerTreasures(final int yellowPlayerTreasures) {
        this.yellowPlayerTreasures = yellowPlayerTreasures;
    }

    public int getTotalTreasures() {
        return totalTreasures;
    }

    public void setTotalTreasures(final int totalTreasures) {
        this.totalTreasures = totalTreasures;
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

    public void setInitialBoardState(String[][] initialBoardState) {
        this.initialBoardState = initialBoardState;
    }

    public String[][] getInitialBoardState() {
        return initialBoardState;
    }

    public void setBoardStateUpdates(List<String> boardStateUpdates) {
        this.boardStateUpdates = boardStateUpdates;
    }

    public List<String> getBoardStateUpdates() {
        return boardStateUpdates;
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

    public String getGameOutcome() {
        return gameOutcome;
    }

    public void setGameOutcome(final String gameOutcome) {
        this.gameOutcome = gameOutcome;
    }

}
