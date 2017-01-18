package spacerace.client.communication;

import spacerace.domain.Action;
import spacerace.server.communication.response.ServerResponse;
import spacerace.server.communication.rest.SpaceRaceRestController;

public class LocalServerAdapter implements ServerAdapter {

    private final String playerName;
    private final String gameName;
    private final int    levelNumber;
    private final SpaceRaceRestController serverController = new SpaceRaceRestController();

    public LocalServerAdapter(final String playerName, final String gameName, final int levelNumber) {
        this.playerName = playerName;
        this.gameName = gameName;
        this.levelNumber = levelNumber;
    }

    @Override
    public ServerResponse registerPlayer() {
        return serverController.registerPlayer(gameName, playerName, levelNumber);
    }

    @Override
    public ServerResponse getGameState() {
        return serverController.getGameState(gameName);
    }

    @Override
    public ServerResponse postActionToServer(final Action action) {
        return serverController.action(gameName, playerName, action.getAccelerationX().toString(), action.getAccelerationY().toString(), action.isStabilize());
    }

    @Override
    public ServerResponse sendStartCommand() {
        return serverController.startGame(gameName);
    }

    @Override
    public ServerResponse getGameResult() {
        return serverController.getGameResult(gameName);
    }
}
