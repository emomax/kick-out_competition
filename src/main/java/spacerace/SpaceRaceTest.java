package spacerace;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.IntSummaryStatistics;

import org.springframework.web.client.RestTemplate;

import spacerace.client.RemoteGame;
import spacerace.client.communication.SocketServerAdapter;
import spacerace.domain.GameStatistics;
import spacerace.gameengine.ManualGameEngine;

public class SpaceRaceTest {

    private static final String SERVER_IP = "127.0.0.1"; // If you run locally
    //    private static final String SERVER_IP = "10.46.1.42"; // Game server WIFI
    //    private static final String SERVER_IP = "10.46.1.111"; // Game server ETHERNET
    //        private static final String SERVER_IP = "10.46.0.243"; // Max J

    public static void main(final String[] args) throws InterruptedException, IOException {
        startGameWithMultipleShips();
        //        testRestServerResponseTime();
        //        testSocketServerResponseTime();
    }

    private static void startGameWithMultipleShips() throws IOException, InterruptedException {
        final String gameName = "Battle of Trustly7";
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
        //        final RemoteServerAdapter server = new RemoteServerAdapter(SERVER_IP, playerName, gameName, 1);
        final SocketServerAdapter server           = new SocketServerAdapter(SERVER_IP, playerName, gameName, 1);
        final RemoteGame          remoteGame       = new RemoteGame(server, playerName, gameName);
        final ManualGameEngine    manualGameEngine = new ManualGameEngine();
        remoteGame.runGame(manualGameEngine, manualGameEngine);
    }

    private static void testRestServerResponseTime() throws InterruptedException {
        final RestTemplate restTemplate = new RestTemplate();

        final String url = "http://" + SERVER_IP + "/test";

        final GameStatistics gameStatistics = new GameStatistics();
        for (int i = 0; i < 100; i++) {
            final long   before    = System.currentTimeMillis();
            final String response  = restTemplate.getForObject(url, String.class);
            final Long   totalTime = System.currentTimeMillis() - before;
            //            System.out.println(response + "    " + totalTime);
            gameStatistics.addCycleTime(totalTime.intValue());

            Thread.sleep(17);
        }
        final IntSummaryStatistics statistics = gameStatistics.getGameCycleStatistics();
        System.out.println("Min: " + statistics.getMin());
        System.out.println("Max: " + statistics.getMax());
        System.out.println("Average: " + statistics.getAverage());
        //        Min: 1
        //        Max: 41
        //        Average: 3.43
        //        Min: 1
        //        Max: 48
        //        Average: 2.98
    }

    private static void testSocketServerResponseTime() throws InterruptedException, IOException {
        final int    PORT          = 9090;
        final String serverAddress = "127.0.0.1";

        try (final Socket socket = new Socket(serverAddress, PORT);
             final ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
             final ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream())
        ) {
            final GameStatistics gameStatistics = new GameStatistics();
            for (int i = 0; i < 100; i++) {
                final long before = System.currentTimeMillis();

                outputStream.writeObject("test");
                try {
                    final String response = (String) inputStream.readObject();
                    //                System.out.println(response);
                }
                catch (final IOException | ClassNotFoundException ex) {
                    ex.printStackTrace();
                }
                final Long totalTime = System.currentTimeMillis() - before;
                //            System.out.println(response + "    " + totalTime);
                gameStatistics.addCycleTime(totalTime.intValue());

                Thread.sleep(17);
            }

            final IntSummaryStatistics statistics = gameStatistics.getGameCycleStatistics();
            System.out.println("Min: " + statistics.getMin());
            System.out.println("Max: " + statistics.getMax());
            System.out.println("Average: " + statistics.getAverage());
            //            Min: 0
            //            Max: 7
            //            Average: 0.27
            //            Min: 0
            //            Max: 6
            //            Average: 0.28
        }
    }
}
