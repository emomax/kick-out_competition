package commons.client;

import fourinarowbot.client.FourInARowRemoteGameImpl;
import fourinarowbot.gameengine.GameEngine;
import fourinarowbot.gameengine.MyN00bGameEngine;
import commons.client.RemoteGame;

public class GameRunner {

    public static void main(final String[] args) {
        final String     playerName   = "MrHeyhey";
        final String     gameName     = "MyGame1";
        final GameEngine myGameEngine = new MyN00bGameEngine();

        RemoteGame gameInstance = new FourInARowRemoteGameImpl();
        gameInstance.startGame(playerName, gameName, myGameEngine);
    }
}
