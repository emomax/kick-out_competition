package spacerace.client;

import java.awt.event.KeyListener;
import java.io.IOException;

import spacerace.domain.Action;
import spacerace.domain.GameState;
import spacerace.domain.GameStatistics;
import spacerace.domain.ShipState;
import spacerace.gameengine.ManualGameEngine;
import spacerace.gameengine.SpaceRaceGameEngine;
import spacerace.graphics.SpaceRaceGraphics;
import spacerace.graphics.SpaceRaceGraphicsFactory;
import spacerace.level.Level;

public class RemoteGame {

    private static final int GAME_CYCLE_MIN_TIME = 15;

    private final String              playerName;
    private final RemoteServerAdapter remoteServerAdapter;
    private       SpaceRaceGraphics   spaceRaceGraphics;
    private final GameStatistics gameStatistics = new GameStatistics();

    public RemoteGame(final String serverAddress, final String playerName, final String gameName, final int levelNumber) {
        this.playerName = playerName;
        this.remoteServerAdapter = new RemoteServerAdapter(serverAddress, playerName, gameName, levelNumber);
    }

    String runManualGame() throws IOException, InterruptedException {
        final ManualGameEngine manualGameEngine = new ManualGameEngine();
        return runGame(manualGameEngine, manualGameEngine);
    }

    String runGame(final SpaceRaceGameEngine gameEngine) throws IOException, InterruptedException {
        return runGame(gameEngine, null);
    }

    private String runGame(final SpaceRaceGameEngine gameEngine, final KeyListener keyListener) throws IOException, InterruptedException {
        final Level level = remoteServerAdapter.registerPlayer();
        return runGameLoop(gameEngine, level, keyListener);
    }

    private String runGameLoop(final SpaceRaceGameEngine gameEngine, final Level level, final KeyListener keyListener) throws InterruptedException, IOException {
        while (true) {
            final long timeBeforeCycle = System.currentTimeMillis();

            final GameState gameState = remoteServerAdapter.getGameState();
            updateGraphics(gameState, level, keyListener);
            final Action action = getNextActionFromGameEngine(gameEngine, gameState);
            remoteServerAdapter.postActionToServer(action);
            sleepIfGameCycleTooFast(timeBeforeCycle);
        }
    }

    private void updateGraphics(final GameState gameState, final Level level, final KeyListener keyListener) throws IOException {
        if (spaceRaceGraphics == null) {
            spaceRaceGraphics = SpaceRaceGraphicsFactory.createGraphics(level, keyListener, gameState, gameStatistics);
        }
        spaceRaceGraphics.updateGraphics(gameState);
    }

    private Action getNextActionFromGameEngine(final SpaceRaceGameEngine gameEngine, final GameState gameState) {
        final ShipState playerShipState = gameState.getShipStates().stream()
                .filter(shipState -> shipState.getName().equals(playerName))
                .findAny()
                .orElseThrow(() -> new IllegalStateException("Could not find ship for player: " + playerName));
        return gameEngine.getAction(playerShipState);
    }

    private void sleepIfGameCycleTooFast(final long timeBeforeCycle) throws InterruptedException {
        final long executionTime = System.currentTimeMillis() - timeBeforeCycle;
        if (executionTime < GAME_CYCLE_MIN_TIME) {
            Thread.sleep(GAME_CYCLE_MIN_TIME - executionTime);
        }
        final Long cycleTime = System.currentTimeMillis() - timeBeforeCycle;
        gameStatistics.addCycleTime(cycleTime.intValue());
    }
}
