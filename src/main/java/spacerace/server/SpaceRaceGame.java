package spacerace.server;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import spacerace.domain.Acceleration;
import spacerace.domain.GameStatus;
import spacerace.domain.PlayerResult;
import spacerace.domain.Ship;
import spacerace.domain.Vector2D;
import spacerace.level.Level;

import static java.awt.Color.BLUE;
import static java.awt.Color.CYAN;
import static java.awt.Color.GREEN;
import static java.awt.Color.MAGENTA;
import static java.awt.Color.PINK;
import static java.awt.Color.RED;
import static java.awt.Color.WHITE;
import static java.awt.Color.YELLOW;
import static java.util.Arrays.asList;

public class SpaceRaceGame {

    public static final  int         JOIN_GAME_TIMEOUT    = 30;
    private static final List<Color> STANDARD_SHIP_COLORS = asList(RED, BLUE, MAGENTA, GREEN, CYAN, WHITE, YELLOW, PINK);

    private final UUID   id;
    private final String name;
    private final ConcurrentHashMap<String, Ship> ships = new ConcurrentHashMap<>();
    private final    Level      level;
    private volatile GameStatus gameStatus;
    private          long       startTime;
    private final Map<String, PlayerResult> playerPositions = new ConcurrentHashMap<>();

    SpaceRaceGame(final UUID id, final String name, final Level level) {
        this.id = id;
        this.name = name;
        this.level = level;
        this.gameStatus = GameStatus.JOINABLE;

        // Schedule game killer if game created but players never started it
        final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.schedule(() -> {
            if (gameStatus.equals(GameStatus.JOINABLE)) {
                gameStatus = GameStatus.CLOSED;
            }
        }, JOIN_GAME_TIMEOUT, TimeUnit.SECONDS);
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public GameStatus getGameStatus() {
        return gameStatus;
    }

    public void startGame() {
        startTime = System.currentTimeMillis();
        final GameCycle gameCycle = new GameCycle(this);
        new Thread(gameCycle).start();
        gameStatus = GameStatus.RUNNING;
    }

    void finishGame() {
        gameStatus = GameStatus.FINISHED;
        // Close game in a moment so that all players have time to discover that the game has finished before it closes
        final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.schedule(() -> gameStatus = GameStatus.CLOSED, 5, TimeUnit.SECONDS);
    }

    public Collection<Ship> getShips() {
        return ships.values();
    }

    public Level getLevel() {
        return level;
    }

    public long getStartTime() {
        return startTime;
    }

    public void addShip(final String playerName) throws IOException {
        synchronized (ships) {
            if (ships.get(playerName) != null) {
                throw new IllegalArgumentException("Player already registered");
            }

            final Color shipColor = getNextShipColor();
            final Ship  ship      = new Ship(playerName, shipColor, level.getStartPosition());
            ships.put(ship.getName(), ship);
        }
        playerPositions.put(playerName, new PlayerResult(playerName, null));
    }

    private Color getNextShipColor() {
        if (ships.keySet().size() < STANDARD_SHIP_COLORS.size()) {
            return STANDARD_SHIP_COLORS.get(ships.keySet().size());
        }
        else {
            // Well this could get interesting....
            final Random random = new Random(255);
            return Color.getHSBColor(random.nextFloat(), random.nextFloat(), random.nextFloat());
        }
    }

    public void updateShipParameters(final String playerName, final Acceleration accelerationX, final Acceleration accelerationY, final boolean stabilize) {
        final Ship ship = ships.get(playerName);

        final int      accelerationDirectionX = convertAcceleration(accelerationX);
        final int      accelerationDirectionY = convertAcceleration(accelerationY);
        final Vector2D accelerationDirection  = new Vector2D(accelerationDirectionX, accelerationDirectionY);
        ship.setAccelerationDirection(accelerationDirection);

        ship.setStabilize(stabilize);
    }

    private int convertAcceleration(final Acceleration acceleration) {
        if (acceleration == Acceleration.POSITIVE) {
            return 1;
        }
        else if (acceleration == Acceleration.NEGATIVE) {
            return -1;
        }
        else {
            return 0;
        }
    }

    public List<PlayerResult> getPlayerPositions() {
        return new ArrayList<>(playerPositions.values());
    }

    // This implementation only supports that a player finishes one time. If we want to support multiple runs,
    // we must save the last start time (and check if new run is better)
    void setShipPassedGoalLine(final Ship ship) {
        final long lapTime;
        if (ship.getResetTime() != null) {
            lapTime = System.currentTimeMillis() - ship.getResetTime();
        }
        else {
            lapTime = System.currentTimeMillis() - startTime;
        }
        final PlayerResult playerResult = new PlayerResult(ship.getName(), lapTime);
        playerPositions.put(ship.getName(), playerResult);
    }
}
