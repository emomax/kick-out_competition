package commons.server;

import java.util.Date;
import java.util.UUID;

public interface Game {
    UUID getId();
    String getName();
    String getRedPlayerName();
    String getYellowPlayerName();

    void waitForMyTurn(final String playerName) throws InterruptedException;
    void finishMyTurn();
    boolean isGameOver();

    Date getGameStartTime();
    GameTimer getTimer();
    void updateGameStatisticsWithGameTime();
}
