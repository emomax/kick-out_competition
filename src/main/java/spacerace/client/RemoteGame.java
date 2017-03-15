package spacerace.client;

import java.awt.event.KeyListener;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

import spacerace.client.communication.ServerAdapter;
import spacerace.domain.Action;
import spacerace.domain.Detector;
import spacerace.domain.DetectorFactory;
import spacerace.domain.GameState;
import spacerace.domain.GameStatus;
import spacerace.domain.Missile;
import spacerace.domain.ShipState;
import spacerace.domain.Statistics;
import spacerace.domain.Vector2D;
import spacerace.gameengine.ManualGameEngine;
import spacerace.gameengine.SpaceRaceGameEngine;
import spacerace.graphics.GraphicsFactory;
import spacerace.graphics.PlayerGraphics;
import spacerace.level.Level;
import spacerace.level.LevelRepository;
import spacerace.level.ShipGraphics;
import spacerace.server.communication.response.ServerResponse;

import static java.util.stream.Collectors.toList;

public class RemoteGame {

    private static final int GAME_CYCLE_MIN_TIME = 15;

    private final String         gameName;
    private final String         playerName;
    private final ServerAdapter  server;
    private final Level          level;
    private       PlayerGraphics playerGraphics;
    private final Statistics gameCycleStatistics    = new Statistics();
    private final Statistics responseTimeStatistics = new Statistics();

    public RemoteGame(final ServerAdapter server, final String playerName, final String gameName, final int levelNumber) {
        this.gameName = gameName;
        this.playerName = playerName;
        this.server = server;
        this.level = LevelRepository.getLevel(levelNumber);
    }

    void runManualGame() throws IOException, InterruptedException {
        final ManualGameEngine manualGameEngine = new ManualGameEngine();
        runGame(manualGameEngine, manualGameEngine);
    }

    public void runGame(final SpaceRaceGameEngine gameEngine) throws IOException, InterruptedException {
        runGame(gameEngine, null);
    }

    public void runGame(final SpaceRaceGameEngine gameEngine, final KeyListener manualKeyListener) throws IOException, InterruptedException {
        invokeServerCall(server::registerPlayer, "Exception when trying to register player: " + playerName);
        runGameLoop(gameEngine, manualKeyListener);
    }

    private void runGameLoop(final SpaceRaceGameEngine gameEngine, final KeyListener manualKeyListener) throws InterruptedException, IOException {
        boolean stop = false;
        while (!stop) {
            final long timeBeforeCycle = System.currentTimeMillis();

            final ServerResponse response                  = invokeServerCall(server::getGameState, "Exception when getting game state for game: " + gameName);
            final GameState      gameState                 = response.getGameState();
            final PlayerGraphics graphics                  = getPlayerGraphics(gameState, manualKeyListener);
            final ShipState      playerShipState           = getPlayerShip(gameState);
            final List<Vector2D> otherShipPositions        = getOtherShipPositions(playerShipState, gameState);
            final List<Vector2D> otherShipMissilePositions = getOtherShipsMissilePositions(playerShipState, gameState);
            final List<Detector> detectors                 = getDetectors(level.getShipGraphics(), playerShipState);
            playerShipState.setDetectors(detectors);

            if (GameStatus.valueOf(gameState.getGameStatus()) == GameStatus.RUNNING) {
                final Action action = gameEngine.getAction(playerShipState, otherShipPositions, otherShipMissilePositions);
                invokeServerCall(() -> server.postActionToServer(action), "Exception when posting action: " + action);
            }
            else if (GameStatus.valueOf(gameState.getGameStatus()) == GameStatus.FINISHED) {
                final ServerResponse resultListResponse = invokeServerCall(server::getGameResult, "Exception when getting result for game: " + gameName);
                graphics.setPlayerResults(resultListResponse.getPlayerResults());
                stop = true;
            }
            graphics.setState(gameState);
            graphics.setDetectors(detectors);
            sleepIfGameCycleTooFast(timeBeforeCycle);
        }
    }

    private ShipState getPlayerShip(final GameState gameState) {
        return gameState.getShipStates().stream()
                .filter(shipState -> shipState.getName().equals(playerName))
                .findAny()
                .orElseThrow(() -> new IllegalStateException("Could not find ship for player: " + playerName));
    }

    private List<Vector2D> getOtherShipPositions(final ShipState shipStateToExclude, final GameState gameState) {
        final double shipWidth  = level.getShipGraphics().getWidth() / 2.0;
        final double shipHeight = level.getShipGraphics().getHeight() / 2.0;
        return gameState.getShipStates().stream()
                .filter(shipState -> shipState != shipStateToExclude)
                .map(shipState -> getCenter(shipState.getPosition(), shipWidth, shipHeight))
                .collect(toList());
    }

    private List<Vector2D> getOtherShipsMissilePositions(final ShipState shipStateToExclude, final GameState gameState) {
        return gameState.getShipStates().stream()
                .filter(shipState -> shipState != shipStateToExclude)
                .filter(shipState -> shipState.getMissilePosition() != null)
                .map(shipState -> getCenter(shipState.getMissilePosition(), Missile.RADIUS, Missile.RADIUS))
                .collect(toList());
    }

    private Vector2D getCenter(final Vector2D position, final double halfWidth, final double halfHeight) {
        final double centerX = position.getX() + halfWidth;
        final double centerY = position.getY() + halfHeight;
        return new Vector2D(centerX, centerY);
    }

    private PlayerGraphics getPlayerGraphics(final GameState gameState, final KeyListener manualKeyListener) throws IOException {
        if (playerGraphics == null) {
            final StartGameKeyAdapter startGameKeyAdapter = new StartGameKeyAdapter(this);
            final List<KeyListener>   keyListeners        = Arrays.asList(manualKeyListener, startGameKeyAdapter);
            playerGraphics = GraphicsFactory.createPlayerGraphics(level, keyListeners, gameState, gameCycleStatistics, responseTimeStatistics, playerName);
        }
        return playerGraphics;
    }

    private List<Detector> getDetectors(final ShipGraphics shipGraphics, final ShipState playerShipState) {
        final DetectorFactory detectorFactory = new DetectorFactory(playerShipState, shipGraphics, level);
        return detectorFactory.getDetectors();
    }

    private void sleepIfGameCycleTooFast(final long timeBeforeCycle) throws InterruptedException {
        final long executionTime = System.currentTimeMillis() - timeBeforeCycle;
        if (executionTime < GAME_CYCLE_MIN_TIME) {
            Thread.sleep(GAME_CYCLE_MIN_TIME - executionTime);
        }
        final Long cycleTime = System.currentTimeMillis() - timeBeforeCycle;
        gameCycleStatistics.add(cycleTime.intValue());
    }

    void sendStartCommand() {
        invokeServerCall(server::sendStartCommand, "Exception when starting game: " + gameName);
    }

    private ServerResponse invokeServerCall(final Supplier<ServerResponse> supplier, final String failMessage) {
        final long           beforeTime = System.currentTimeMillis();
        final ServerResponse response   = supplier.get();
        if (response.getErrorMessage() != null) {
            throw new IllegalStateException(failMessage + ". Error message: " + response.getErrorMessage());
        }
        final Long responseTime = System.currentTimeMillis() - beforeTime;
        responseTimeStatistics.add(responseTime.intValue());
        return response;
    }
}
