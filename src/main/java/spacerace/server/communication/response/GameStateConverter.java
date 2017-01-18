package spacerace.server.communication.response;

import java.util.List;

import spacerace.domain.GameState;
import spacerace.domain.Ship;
import spacerace.domain.ShipState;
import spacerace.server.SpaceRaceGame;

import static java.util.stream.Collectors.toList;

public class GameStateConverter {

    private GameStateConverter() {
        // No instantiation for you!
    }

    public static GameState convertGameToGameState(final SpaceRaceGame game) {
        final GameState gameState = new GameState();
        gameState.setGameStatus(game.getGameStatus().toString());
        gameState.setStartTime(game.getStartTime());

        final List<ShipState> shipStates = game.getShips().stream()
                .map(GameStateConverter::convertShipToShipState)
                .collect(toList());

        gameState.setShipStates(shipStates);

        return gameState;
    }

    private static ShipState convertShipToShipState(final Ship ship) {
        final ShipState shipState = new ShipState();
        shipState.setName(ship.getName());
        shipState.setPosition(ship.getPosition());
        shipState.setSpeed(ship.getSpeed());
        shipState.setAccelerationDirection(ship.getAccelerationDirection());
        shipState.setStabilize(ship.isStabilize());
        shipState.setColorRGB(ship.getColor().getRGB());
        shipState.setResetFrozen(ship.isResetFrozen());
        shipState.setPassedGoal(ship.isPassedGoal());
        return shipState;
    }
}
