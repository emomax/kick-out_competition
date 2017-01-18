package spacerace.server.socket;

import java.io.Serializable;

public class SocketRequest implements Serializable {

    private SocketRequestType type;
    private String            gameName;
    private String            playerName;
    private Integer           levelNumber;
    private String            accelerationX;
    private String            accelerationY;
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
