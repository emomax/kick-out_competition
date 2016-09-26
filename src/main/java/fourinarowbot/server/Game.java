package fourinarowbot.server;

import java.util.UUID;

import fourinarowbot.board.BoardImpl;
import fourinarowbot.domain.MarkerColor;

public class Game {

    private final UUID   id;
    private final String name;
    private final String redPlayerName;
    private       String yellowPlayerName;
    private final BoardImpl board           = new BoardImpl();
    private       Boolean   isRedPlayerTurn = true;

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

    public void waitForMyTurn(final String playerName) throws InterruptedException {
        final MarkerColor playerColor = getPlayerColor(playerName);
        while (true) {
            synchronized (isRedPlayerTurn) {
                if (playerColor.equals(MarkerColor.RED) && isRedPlayerTurn) {
                    break;
                }
                else if (playerColor.equals(MarkerColor.YELLOW) && !isRedPlayerTurn) {
                    break;
                }
            }
            Thread.sleep(100);
        }
    }

    public void finishMyTurn() {
        synchronized (isRedPlayerTurn) {
            isRedPlayerTurn = !isRedPlayerTurn;
        }
    }
}
