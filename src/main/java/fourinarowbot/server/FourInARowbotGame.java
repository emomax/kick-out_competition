package fourinarowbot.server;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import commons.server.Game;
import commons.server.GameTimer;
import fourinarowbot.SearchResult;
import fourinarowbot.board.FourInARowbotBoard;
import fourinarowbot.board.BoardState;
import fourinarowbot.domain.MarkerColor;
import fourinarowbot.server.response.GameStatistics;

public class FourInARowbotGame implements Game, Serializable {

    private static final int NUMBER_OF_ROUNDS = 10;

    private final UUID   id;
    private final String name;
    private final String redPlayerName;
    private       String yellowPlayerName;
    private final FourInARowbotBoard board                 = new FourInARowbotBoard();
    private final AtomicBoolean      isRedPlayerTurn       = new AtomicBoolean(true);
    private final GameStatistics     gameStatistics        = new GameStatistics();
    private final AtomicInteger      numberOfFinishedGames = new AtomicInteger();
    private final GameTimer          gameTimer             = new GameTimer();
    private final Date               gameStartTime         = new Date();

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

    public MarkerColor getPlayerColor(final String playerName) {
        if (redPlayerName.equals(playerName)) {
            return MarkerColor.RED;
        }
        else {
            return MarkerColor.YELLOW;
        }
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
        final MarkerColor playerColor = getPlayerColor(playerName);
        while (true) {
            if (playerColor.equals(MarkerColor.RED) && isRedPlayerTurn.get()) {
                break;
            }
            else if (playerColor.equals(MarkerColor.YELLOW) && !isRedPlayerTurn.get()) {
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
        gameStatistics.updateStatistics(gameStatusAfterPlacing, new BoardState(board.getBoard()));
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
}
