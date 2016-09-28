package fourinarowbot.client;

import org.springframework.web.client.RestTemplate;

import fourinarowbot.board.BoardImpl;
import fourinarowbot.board.BoardState;
import fourinarowbot.domain.Coordinates;
import fourinarowbot.gameengine.GameEngine;
import fourinarowbot.server.response.ServerResponse;

public class RemoteGame {

    private static final String SERVER_ADDRESS = "127.0.0.1:8080";

    public static void startGame(final String playerName, final String gameName, final GameEngine gameEngine) {
        final String     message;
        final BoardState finalBoardState;
        while (true) {
            final ServerResponse boardStateResponse = getBoardState(gameName, playerName);
            if (boardStateResponse.getMessage() != null) {
                message = boardStateResponse.getMessage();
                finalBoardState = boardStateResponse.getBoardState();
                break;
            }

            final BoardImpl   board       = new BoardImpl(boardStateResponse.getBoardState().getMarkers());
            final Coordinates coordinates = gameEngine.getCoordinatesForNextMakerToPlace(board);

            final ServerResponse placeMarkerResponse = placeMarker(gameName, playerName, coordinates);
            if (placeMarkerResponse.getMessage() != null) {
                finalBoardState = placeMarkerResponse.getBoardState();
                message = placeMarkerResponse.getMessage();
                break;
            }
        }
        System.out.println("Game over for " + playerName + ". " + message);
        if (finalBoardState != null) {
            System.out.println("Board state was:");
            new BoardImpl(finalBoardState.getMarkers()).print();
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
