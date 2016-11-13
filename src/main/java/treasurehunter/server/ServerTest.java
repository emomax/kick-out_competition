package treasurehunter.server;

import commons.gameengine.Action;
import commons.gameengine.board.PlayerColor;
import commons.network.client.RemoteGame;
import commons.network.server.Response.ServerResponseBase;
import commons.network.server.ServerRestController;
import treasurehunter.board.TreasureHunterBoard;
import treasurehunter.client.TreasureHunterRemoteGameImpl;
import treasurehunter.domain.Move;
import treasurehunter.gameengine.SmartRandomTreasureHunterGameEngine;
import treasurehunter.gameengine.TreasureHunterGameEngine;
import treasurehunter.server.response.ServerResponse;

public class ServerTest {

    public static void main(final String[] args) {
        final String               gameName = "myGame2";

        new Thread(() -> {
            String                               playerName   = "Duke Fancypants";
            final SmartRandomTreasureHunterGameEngine playerEngine = new SmartRandomTreasureHunterGameEngine();
            RemoteGame                           gameInstance = new TreasureHunterRemoteGameImpl();
            ServerResponseBase                   gameResult   = gameInstance.runGame(playerName, gameName, playerEngine);

            gameInstance.printGameResult(gameResult);
        }).start();

        new Thread(() -> {
            String                                    playerName   = "Sergeant Slick";
            final SmartRandomTreasureHunterGameEngine playerEngine = new SmartRandomTreasureHunterGameEngine();
            RemoteGame         gameInstance = new TreasureHunterRemoteGameImpl();
            ServerResponseBase gameResult   = gameInstance.runGame(playerName, gameName, playerEngine);

            gameInstance.printGameResult(gameResult);
        }).start();
    }

    /**
     *   Used to test the server locally, without an actual connection.
     */
    @SuppressWarnings("unused")
    private static void startPlaying(final ServerRestController server, final String gameName, final String playerName, final TreasureHunterGameEngine playerEngine) {
        String message;
        while (true) {
            final ServerResponse boardStateResponse = (ServerResponse) server.getTreasureHunterBoardState(playerName, gameName);
            if (boardStateResponse.getMessage() != null) {
                message = boardStateResponse.getMessage();
                break;
            }

            final TreasureHunterBoard board         = new TreasureHunterBoard(boardStateResponse.getBoardState().getCells());
            final PlayerColor         myPlayerColor = getMyPlayerColor(playerName, boardStateResponse);
            final Action<Move>        move          = playerEngine.getNextMove(board, myPlayerColor);
            final ServerResponse movePlayerResponse = (ServerResponse) server.move(playerName, gameName, move.get().toString());

            System.out.println(playerName + " moved: " + move.get().toString());
            board.print();
            if (movePlayerResponse.getMessage() != null) {
                board.print();
                message = movePlayerResponse.getMessage();
                break;
            }
            sleep(30L);
        }
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
