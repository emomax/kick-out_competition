package spacerace.server.socket;

import java.io.Serializable;

public class SocketRequest implements Serializable {

    public enum Type {
        REGISTER_PLAYER,
        GET_GAME_STATE,
        POST_ACTION,
        SEND_START_COMMAND,
        GET_GAME_RESULT,
        TEST
    }

    private final Type    type;
    private       String  gameName;
    private       String  playerName;
    private       Integer levelNumber;
    private       String  accelerationX;
    private       String  accelerationY;
    private       boolean stabilize;

    public SocketRequest(final Type type) {
        this.type = type;
    }

    public Type getType() {
        return type;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(final String gameName) {
        this.gameName = gameName;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(final String playerName) {
        this.playerName = playerName;
    }

    public Integer getLevelNumber() {
        return levelNumber;
    }

    public void setLevelNumber(final Integer levelNumber) {
        this.levelNumber = levelNumber;
    }

    public String getAccelerationX() {
        return accelerationX;
    }

    public void setAccelerationX(final String accelerationX) {
        this.accelerationX = accelerationX;
    }

    public String getAccelerationY() {
        return accelerationY;
    }

    public void setAccelerationY(final String accelerationY) {
        this.accelerationY = accelerationY;
    }

    public boolean isStabilize() {
        return stabilize;
    }

    public void setStabilize(final boolean stabilize) {
        this.stabilize = stabilize;
    }
}
