package spacerace.client;

import java.awt.Dimension;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

import spacerace.domain.Action;
import spacerace.domain.Detector;
import spacerace.domain.DetectorFactory;
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

    public void runManualGame() throws IOException, InterruptedException {
        final ManualGameEngine manualGameEngine = new ManualGameEngine();
        runGame(manualGameEngine, manualGameEngine);
    }

    public void runGame(final SpaceRaceGameEngine gameEngine) throws IOException, InterruptedException {
        runGame(gameEngine, null);
    }

    public void runGame(final SpaceRaceGameEngine gameEngine, final KeyListener manualKeyListener) throws IOException, InterruptedException {
        final ServerResponse response = invokeServerCall(server::registerPlayer, "Exception when trying to register player: " + playerName);
        runGameLoop(gameEngine, response.getLevel(), manualKeyListener);
    }

    private void runGameLoop(final SpaceRaceGameEngine gameEngine, final Level level, final KeyListener manualKeyListener) throws InterruptedException, IOException {
        boolean stop = false;
        while (!stop) {
            final long timeBeforeCycle = System.currentTimeMillis();

            final ServerResponse    response        = invokeServerCall(server::getGameState, "Exception when getting game state for game: " + gameName);
            final GameState         gameState       = response.getGameState();
            final SpaceRaceGraphics graphics        = getGraphics(gameState, level, manualKeyListener);
            final ShipState         playerShipState = getPlayerShip(gameState);
            final List<Detector>    detectors       = getDetectors(level, graphics.getShipImageDimension(), playerShipState);
            playerShipState.setDetectors(detectors);


            if (GameStatus.valueOf(gameState.getGameStatus()) == GameStatus.RUNNING) {
                final Action action = gameEngine.getAction(playerShipState);
                invokeServerCall(() -> server.postActionToServer(action), "Exception when posting action: " + action);
            }
            else if (GameStatus.valueOf(gameState.getGameStatus()) == GameStatus.FINISHED) {
                final ServerResponse resultListResponse = invokeServerCall(server::getGameResult, "Exception when getting result for game: " + gameName);
                graphics.setPlayerResults(resultListResponse.getPlayerResults());
                stop = true;
            }
            graphics.setState(gameState, detectors);
            sleepIfGameCycleTooFast(timeBeforeCycle);
        }
    }

    private ShipState getPlayerShip(final GameState gameState) {
        return gameState.getShipStates().stream()
                .filter(shipState -> shipState.getName().equals(playerName))
                .findAny()
                .orElseThrow(() -> new IllegalStateException("Could not find ship for player: " + playerName));
    }

    private SpaceRaceGraphics getGraphics(final GameState gameState, final Level level, final KeyListener manualKeyListener) throws IOException {
        if (spaceRaceGraphics == null) {
            final GameKeyAdapter    gameKeyAdapter = new GameKeyAdapter(this);
            final List<KeyListener> keyListeners   = Arrays.asList(manualKeyListener, gameKeyAdapter);
            spaceRaceGraphics = SpaceRaceGraphicsFactory.createGraphics(level, keyListeners, gameState, gameStatistics, playerName);
        }
        return spaceRaceGraphics;
    }

    private List<Detector> getDetectors(final Level level, final Dimension shipImageDimension, final ShipState playerShipState) {
        final DetectorFactory detectorFactory = new DetectorFactory(playerShipState, shipImageDimension, level);
        return detectorFactory.getDetectors();
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
