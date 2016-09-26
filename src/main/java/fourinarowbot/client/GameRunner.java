package fourinarowbot.client;

import fourinarowbot.gameengine.GameEngine;
import fourinarowbot.gameengine.MyN00bGameEngine;

public class GameRunner {

    public static void main(final String[] args) {
        final String     playerName   = "MrHeyhey";
        final String     gameName     = "MyGame1";
        final GameEngine myGameEngine = new MyN00bGameEngine();

        RemoteGame.startGame(playerName, gameName, myGameEngine);
    }
}
