package commons.client;

import fourinarowbot.gameengine.GameEngine;
import fourinarowbot.server.response.ServerResponse;

public interface RemoteGame {
    void startGame(final String playerName, final String gameName, final GameEngine gameEngine);

    ServerResponse runGame(final String playerName, final String gameName, final GameEngine gameEngine);
    void printGameResult(final ServerResponse response);
    ServerResponse getBoardState(final String gameName, final String playerName);
}
