package fourinarowbot.server;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import commons.gameengine.board.BoardState;
import commons.gameengine.board.PlayerColor;
import commons.network.server.Game;
import commons.network.server.GameTimer;
import fourinarowbot.SearchResult;
import fourinarowbot.board.FourInARowbotBoard;
import fourinarowbot.server.response.GameStatistics;

public class FourInARowbotGame implements Game, Serializable {

    private static final int NUMBER_OF_ROUNDS = 10;

    private final UUID   id;
    private final String name;
    private       String redPlayerName;
    private       String yellowPlayerName;
    private final FourInARowbotBoard board                 = new FourInARowbotBoard();
    private final AtomicBoolean      isRedPlayerTurn       = new AtomicBoolean(true);
    private final GameStatistics     gameStatistics        = new GameStatistics();
    private final AtomicInteger      numberOfFinishedGames = new AtomicInteger();
    private final GameTimer          gameTimer             = new GameTimer();
    private final Date               gameStartTime         = new Date();
    private       String             gameOutcome           = "";

    public FourInARowbotGame(final UUID id, final String name, final String redPlayerName) throws InterruptedException {
        this.id = id;
        this.name = name;
        this.redPlayerName = redPlayerName;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public synchronized FourInARowbotBoard getBoard() {
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

    public GameStatistics getGameStatistics() {
        return gameStatistics;
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

    public void finishMyTurn() {
        isRedPlayerTurn.set(!isRedPlayerTurn.get());
    }

    public synchronized boolean isGameOver() {
        return numberOfFinishedGames.get() == NUMBER_OF_ROUNDS;
    }

    public synchronized void setRoundOver(final SearchResult gameStatusAfterPlacing, final FourInARowbotBoard board) {
        gameStatistics.updateStatistics(gameStatusAfterPlacing, new BoardState<>(board.getBoard()));
        numberOfFinishedGames.incrementAndGet();
        this.board.reset();
    }

    public GameTimer getTimer() {
        return gameTimer;
    }

    public void updateGameStatisticsWithGameTime() {
        gameStatistics.setRedPlayerGameTime(gameTimer.getGameTime(redPlayerName));
        gameStatistics.setYellowPlayerGameTime(gameTimer.getGameTime(yellowPlayerName));
    }

    public Date getGameStartTime() {
        return gameStartTime;
    }

    public void setGameOutcome(String outcome) {
        this.gameOutcome = outcome;
    }

    public String getGameOutcome() {
        return this.gameOutcome;
    }
}
