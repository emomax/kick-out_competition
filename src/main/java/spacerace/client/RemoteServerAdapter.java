package spacerace.client;

import org.springframework.web.client.RestTemplate;

import spacerace.domain.Action;
import spacerace.server.response.ServerResponse;

public class RemoteServerAdapter implements ServerAdapter {

    private final String serverAddress;
    private final String playerName;
    private final String gameName;
    private final int    levelNumber;

    public RemoteServerAdapter(final String serverAddress, final String playerName, final String gameName, final int levelNumber) {
        this.serverAddress = serverAddress;
        this.playerName = playerName;
        this.gameName = gameName;
        this.levelNumber = levelNumber;
    }

    @Override
    public ServerResponse registerPlayer() {
        final RestTemplate restTemplate = new RestTemplate();
        final String url = "http://" + serverAddress
                           + "/registerPlayer"
                           + "?gameName=" + gameName
                           + "&playerName=" + playerName
                           + "&levelNumber=" + levelNumber;

        return restTemplate.getForObject(url, ServerResponse.class);
    }

    @Override
    public ServerResponse getGameState() {
        final RestTemplate restTemplate = new RestTemplate();
        final String       url          = "http://" + serverAddress + "/getGameState?gameName=" + gameName;
        return restTemplate.getForObject(url, ServerResponse.class);
    }

    @Override
    public ServerResponse postActionToServer(final Action action) {
        final RestTemplate restTemplate = new RestTemplate();
        final String url = "http://" + serverAddress
                           + "/action"
                           + "?gameName=" + gameName
                           + "&playerName=" + playerName
                           + "&accelerationX=" + action.getAccelerationX()
                           + "&accelerationY=" + action.getAccelerationY()
                           + "&stabilize=" + action.isStabilize();

        return restTemplate.getForObject(url, ServerResponse.class);
    }

    @Override
    public ServerResponse sendStartCommand() {
        final RestTemplate restTemplate = new RestTemplate();
        final String       url          = "http://" + serverAddress + "/startGame?gameName=" + gameName;
        return restTemplate.getForObject(url, ServerResponse.class);
    }
}
