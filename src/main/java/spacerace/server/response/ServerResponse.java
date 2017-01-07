package spacerace.server.response;

import java.io.Serializable;

import spacerace.domain.GameState;
import spacerace.level.Level;

public class ServerResponse implements Serializable {

    public String message;
    GameState gameState;
    Level     level;

    public String getMessage() {
        return message;
    }

    public void setMessage(final String message) {
        this.message = message;
    }

    public GameState getGameState() {
        return gameState;
    }

    public void setGameState(final GameState gameState) {
        this.gameState = gameState;
    }

    public Level getLevel() {
        return level;
    }

    public void setLevel(final Level level) {
        this.level = level;
    }
}
