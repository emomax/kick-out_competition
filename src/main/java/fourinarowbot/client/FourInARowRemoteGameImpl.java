package fourinarowbot.client;

import org.springframework.web.client.RestTemplate;

import fourinarowbot.board.FourInARowbotBoard;
import commons.gameengine.Coordinates;
import fourinarowbot.domain.MarkerColor;
import fourinarowbot.gameengine.GameEngine;
import fourinarowbot.server.response.ServerResponse;
import commons.client.RemoteGame;

public class FourInARowRemoteGameImpl implements RemoteGame {

    private static final String SERVER_ADDRESS = "10.46.1.193:8080"; // FourInARowbotGame server
    //    private static final String SERVER_ADDRESS = "127.0.0.1:8080"; // If you run locally

    public void startGame(final String playerName, final String gameName, final GameEngine gameEngine) {
        final ServerResponse serverResponse = runGame(playerName, gameName, gameEngine);
        printGameResult(serverResponse);
    }

    public ServerResponse runGame(final String playerName, final String gameName, final GameEngine gameEngine) {
        ServerResponse serverResponse;
        while (true) {
            serverResponse = getBoardState(gameName, playerName);
            if (serverResponse.getMessage() != null) {
                return serverResponse;
            }

            final Coordinates coordinates = getCoordinatesForNextMarkerToPlace(playerName, gameEngine, serverResponse);
            serverResponse = placeMarker(gameName, playerName, coordinates);
            if (serverResponse.getMessage() != null) {
                return serverResponse;
            }
        }
    }

    private static Coordinates getCoordinatesForNextMarkerToPlace(final String playerName, final GameEngine gameEngine, final ServerResponse response) {
        final FourInARowbotBoard board         = new FourInARowbotBoard(response.getBoardState().getMarkers());
        final MarkerColor        myMarkerColor = getMyMarkerColor(playerName, response);
        return gameEngine.getCoordinatesForNextMakerToPlace(board, myMarkerColor);
    }

    private static MarkerColor getMyMarkerColor(final String playerName, final ServerResponse response) {
        if (response.getRedPlayerName().equals(playerName)) {
            return MarkerColor.RED;
        }
        return MarkerColor.YELLOW;
    }

    public void printGameResult(final ServerResponse response) {
        System.out.println(response.getMessage());
        if (response.getGameStatistics() != null) {
            response.getGameStatistics().print(response.getRedPlayerName(), response.getYellowPlayerName());
        }
    }

    public ServerResponse getBoardState(final String gameName, final String playerName) {
        final RestTemplate restTemplate = new RestTemplate();
        final String       url          = "http://" + SERVER_ADDRESS + "/getBoard?gameName=" + gameName + "&playerName=" + playerName;
        return restTemplate.getForObject(url, ServerResponse.class);
    }

    private static ServerResponse placeMarker(final String gameName, final String playerName, final Coordinates coordinates) {
        final RestTemplate restTemplate = new RestTemplate();
        final String       url          = "http://" + SERVER_ADDRESS + "/placeMarker?gameName=" + gameName + "&playerName=" + playerName + "&x=" + coordinates.getX() + "&y=" + coordinates.getY();
        return restTemplate.getForObject(url, ServerResponse.class);
    }
}
