package fourinarowbot.client;

import org.springframework.web.client.RestTemplate;

import fourinarowbot.board.BoardImpl;
import fourinarowbot.domain.Coordinates;
import fourinarowbot.gameengine.GameEngine;
import fourinarowbot.server.response.ServerResponse;

public class RemoteGame {

    private static final String SERVER_ADDRESS = "127.0.0.1:8080";

    public static void startGame(final String playerName, final String gameName, final GameEngine gameEngine) {
        final ServerResponse serverResponse = runGame(playerName, gameName, gameEngine);
        printGameResult(playerName, serverResponse);
    }

    private static ServerResponse runGame(final String playerName, final String gameName, final GameEngine gameEngine) {
        ServerResponse serverResponse;
        while (true) {
            serverResponse = getBoardState(gameName, playerName);
            if (serverResponse.getMessage() != null) {
                return serverResponse;
            }
            final Coordinates coordinates = getCoordinatesForNextMarkerToPlace(gameEngine, serverResponse);
            serverResponse = placeMarker(gameName, playerName, coordinates);
            if (serverResponse.getMessage() != null) {
                return serverResponse;
            }
        }
    }

    private static Coordinates getCoordinatesForNextMarkerToPlace(final GameEngine gameEngine, final ServerResponse serverResponse) {
        final BoardImpl board = new BoardImpl(serverResponse.getBoardState().getMarkers());
        return gameEngine.getCoordinatesForNextMakerToPlace(board);
    }

    private static void printGameResult(final String playerName, final ServerResponse serverResponse) {
        System.out.println("Game over for " + playerName + ". " + serverResponse.getMessage());
        if (serverResponse.getBoardState() != null) {
            System.out.println("Board state was:");
            new BoardImpl(serverResponse.getBoardState().getMarkers()).print();
        }
    }

    private static ServerResponse getBoardState(final String gameName, final String playerName) {
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
