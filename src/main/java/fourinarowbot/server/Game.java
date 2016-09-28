package fourinarowbot.server;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import fourinarowbot.SearchResult;
import fourinarowbot.board.BoardImpl;
import fourinarowbot.board.BoardState;
import fourinarowbot.domain.MarkerColor;
import fourinarowbot.server.response.GameStatistics;

public class Game {

    private static final int NUMBER_OF_ROUNDS = 10;

    private final UUID   id;
    private final String name;
    private final String redPlayerName;
    private       String yellowPlayerName;
    private final BoardImpl      board                 = new BoardImpl();
    private final AtomicBoolean  isRedPlayerTurn       = new AtomicBoolean(true);
    private final GameStatistics gameStatistics        = new GameStatistics();
    private final AtomicInteger  numberOfFinishedGames = new AtomicInteger();

    public Game(final UUID id, final String name, final String redPlayerName) throws InterruptedException {
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

    public synchronized BoardImpl getBoard() {
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

    public synchronized void setRoundOver(final SearchResult gameStatusAfterPlacing, final BoardImpl board) {
        gameStatistics.updateStatistics(gameStatusAfterPlacing, new BoardState(board.getBoard()));
        numberOfFinishedGames.incrementAndGet();
        this.board.reset();
    }
}
