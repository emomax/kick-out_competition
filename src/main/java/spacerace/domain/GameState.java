package spacerace.domain;

import java.io.Serializable;
import java.util.List;

public class GameState implements Serializable {

    private String          gameStatus;
    private List<ShipState> shipStates;

    public GameState() {
        // For JSON conversion
    }

    public GameState(final String gameStatus, final List<ShipState> shipStates) {
        this.gameStatus = gameStatus;
        this.shipStates = shipStates;
    }

    public void setGameStatus(final String gameStatus) {
        this.gameStatus = gameStatus;
    }

    public String getGameStatus() {
        return gameStatus;
    }

    public List<ShipState> getShipStates() {
        return shipStates;
    }

    public void setShipStates(final List<ShipState> shipStates) {
        this.shipStates = shipStates;
    }
}
