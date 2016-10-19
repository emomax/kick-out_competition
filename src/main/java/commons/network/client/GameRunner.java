package commons.network.client;

import commons.network.server.Response.ServerResponseBase;
import commons.gameengine.GameEngine;
import treasurehunter.client.TreasureHunterRemoteGameImpl;
import treasurehunter.gameengine.SmartRandomTreasureHunterGameEngine;

public class GameRunner {

    public static void main(final String[] args) {
        final String     playerName   = "original_Max";
        final String     gameName     = "puppy";
        final GameEngine myGameEngine = new SmartRandomTreasureHunterGameEngine();

        RemoteGame         gameInstance = new TreasureHunterRemoteGameImpl();
        ServerResponseBase gameResult = gameInstance.runGame(playerName, gameName, myGameEngine);

        gameInstance.printGameResult(gameResult);
    }
}
