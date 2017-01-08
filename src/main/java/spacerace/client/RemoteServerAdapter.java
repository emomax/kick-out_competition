package spacerace.client;

import org.springframework.web.client.RestTemplate;

import spacerace.domain.Action;
import spacerace.domain.GameState;
import spacerace.level.Level;
import spacerace.server.response.ServerResponse;

final class RemoteServerAdapter {

    private final String serverAddress;
    private final String playerName;
    private final String gameName;
    private final int    levelNumber;

    RemoteServerAdapter(final String serverAddress, final String playerName, final String gameName, final int levelNumber) {
        this.serverAddress = serverAddress;
        this.playerName = playerName;
        this.gameName = gameName;
        this.levelNumber = levelNumber;
    }

    Level registerPlayer() {
        final RestTemplate restTemplate = new RestTemplate();
        final String url = "http://" + serverAddress
                           + "/registerPlayer"
                           + "?gameName=" + gameName
                           + "&playerName=" + playerName
                           + "&levelNumber=" + levelNumber;

        final ServerResponse response = restTemplate.getForObject(url, ServerResponse.class);
        if (response.getMessage() != null) {
            throw new IllegalStateException("Exception when trying to register player: " + playerName + ", message: " + response.getMessage());
        }
        return response.getLevel();
    }

    GameState getGameState() {
        final RestTemplate restTemplate = new RestTemplate();
        final String       url          = "http://" + serverAddress + "/getGameState?gameName=" + gameName;

        final ServerResponse response = restTemplate.getForObject(url, ServerResponse.class);
        if (response.getMessage() != null) {
            throw new IllegalStateException("Exception when getting game state for game: " + gameName + ", message: " + response.getMessage());
        }
        return response.getGameState();
    }

    void postActionToServer(final Action action) {
        final RestTemplate restTemplate = new RestTemplate();
        final String url = "http://" + serverAddress
                           + "/action"
                           + "?gameName=" + gameName
                           + "&playerName=" + playerName
                           + "&accelerationX=" + action.getAccelerationX()
                           + "&accelerationY=" + action.getAccelerationY()
                           + "&stabilize=" + action.isStabilize();

        final ServerResponse response = restTemplate.getForObject(url, ServerResponse.class);
        if (response.getMessage() != null) {
            throw new IllegalStateException("Exception when posting action: " + action + ", message: " + response.getMessage());
        }
    }
}
