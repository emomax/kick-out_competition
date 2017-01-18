package spacerace.server.communication.response;

import java.io.Serializable;
import java.util.List;

import spacerace.domain.GameState;
import spacerace.domain.PlayerResult;
import spacerace.level.Level;

public class ServerResponse implements Serializable {

    private String             message;
    private String             errorMessage;
    private GameState          gameState;
    private Level              level;
    private List<PlayerResult> playerResults;

    public String getMessage() {
        return message;
    }

    public void setMessage(final String message) {
        this.message = message;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(final String errorMessage) {
        this.errorMessage = errorMessage;
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

    public List<PlayerResult> getPlayerResults() {
        return playerResults;
    }

    public void setPlayerResults(final List<PlayerResult> playerResults) {
        this.playerResults = playerResults;
    }
}
