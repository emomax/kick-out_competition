package fourinarowbot.server;

import commons.gameengine.board.PlayerColor;
import commons.network.server.ServerRestController;
import fourinarowbot.board.FourInARowbotBoard;
import commons.gameengine.board.Coordinate;
import fourinarowbot.gameengine.MyN00bGameEngine;
import fourinarowbot.server.response.ServerResponse;

public class ServerTest {

    public static void main(final String[] args) {
        final ServerRestController server   = new ServerRestController();
        final String               gameName = "myGame1";

        new Thread(() -> {
            String                 playerName   = "Player1";
            final MyN00bGameEngine playerEngine = new MyN00bGameEngine();
            startPlaying(server, gameName, playerName, playerEngine);
        }).start();

        new Thread(() -> {
            String                 playerName   = "Player2";
            final MyN00bGameEngine playerEngine = new MyN00bGameEngine();
            startPlaying(server, gameName, playerName, playerEngine);
        }).start();
    }

    private static void startPlaying(final ServerRestController server, final String gameName, final String playerName, final MyN00bGameEngine playerEngine) {
        String message;
        while (true) {
            final ServerResponse boardStateResponse = server.getBoardState(playerName, gameName);
            if (boardStateResponse.getMessage() != null) {
                message = boardStateResponse.getMessage();
                break;
            }

            System.out.println(playerName + " got board");

            final FourInARowbotBoard board         = new FourInARowbotBoard(boardStateResponse.getBoardState().getCells());
            final PlayerColor        myPlayerColor = getMyPlayerColor(playerName, boardStateResponse);
            final Coordinate         coordinates   = playerEngine.getCoordinatesForNextMakerToPlace(board, myPlayerColor);
            final ServerResponse placeMarkerResponse = server.placeMarker(playerName, gameName, coordinates.getX(), coordinates.getY());
            System.out.println(playerName + " placed marker");
            if (placeMarkerResponse.getMessage() != null) {
                board.print();
                message = placeMarkerResponse.getMessage();
                break;
            }
            sleep(500);
        }
        System.out.println(playerName + " stopped playing after message: " + message);
    }

    private static PlayerColor getMyPlayerColor(final String playerName, final ServerResponse response) {
        if (response.getRedPlayerName().equals(playerName)) {
            return PlayerColor.RED;
        }
        return PlayerColor.YELLOW;
    }

    private static void sleep(final long time) {
        try {
            Thread.sleep(time);
        }
        catch (final InterruptedException e) {
            e.printStackTrace();
        }
    }
}
