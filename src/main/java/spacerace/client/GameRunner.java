package spacerace.client;

import java.io.IOException;
import java.util.logging.Handler;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import spacerace.client.communication.ServerAdapter;
import spacerace.client.communication.SocketServerAdapter;
import spacerace.gameengine.MyN00bGameEngine;
import spacerace.gameengine.SpaceRaceGameEngine;

public class GameRunner {

    private static final String SERVER_IP = "127.0.0.1"; // If you run locally
    //    private static final String SERVER_IP = "10.46.1.42"; // Game server WIFI
    //    private static final String SERVER_IP = "10.46.1.111"; // Game server ETHERNET
    //    private static final String SERVER_IP = "10.46.0.243"; // Max J
    //    private static final String SERVER_IP = "192.168.1.174"; // Other

    public static void main(final String[] args) throws IOException, InterruptedException {
        setLogLevel();

        final String              playerName   = "n00b";
        final String              gameName     = "battleOfTrustly";
        final int                 levelNumber  = 1;
        final SpaceRaceGameEngine myGameEngine = new MyN00bGameEngine();

        final ServerAdapter server = new SocketServerAdapter(SERVER_IP, playerName, gameName, levelNumber);
        //        final ServerAdapter server     = new RemoteServerAdapter(SERVER_IP, playerName, gameName, levelNumber);
        final RemoteGame remoteGame = new RemoteGame(server, playerName, gameName);
        //        final String gameResult = remoteGame.runGame(myGameEngine);
        remoteGame.runManualGame();
    }

    private static void setLogLevel() {
        final Logger log = LogManager.getLogManager().getLogger("");
        for (final Handler handler : log.getHandlers()) {
            handler.setLevel(java.util.logging.Level.WARNING);
        }
    }
}
