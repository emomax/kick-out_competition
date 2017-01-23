package spacerace.client;

import java.io.IOException;

import spacerace.client.communication.ServerAdapter;
import spacerace.client.communication.SocketServerAdapter;

public class GameRunner {

    private static final String SERVER_IP = "127.0.0.1"; // If you run locally
    //    private static final String SERVER_IP = "10.46.1.42"; // Game server WIFI
    //    private static final String SERVER_IP = "10.46.1.111"; // Game server ETHERNET
    //    private static final String SERVER_IP = "192.168.1.174"; // Other

    public static void main(final String[] args) throws IOException, InterruptedException {
        final String playerName  = "n00b";
        final String gameName    = "BattleOfTrustly";
        final int    levelNumber = 1;

        // Choose how you want to connect. Direct socket or REST
        final ServerAdapter server = new SocketServerAdapter(SERVER_IP, playerName, gameName, levelNumber);
        //        final ServerAdapter server = new RestServerAdapter(SERVER_IP, playerName, gameName, levelNumber);

        final RemoteGame remoteGame = new RemoteGame(server, playerName, gameName, levelNumber);

        // Run manual game or with your game engine
        //        remoteGame.runGame(new MyN00bGameEngine());
        remoteGame.runManualGame();
    }
}
