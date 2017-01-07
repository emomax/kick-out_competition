package spacerace.client;

import java.awt.event.KeyListener;
import java.io.IOException;
import java.util.Collection;
import java.util.IntSummaryStatistics;

import org.springframework.web.client.RestTemplate;

import spacerace.domain.Action;
import spacerace.domain.CircularFifoQueue;
import spacerace.domain.GameState;
import spacerace.domain.ShipState;
import spacerace.gameengine.ManualGameEngine;
import spacerace.gameengine.SpaceRaceGameEngine;
import spacerace.graphics.SpaceRaceGraphics;
import spacerace.level.Level;
import spacerace.server.response.ServerResponse;

public class RemoteGame {

    private static final int GAME_CYCLE_MIN_TIME = 15;

    private final String            serverAddress;
    private final String            playerName;
    private final String            gameName;
    private final int               levelNumber;
    private       SpaceRaceGraphics spaceRaceGraphics;
    private final CircularFifoQueue<Integer> latestCycleTimes = new CircularFifoQueue<>(100);

    public RemoteGame(final String serverAddress, final String playerName, final String gameName, final int levelNumber) {
        this.serverAddress = serverAddress;
        this.playerName = playerName;
        this.gameName = gameName;
        this.levelNumber = levelNumber;
    }

    String runManualGame() throws IOException, InterruptedException {
        final ManualGameEngine manualGameEngine = new ManualGameEngine();
        return runGame(manualGameEngine, manualGameEngine);
    }

    String runGame(final SpaceRaceGameEngine gameEngine) throws IOException, InterruptedException {
        return runGame(gameEngine, null);
    }

    private String runGame(final SpaceRaceGameEngine gameEngine, final KeyListener keyListener) throws IOException, InterruptedException {
        final Level level = registerPlayer();

        return runGameLoop(gameEngine, level, keyListener);
    }

    private Level registerPlayer() {
        final RestTemplate restTemplate = new RestTemplate();
        final String url = "http://" + serverAddress
                           + "/registerPlayer"
                           + "?gameName=" + gameName
                           + "&playerName=" + playerName
                           + "&levelNumber=" + levelNumber;

        final ServerResponse response = restTemplate.getForObject(url, ServerResponse.class);
        if (response.getMessage() != null) {
            throw new IllegalStateException("Exception when trying to register player: " + playerName + ", message: " + response.getMessage());
        }
        return response.getLevel();
    }

    private String runGameLoop(final SpaceRaceGameEngine gameEngine, final Level level, final KeyListener keyListener) throws InterruptedException, IOException {
        while (true) {
            final long timeBeforeCycle = System.currentTimeMillis();
            //            final long beforeTime = System.currentTimeMillis();

            // Get game state from server
            final GameState gameState = getGameState(serverAddress, gameName);

            // Calculate update times / FPS statistics
            final Collection<Integer>  integers             = latestCycleTimes.asCollection();
            final IntSummaryStatistics updateTimeStatistics = integers.stream().mapToInt(Integer::intValue).summaryStatistics();

            final String updateTimeText = "Update time"
                                          + "  min: " + updateTimeStatistics.getMin()
                                          + "  max: " + updateTimeStatistics.getMax()
                                          + "  average: " + ((int) updateTimeStatistics.getAverage());
            final String fpsText = "FPS3    "
                                   + "  min: " + (1000 / updateTimeStatistics.getMax())
                                   + "  max: " + (1000 / updateTimeStatistics.getMin())
                                   + "  average: " + ((int) (1000 / updateTimeStatistics.getAverage()));

            // Update graphics
            updateGraphics(gameState, level, keyListener, updateTimeText, fpsText);

            // Get next action from game engine
            final ShipState playerShipState = gameState.getShipStates().stream()
                    .filter(shipState -> shipState.getName().equals(playerName))
                    .findAny()
                    .orElseThrow(() -> new IllegalStateException("Could not find ship for player: " + playerName));
            final Action action = gameEngine.getAction(playerShipState);

            // Post action to server
            postAction(action);

            //            final long cycleTime = System.currentTimeMillis() - beforeTime;
            //
            //            // We limit the load on the server by allowing about 60fps on client side because why not
            //            if (cycleTime < GAME_CYCLE_MIN_TIME) {
            //                System.out.println("####### Sleeping " + (GAME_CYCLE_MIN_TIME - cycleTime) + " because timesincelastupdate is: " + cycleTime);
            //                Thread.sleep(GAME_CYCLE_MIN_TIME - cycleTime);
            //            }
            //            System.out.println("== " + latestUpdateTimes.asCollection().size());
            //            System.out.println("== " + latestUpdateTimes.asCollection());

            final long executionTime = System.currentTimeMillis() - timeBeforeCycle;
            if (executionTime < GAME_CYCLE_MIN_TIME) {
                Thread.sleep(GAME_CYCLE_MIN_TIME - executionTime);
            }
            final Long cycleTime = System.currentTimeMillis() - timeBeforeCycle;
            latestCycleTimes.add(cycleTime.intValue());
        }
    }

    private void updateGraphics(final GameState gameState, final Level level, final KeyListener keyListener, final String updateTimeText, final String fpsText) throws IOException {
        if (spaceRaceGraphics == null) {
            spaceRaceGraphics = new SpaceRaceGraphics(level, keyListener, gameState);
        }
        spaceRaceGraphics.updateGraphics(gameState, updateTimeText, fpsText);
    }

    private GameState getGameState(final String serverAddress, final String gameName) {
        final RestTemplate restTemplate = new RestTemplate();
        final String       url          = "http://" + serverAddress + "/getGameState?gameName=" + gameName;

        final ServerResponse response = restTemplate.getForObject(url, ServerResponse.class);
        if (response.getMessage() != null) {
            throw new IllegalStateException("Exception when getting game state for game: " + gameName + ", message: " + response.getMessage());
        }
        return response.getGameState();
    }

    private void postAction(final Action action) {
        final RestTemplate restTemplate = new RestTemplate();
        final String url = "http://" + serverAddress
                           + "/action"
                           + "?gameName=" + gameName
                           + "&playerName=" + playerName
                           + "&accelerationX=" + action.getAccelerationX()
                           + "&accelerationY=" + action.getAccelerationY()
                           + "&stabilize=" + action.isStabilize();

        final ServerResponse response = restTemplate.getForObject(url, ServerResponse.class);
        if (response.getMessage() != null) {
            throw new IllegalStateException("Exception when posting action: " + action + ", message: " + response.getMessage());
        }
    }
}
