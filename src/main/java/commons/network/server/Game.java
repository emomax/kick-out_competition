package commons.network.server;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

import commons.gameengine.board.Board;

public interface Game extends Serializable {
    UUID getId();
    String getName();
    String getRedPlayerName();
    String getYellowPlayerName();
    String getGameOutcome();

    void setRedPlayerName(String redPlayerName);
    void setYellowPlayerName(String redPlayerName);

    Board getBoard();

    void waitForMyTurn(final String playerName) throws InterruptedException;
    void finishMyTurn();
    boolean isGameOver();

    Date getGameStartTime();
    GameTimer getTimer();
    void updateGameStatisticsWithGameTime();

    void setGameOutcome(String s);
}
