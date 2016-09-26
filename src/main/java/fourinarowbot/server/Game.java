package fourinarowbot.server;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

import fourinarowbot.board.BoardImpl;
import fourinarowbot.domain.MarkerColor;

public class Game {

    private final UUID   id;
    private final String name;
    private final String redPlayerName;
    private       String yellowPlayerName;
    private final BoardImpl     board           = new BoardImpl();
    //    private final Semaphore   semaphore = new Semaphore(0);
    private final AtomicBoolean isRedPlayerTurn = new AtomicBoolean(true);
    private final MarkerColor   nextTurn        = MarkerColor.RED; // Red player always starts as of now

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

    public BoardImpl getBoard() {
        return board;
    }

    public synchronized boolean isItMyTurn(final String playerName) {
        if (playerName.equals(redPlayerName) && nextTurn == MarkerColor.RED) {
            return true;
        }
        else if (playerName.equals(yellowPlayerName) && nextTurn == MarkerColor.YELLOW) {
            return true;
        }
        return false;
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

    public String getYellowPlayerName() {
        return yellowPlayerName;
    }

    public void waitForMyTurn(final String playerName) throws InterruptedException {
        //        semaphore.acquire();
        final MarkerColor playerColor = getPlayerColor(playerName);
        while (true) {
            if (playerColor.equals(MarkerColor.RED) && isRedPlayerTurn.get()) {
                break;
            }
            else if (playerColor.equals(MarkerColor.YELLOW) && !isRedPlayerTurn.get()) {
                break;
            }
            Thread.sleep(100);
        }
    }

    public void finishMyTurn() {
        //        semaphore.release();
        final boolean redTurn = isRedPlayerTurn.get();
        isRedPlayerTurn.set(!redTurn);
    }
}
