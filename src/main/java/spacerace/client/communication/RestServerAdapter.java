package spacerace.client.communication;

import org.springframework.web.client.RestTemplate;

import spacerace.domain.Action;
import spacerace.server.communication.response.ServerResponse;

public class RestServerAdapter implements ServerAdapter {

    private static final int PORT = 8080;

    private final String  serverAddress;
    private final String  playerName;
    private final String  gameName;
    private final Integer levelNumber;

    public RestServerAdapter(final String serverIP, final String playerName, final String gameName) {
        this(serverIP, playerName, gameName, null);
    }

    public RestServerAdapter(final String serverIP, final String playerName, final String gameName, final Integer levelNumber) {
        this.serverAddress = serverIP + ":" + PORT;
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
    public ServerResponse getGameStateForViewing() {
        final RestTemplate restTemplate = new RestTemplate();
        final String       url          = "http://" + serverAddress + "/getGameStateForViewing?gameName=" + gameName;
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
                           + "&stabilize=" + action.isStabilize()
                           + "&missileAngle=" + action.getMissileAngle();

        return restTemplate.getForObject(url, ServerResponse.class);
    }

    @Override
    public ServerResponse sendStartCommand() {
        final RestTemplate restTemplate = new RestTemplate();
        final String       url          = "http://" + serverAddress + "/startGame?gameName=" + gameName;
        return restTemplate.getForObject(url, ServerResponse.class);
    }

    @Override
    public ServerResponse getGameResult() {
        final RestTemplate restTemplate = new RestTemplate();
        final String       url          = "http://" + serverAddress + "/getPlayerPositions?gameName=" + gameName;
        return restTemplate.getForObject(url, ServerResponse.class);
    }
}
