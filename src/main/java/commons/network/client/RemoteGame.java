package commons.network.client;

import commons.gameengine.GameEngine;
import commons.network.server.Response.ServerResponseBase;

public interface RemoteGame {
   ServerResponseBase getBoardState(final String gameName, final String playerName);

   ServerResponseBase runGame(final String playerName, final String gameName, final GameEngine gameEngine);
   void printGameResult(final ServerResponseBase response);
}
