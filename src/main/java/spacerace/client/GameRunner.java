package spacerace.client;

import java.io.IOException;

import spacerace.gameengine.MyN00bGameEngine;
import spacerace.gameengine.SpaceRaceGameEngine;

public class GameRunner {

    private static final String SERVER_ADDRESS = "127.0.0.1:8080"; // If you run locally
    //    private static final String SERVER_ADDRESS = "10.46.1.193:8080"; // Game server

    public static void main(final String[] args) throws IOException, InterruptedException {
        final String              playerName   = "n00b";
        final String              gameName     = "battleOfTrustly";
        final int                 levelNumber  = 1;
        final SpaceRaceGameEngine myGameEngine = new MyN00bGameEngine();

        final RemoteGame remoteGame = new RemoteGame(SERVER_ADDRESS, playerName, gameName, levelNumber);
        //        final String gameResult = remoteGame.runGame(myGameEngine);
        final String gameResult = remoteGame.runManualGame();

        System.out.println(gameResult);
    }
}
