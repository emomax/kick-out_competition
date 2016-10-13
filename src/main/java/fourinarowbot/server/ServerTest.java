package fourinarowbot.server;

import fourinarowbot.board.FourInARowbotBoard;
import commons.gameengine.Coordinates;
import fourinarowbot.domain.MarkerColor;
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
        String message = null;
        while (true) {
            //            System.out.println(playerName + " getting board state...");
            final ServerResponse boardStateResponse = server.getBoardState(playerName, gameName);
            if (boardStateResponse.getMessage() != null) {
                message = boardStateResponse.getMessage();
                break;
            }

            System.out.println(playerName + " got board");

            final FourInARowbotBoard board         = new FourInARowbotBoard(boardStateResponse.getBoardState().getMarkers());
            final MarkerColor        myMarkerColor = getMyMarkerColor(playerName, boardStateResponse);
            final Coordinates        coordinates   = playerEngine.getCoordinatesForNextMakerToPlace(board, myMarkerColor);
            //            System.out.println(playerName + " placing marker...");
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

    private static MarkerColor getMyMarkerColor(final String playerName, final ServerResponse response) {
        if (response.getRedPlayerName().equals(playerName)) {
            return MarkerColor.RED;
        }
        return MarkerColor.YELLOW;
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
