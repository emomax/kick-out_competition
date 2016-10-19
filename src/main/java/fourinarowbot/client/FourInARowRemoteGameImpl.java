package fourinarowbot.client;

import org.springframework.web.client.RestTemplate;

import commons.gameengine.board.Coordinate;
import commons.gameengine.board.PlayerColor;
import commons.network.client.RemoteGame;
import commons.network.server.Response.ServerResponseBase;
import commons.gameengine.Action;
import commons.gameengine.GameEngine;
import fourinarowbot.board.FourInARowbotBoard;
import fourinarowbot.domain.Marker;
import fourinarowbot.gameengine.FourInARowbotGameEngine;
import fourinarowbot.server.response.ServerResponse;

public class FourInARowRemoteGameImpl implements RemoteGame {

    private static final String SERVER_ADDRESS = "10.46.1.193:8080"; // TreasureHunterGame server
    //    private static final String SERVER_ADDRESS = "127.0.0.1:8080"; // If you run locally

    @Override
    public void printGameResult(ServerResponseBase response) {
        System.out.println(response.getMessage());

        if (((ServerResponse) response).getGameStatistics() != null) {
            ((ServerResponse) response).getGameStatistics().print(response.getRedPlayerName(), response.getYellowPlayerName());
        }
    }

    @Override
    public ServerResponseBase runGame(final String playerName, final String gameName, final GameEngine gameEngine) {
        if (!(gameEngine instanceof FourInARowbotGameEngine)) {
            throw new RuntimeException("Error: Four-in-a-row game initiated, but a different engine was specified.");
        }

        ServerResponse serverResponse;
        while (true) {
            serverResponse = getBoardState(gameName, playerName);
            if (serverResponse.getMessage() != null) {
                return serverResponse;
            }

            final Coordinate coordinates = getCoordinatesForNextMarkerToPlace(playerName, (FourInARowbotGameEngine) gameEngine, serverResponse);
            serverResponse = placeMarker(gameName, playerName, coordinates);
            if (serverResponse.getMessage() != null) {
                return serverResponse;
            }
        }
    }

    private static Coordinate getCoordinatesForNextMarkerToPlace(final String playerName, final FourInARowbotGameEngine gameEngine, final ServerResponse response) {
        final FourInARowbotBoard board         = new FourInARowbotBoard(response.getBoardState().getCells());
        final PlayerColor        myMarkerColor = getMyPlayerColor(playerName, response);
        final Action<Marker>     myAction      = gameEngine.getNextMove(board, myMarkerColor);

        return myAction.get().getCoordinates();
    }

    private static PlayerColor getMyPlayerColor(final String playerName, final ServerResponse response) {
        if (response.getRedPlayerName().equals(playerName)) {
            return PlayerColor.RED;
        }
        return PlayerColor.YELLOW;
    }


    public ServerResponse getBoardState(final String gameName, final String playerName) {
        final RestTemplate restTemplate = new RestTemplate();
        final String       url          = "http://" + SERVER_ADDRESS + "/getBoard?gameName=" + gameName + "&playerName=" + playerName;
        return restTemplate.getForObject(url, ServerResponse.class);
    }

    private static ServerResponse placeMarker(final String gameName, final String playerName, final Coordinate coordinates) {
        final RestTemplate restTemplate = new RestTemplate();
        final String       url          = "http://" + SERVER_ADDRESS + "/placeMarker?gameName=" + gameName + "&playerName=" + playerName + "&x=" + coordinates.getX() + "&y=" + coordinates.getY();
        return restTemplate.getForObject(url, ServerResponse.class);
    }
}
