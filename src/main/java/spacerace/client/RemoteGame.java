package spacerace.client;

import java.awt.event.KeyListener;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

import spacerace.domain.Action;
import spacerace.domain.GameState;
import spacerace.domain.GameStatistics;
import spacerace.domain.GameStatus;
import spacerace.domain.ShipState;
import spacerace.gameengine.ManualGameEngine;
import spacerace.gameengine.SpaceRaceGameEngine;
import spacerace.graphics.SpaceRaceGraphics;
import spacerace.graphics.SpaceRaceGraphicsFactory;
import spacerace.level.Level;
import spacerace.server.response.ServerResponse;

public class RemoteGame {

    private static final int GAME_CYCLE_MIN_TIME = 15;

    private final String            gameName;
    private final String            playerName;
    private final ServerAdapter     server;
    private       SpaceRaceGraphics spaceRaceGraphics;
    private final GameStatistics gameStatistics = new GameStatistics();

    public RemoteGame(final ServerAdapter server, final String playerName, final String gameName) {
        this.gameName = gameName;
        this.playerName = playerName;
        this.server = server;
    }

    String runManualGame() throws IOException, InterruptedException {
        final ManualGameEngine manualGameEngine = new ManualGameEngine();
        return runGame(manualGameEngine, manualGameEngine);
    }

    public String runGame(final SpaceRaceGameEngine gameEngine) throws IOException, InterruptedException {
        return runGame(gameEngine, null);
    }

    public String runGame(final SpaceRaceGameEngine gameEngine, final KeyListener manualKeyListener) throws IOException, InterruptedException {
        //        final Level level = server.registerPlayer();
        final ServerResponse response = invokeServerCall(server::registerPlayer, "Exception when trying to register player: " + playerName);
        return runGameLoop(gameEngine, response.getLevel(), manualKeyListener);
    }

    private String runGameLoop(final SpaceRaceGameEngine gameEngine, final Level level, final KeyListener manualKeyListener) throws InterruptedException, IOException {
        while (true) {
            final long timeBeforeCycle = System.currentTimeMillis();

            final ServerResponse response  = invokeServerCall(server::getGameState, "Exception when getting game state for game: " + gameName);
            final GameState      gameState = response.getGameState();
            updateGraphics(gameState, level, manualKeyListener);

            if (GameStatus.valueOf(gameState.getGameStatus()) == GameStatus.RUNNING) {
                final Action action = getNextActionFromGameEngine(gameEngine, gameState);
                invokeServerCall(() -> server.postActionToServer(action), "Exception when posting action: " + action);
            }
            sleepIfGameCycleTooFast(timeBeforeCycle);
        }
    }

    private void updateGraphics(final GameState gameState, final Level level, final KeyListener manualKeyListener) throws IOException {
        if (spaceRaceGraphics == null) {
            final GameKeyAdapter    gameKeyAdapter = new GameKeyAdapter(this);
            final List<KeyListener> keyListeners   = Arrays.asList(manualKeyListener, gameKeyAdapter);
            spaceRaceGraphics = SpaceRaceGraphicsFactory.createGraphics(level, keyListeners, gameState, gameStatistics, playerName);
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

    public void sendStartCommand() {
        invokeServerCall(server::sendStartCommand, "Exception when starting game: " + gameName);
    }

    private ServerResponse invokeServerCall(final Supplier<ServerResponse> supplier, final String failMessage) {
        final ServerResponse response = supplier.get();
        if (response.getErrorMessage() != null) {
            throw new IllegalStateException(failMessage + ". Error message: " + response.getErrorMessage());
        }
        return response;
    }
}
