package spacerace.server;

import java.awt.Color;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import spacerace.domain.Acceleration;
import spacerace.domain.GameStatus;
import spacerace.domain.Ship;
import spacerace.domain.Vector2D;
import spacerace.level.Level;

import static java.awt.Color.BLACK;
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

    private final UUID   id;
    private final String name;
    private final ConcurrentHashMap<String, Ship> ships = new ConcurrentHashMap<>();
    private final Level      level;
    private final GameStatus gameStatus;
    private static final List<Color> STANDARD_SHIP_COLORS = asList(RED, BLUE, MAGENTA, GREEN, CYAN, BLACK, WHITE, YELLOW, PINK);

    public SpaceRaceGame(final UUID id, final String name, final Level level) {
        this.id = id;
        this.name = name;
        this.level = level;
        this.gameStatus = GameStatus.JOINABLE;
        final GameCycle gameCycle = new GameCycle(this);
        new Thread(gameCycle).start();
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

    public Collection<Ship> getShips() {
        return ships.values();
    }

    public Level getLevel() {
        return level;
    }

    public void addShip(final String playerName) {
        synchronized (ships) {
            if (ships.get(playerName) != null) {
                throw new IllegalArgumentException("Player already registered");
            }

            final Color shipColor = getNextShipColor();
            final Ship  ship      = new Ship(playerName, shipColor, level.getStartPosition());
            ships.put(ship.getName(), ship);
        }
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
}
