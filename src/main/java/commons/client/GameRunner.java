package commons.client;

import fourinarowbot.client.FourInARowRemoteGameImpl;
import fourinarowbot.gameengine.GameEngine;
import fourinarowbot.gameengine.MyN00bGameEngine;

public class GameRunner {

    public static void main(final String[] args) {
        final String     playerName   = "original_Max";
        final String     gameName     = "puppy";
        final GameEngine myGameEngine = new MyN00bGameEngine();

        RemoteGame gameInstance = new FourInARowRemoteGameImpl();
        gameInstance.startGame(playerName, gameName, myGameEngine);
    }
}
