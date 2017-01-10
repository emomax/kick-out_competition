package spacerace;

import java.io.IOException;

import org.springframework.web.client.RestTemplate;

import spacerace.client.RemoteGame;
import spacerace.client.RemoteServerAdapter;
import spacerace.gameengine.ManualGameEngine;

public class SpaceRaceTest {

    //    private static final String SERVER_ADDRESS = "127.0.0.1:8080"; // If you run locally
    //    private static final String SERVER_ADDRESS = "10.46.1.42:8080"; // Game server
    private static final String SERVER_ADDRESS = "10.46.0.243:8080"; // Max J

    public static void main(final String[] args) throws InterruptedException, IOException {
        startGameWithMultipleShips();
        //        testServerResponseTime();
    }

    private static void startGameWithMultipleShips() throws IOException, InterruptedException {
        final String gameName = "Battle of Trustly5";
        new Thread(() -> {
            try {
                startGame("Robocop1", gameName);
            }
            catch (final Exception e) {
                e.printStackTrace();
            }
        }).start();
        new Thread(() -> {
            try {
                startGame("Robocop2", gameName);
            }
            catch (final Exception e) {
                e.printStackTrace();
            }
        }).start();
        new Thread(() -> {
            try {
                startGame("Robocop3", gameName);
            }
            catch (final Exception e) {
                e.printStackTrace();
            }
        }).start();
        new Thread(() -> {
            try {
                startGame("Robocop4", gameName);
            }
            catch (final Exception e) {
                e.printStackTrace();
            }
        }).start();
        new Thread(() -> {
            try {
                startGame("Robocop5", gameName);
            }
            catch (final Exception e) {
                e.printStackTrace();
            }
        }).start();
        new Thread(() -> {
            try {
                startGame("Robocop6", gameName);
            }
            catch (final Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    private static void startGame(final String playerName, final String gameName) throws IOException, InterruptedException {
        final RemoteServerAdapter server           = new RemoteServerAdapter(SERVER_ADDRESS, playerName, gameName, 1);
        final RemoteGame          remoteGame       = new RemoteGame(server, playerName, gameName);
        final ManualGameEngine    manualGameEngine = new ManualGameEngine();
        remoteGame.runGame(manualGameEngine, manualGameEngine);
    }

    private static void testServerResponseTime() throws InterruptedException {
        final RestTemplate restTemplate = new RestTemplate();

        final String url = "http://" + SERVER_ADDRESS + "/test";

        for (int i = 0; i < 10000; i++) {
            final long   before    = System.currentTimeMillis();
            final String response  = restTemplate.getForObject(url, String.class);
            final long   totalTime = System.currentTimeMillis() - before;
            System.out.println(response + "    " + totalTime);

            Thread.sleep(100);
        }
    }
}
