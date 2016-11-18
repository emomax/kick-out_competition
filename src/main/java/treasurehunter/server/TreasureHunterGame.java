package treasurehunter.server;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import commons.gameengine.board.BoardState;
import commons.gameengine.board.PlayerColor;
import commons.network.server.Game;
import commons.network.server.GameTimer;
import treasurehunter.GameResult;
import treasurehunter.board.BoardStateUpdate;
import treasurehunter.board.Tile;
import treasurehunter.board.TreasureHunterBoard;

public class TreasureHunterGame implements Game, Serializable {
    private final UUID   id;
    private final String name;
    private       String redPlayerName;
    private       String yellowPlayerName;
    private       long redPlayerGameTime;
    private       long yellowPlayerGameTime;
    private       String gameOutcome = "";

    private String[][] initialBoardStateAsString;
    private final List<String> boardStateUpdatesAsJsonString = new ArrayList<>();
    private final TreasureHunterBoard board;
    private final AtomicBoolean       isRedPlayerTurn       = new AtomicBoolean(true);
    private       AtomicInteger       yellowPlayerTreasures = new AtomicInteger(0);
    private       AtomicInteger       numberOfTreasures;
    private       AtomicInteger       redPlayerTreasures    = new AtomicInteger(0);
    private final GameTimer           gameTimer             = new GameTimer();
    private final Date                gameStartTime         = new Date();

    public TreasureHunterGame(final UUID id, final String name, final String redPlayerName) throws InterruptedException {
        this.id = id;
        this.name = name;
        this.redPlayerName = redPlayerName;

        this.board = new TreasureHunterBoard();

        this.numberOfTreasures = new AtomicInteger(board.getTotalTreasures());
    }

    public int getNumberOfTreasures() {
        return numberOfTreasures.get();
    }

    public void redPlayerCollected() {
        redPlayerTreasures.incrementAndGet();
        System.out.println("Red player collected! Treasures: " + redPlayerTreasures);
    }

    public void yellowPlayerCollected() {
        yellowPlayerTreasures.incrementAndGet();
    }

    public int getYellowPlayerTreasures() {
        return yellowPlayerTreasures.get();
    }

    public int getRedPlayerTreasures() {
        return redPlayerTreasures.get();
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setInitialBoardState(final BoardState<Tile> boardState) {
        this.initialBoardStateAsString = cellsAsStrings(boardState);
    }

    public void addBoardStateUpdate(final BoardStateUpdate boardStateUpdate) {
        boardStateUpdatesAsJsonString.add(boardStateUpdate.toJSON());
    }

    public String[][] cellsAsStrings(BoardState<Tile> boardState) {
        final String[][] cellsAsStrings = new String[boardState.getCells().length][boardState.getCells()[0].length];

        for (int x = 0; x < boardState.getCells().length; x++) {
            for (int y = 0; y < boardState.getCells()[0].length; y++) {
                Tile currentTile = boardState.getCells()[x][y];
                String tileString = currentTile.getState().toString();

                if ( currentTile.getState() == Tile.TileState.RED || currentTile.getState() == Tile.TileState.YELLOW) {
                    tileString += "_" + currentTile.getOrientation().toString();
                }

                cellsAsStrings[x][y] = tileString;
            }
        }

        return cellsAsStrings;
    }

    public String[][] getInitialBoardStateAsString() {
        return initialBoardStateAsString;
    }

    public List<String> getBoardUpdatesAsJsonString() {
        return boardStateUpdatesAsJsonString;
    }

    public synchronized TreasureHunterBoard getBoard() {
        return board;
    }

    public PlayerColor getPlayerColor(final String playerName) {
        if (redPlayerName.equals(playerName)) {
            return PlayerColor.RED;
        }
        else {
            return PlayerColor.YELLOW;
        }
    }

    public void setRedPlayerName(String redPlayerName) {
        this.redPlayerName = redPlayerName;
    }

    public String getRedPlayerName() {
        return redPlayerName;
    }

    public synchronized void setYellowPlayerName(final String yellowPlayerName) {
        this.yellowPlayerName = yellowPlayerName;
    }

    public String getYellowPlayerName() {
        return yellowPlayerName;
    }

    public long getRedPlayerGameTime() {
        return redPlayerGameTime;
    }

    public long getYellowPlayerGameTime() {
        return yellowPlayerGameTime;
    }

    public String getGameOutcome() {
        return this.gameOutcome;
    }

    public void setGameOutcome(String outcome) {
        this.gameOutcome = outcome;
    }

    public void waitForMyTurn(final String playerName) throws InterruptedException {
        final PlayerColor playerColor = getPlayerColor(playerName);
        final int maxIterationsBeforeTimeout = 750;
        final AtomicInteger iterator = new AtomicInteger(0);

        while (true) {
            if (iterator.get() % 50 == 0) {
                System.out.println("Waited " + iterator.get()/50 + " seconds for " + playerName + "..");
            }

            if (iterator.incrementAndGet() > maxIterationsBeforeTimeout) {
                throw new InterruptedException("Player " + ((playerColor == PlayerColor.RED) ? PlayerColor.YELLOW : PlayerColor.RED) + " timed out!");
            }

            if (playerColor.equals(PlayerColor.RED) && isRedPlayerTurn.get()) {
                break;
            }
            else if (playerColor.equals(PlayerColor.YELLOW) && !isRedPlayerTurn.get()) {
                break;
            }
            Thread.sleep(20);
        }
    }

    public void finishMyTurn() {
        isRedPlayerTurn.set(!isRedPlayerTurn.get());
    }

    public synchronized void incrementTurns() {
        GameResult.incrementTurns();
    }

    public synchronized boolean isGameOver() {
        return GameResult.isGameOver(board);
    }

    public synchronized boolean initialBoardIsSet() {
        return initialBoardStateAsString != null;
    }

    public synchronized void playerCollected(final PlayerColor playerColor) {
        if (playerColor == PlayerColor.RED) {
            redPlayerCollected();
        }
        else if (playerColor == PlayerColor.YELLOW) {
            yellowPlayerCollected();
        }
    }

    public GameTimer getTimer() {
        return gameTimer;
    }

    public void updateGameStatisticsWithGameTime() {
        redPlayerGameTime = gameTimer.getGameTime(redPlayerName);
        yellowPlayerGameTime = gameTimer.getGameTime(yellowPlayerName);
    }

    public Date getGameStartTime() {
        return gameStartTime;
    }
}
