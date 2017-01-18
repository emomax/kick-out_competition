package spacerace.server.communication.socket;

import java.io.Serializable;

import spacerace.domain.Acceleration;

public class SocketRequest implements Serializable {

    private SocketRequestType type;
    private String            gameName;
    private String            playerName;
    private Integer           levelNumber;
    private Acceleration      accelerationX;
    private Acceleration      accelerationY;
    private boolean           stabilize;

    public SocketRequestType getType() {
        return type;
    }

    public void setType(final SocketRequestType type) {
        this.type = type;
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

    public Acceleration getAccelerationX() {
        return accelerationX;
    }

    public void setAccelerationX(final Acceleration accelerationX) {
        this.accelerationX = accelerationX;
    }

    public Acceleration getAccelerationY() {
        return accelerationY;
    }

    public void setAccelerationY(final Acceleration accelerationY) {
        this.accelerationY = accelerationY;
    }

    public boolean isStabilize() {
        return stabilize;
    }

    public void setStabilize(final boolean stabilize) {
        this.stabilize = stabilize;
    }
}
