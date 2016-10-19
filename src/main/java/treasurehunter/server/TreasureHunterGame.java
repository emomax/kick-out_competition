package treasurehunter.server;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import commons.gameengine.board.PlayerColor;
import commons.network.server.Game;
import commons.network.server.GameTimer;
import treasurehunter.GameResult;
import treasurehunter.board.TreasureHunterBoard;
import treasurehunter.domain.PlayerMove;

public class TreasureHunterGame implements Game, Serializable {
    private final UUID   id;
    private final String name;
    private       String redPlayerName;
    private       String yellowPlayerName;
    private       long redPlayerGameTime;
    private       long yellowPlayerGameTime;

    private final TreasureHunterBoard initialBoard;
    private final TreasureHunterBoard board;
    private       List<PlayerMove>    playerMoves           = new ArrayList<>();
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
        this.initialBoard = new TreasureHunterBoard(board.getCells());

        this.numberOfTreasures = new AtomicInteger(initialBoard.getTotalTreasures());
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

    public TreasureHunterBoard getInitialBoard() {
        return initialBoard;
    }

    public synchronized TreasureHunterBoard getBoard() {
        return board;
    }

    public List<PlayerMove> getPlayerMoves() {
        return playerMoves;
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
    public void waitForMyTurn(final String playerName) throws InterruptedException {
        final PlayerColor playerColor = getPlayerColor(playerName);
        while (true) {
            if (playerColor.equals(PlayerColor.RED) && isRedPlayerTurn.get()) {
                break;
            }
            else if (playerColor.equals(PlayerColor.YELLOW) && !isRedPlayerTurn.get()) {
                break;
            }
            Thread.sleep(20);
        }
    }

    public void addPlayerMove(PlayerMove playerMove) {
        this.playerMoves.add(playerMove);
    }

    public void finishMyTurn() {
        isRedPlayerTurn.set(!isRedPlayerTurn.get());
    }

    public synchronized boolean isGameOver() {
        return GameResult.isGameOver(board);
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
