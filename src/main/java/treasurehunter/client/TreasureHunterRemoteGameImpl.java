package treasurehunter.client;

import org.springframework.web.client.RestTemplate;

import commons.gameengine.board.PlayerColor;
import commons.network.client.RemoteGame;
import commons.gameengine.GameEngine;
import commons.network.server.Response.ServerResponseBase;
import treasurehunter.domain.Move;
import treasurehunter.gameengine.TreasureHunterGameEngine;
import treasurehunter.server.response.ServerResponse;
import treasurehunter.board.TreasureHunterBoard;
import treasurehunter.domain.TreasureHunterAction;

public class TreasureHunterRemoteGameImpl implements RemoteGame {
    //private static final String SERVER_ADDRESS = "127.0.0.1:8080";
    private static final String SERVER_ADDRESS = "10.46.1.193:8080";

    @Override
    public ServerResponseBase runGame(final String playerName, final String gameName, final GameEngine gameEngine) {
        if (!(gameEngine instanceof TreasureHunterGameEngine)) {
            throw new RuntimeException("Wrong engine type for specified game!");
        }

        ServerResponse serverResponse;

        while (true) {
            serverResponse = getBoardState(gameName, playerName);
            if (serverResponse.getMessage() != null) {
                return serverResponse;
            }

            final Move move = getActionForNextMove(playerName, (TreasureHunterGameEngine) gameEngine, serverResponse);
            serverResponse = movePlayer(playerName, gameName, new TreasureHunterAction(move));
            if (serverResponse.getMessage() != null) {
                return serverResponse;
            }
        }
    }

    private Move getActionForNextMove(final String playerName, final TreasureHunterGameEngine gameEngine, final ServerResponse response) {
        final TreasureHunterBoard board = new TreasureHunterBoard(response.getBoardState().getCells());
        final PlayerColor currentPlayerColor = getMyColor(playerName, response);

        return gameEngine.getNextMove(board, currentPlayerColor).get();
    }

    private static PlayerColor getMyColor(final String playerName, final ServerResponse serverResponse) {
        if (serverResponse.getRedPlayerName().equals(playerName)) {
            return PlayerColor.RED;
        }
        return PlayerColor.YELLOW;
    }

    @Override
    public void printGameResult(final ServerResponseBase response) {
        //System.out.println(response.getMessage());
        /*if (((ServerResponse) response).getMoveHistory() != null) {

        }*/
    }

    @Override
    public ServerResponse getBoardState(final String gameName, final String playerName) {
        final RestTemplate restTemplate = new RestTemplate();
        final String       url          = "http://" + SERVER_ADDRESS + "/getTreasureHunterBoard?gameName=" + gameName + "&playerName=" + playerName;
        return restTemplate.getForObject(url, ServerResponse.class);
    }

    private static ServerResponse movePlayer(final String playerName, final String gameName, final TreasureHunterAction action) {
        final RestTemplate restTemplate = new RestTemplate();
        final String url = "http://" + SERVER_ADDRESS + "/" + action.getMethod() + "?gameName=" + gameName + "&playerName=" + playerName + "&" + action.get().toGetValueString();

        return restTemplate.getForObject(url, ServerResponse.class);
    }
}
