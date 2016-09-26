package fourinarowbot.client;

import org.springframework.web.client.RestTemplate;

import fourinarowbot.board.BoardImpl;
import fourinarowbot.domain.Coordinates;
import fourinarowbot.gameengine.GameEngine;
import fourinarowbot.server.response.GetBoardResponse;
import fourinarowbot.server.response.PlaceMarkerResponse;

public class RemoteGame {

    private static final String SERVER_ADDRESS = "127.0.0.1:8080";

    public static void startGame(final String playerName, final String gameName, final GameEngine gameEngine) {
        final String message;
        while (true) {
            final GetBoardResponse boardStateResponse = getBoardState(gameName, playerName);
            if (boardStateResponse.getMessage() != null) {
                message = boardStateResponse.getMessage();
                break;
            }

            final BoardImpl   board       = new BoardImpl(boardStateResponse.getBoardState().getMarkers());
            final Coordinates coordinates = gameEngine.getCoordinatesForNextMakerToPlace(board);

            final PlaceMarkerResponse placeMarkerResponse = placeMarker(gameName, playerName, coordinates);
            if (placeMarkerResponse.getMessage() != null) {
                board.print();
                message = placeMarkerResponse.getMessage();
                break;
            }
        }
        System.out.println("Game over for " + playerName + " " + message);
    }

    private static GetBoardResponse getBoardState(final String gameName, final String playerName) {
        final RestTemplate restTemplate = new RestTemplate();
        final String       url          = "http://" + SERVER_ADDRESS + "/getBoard?gameName=" + gameName + "&playerName=" + playerName;
        return restTemplate.getForObject(url, GetBoardResponse.class);
    }

    private static PlaceMarkerResponse placeMarker(final String gameName, final String playerName, final Coordinates coordinates) {
        final RestTemplate restTemplate = new RestTemplate();
        final String       url          = "http://" + SERVER_ADDRESS + "/placeMarker?gameName=" + gameName + "&playerName=" + playerName + "&x=" + coordinates.getX() + "&y=" + coordinates.getY();
        return restTemplate.getForObject(url, PlaceMarkerResponse.class);
    }
}
