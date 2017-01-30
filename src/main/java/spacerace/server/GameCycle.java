package spacerace.server;

import java.awt.Rectangle;
import java.util.List;

import spacerace.domain.Line2D;
import spacerace.domain.Rectangle2D;
import spacerace.domain.Ship;

import static java.util.stream.Collectors.toList;

public class GameCycle implements Runnable {

    private static final int GAME_CYCLE_SLEEP_TIME = 17;

    private final SpaceRaceGame game;

    public GameCycle(final SpaceRaceGame game) {
        this.game = game;
    }

    public void run() {
        long beforeTime         = System.currentTimeMillis();
        long lastSpriteMovement = System.currentTimeMillis();

        while (true) {
            if (gameTimeIsUp()) {
                game.finishGame();
                break;
            }

            final long timeSinceLastSpriteMovement = System.currentTimeMillis() - lastSpriteMovement;
            runGameCycle(timeSinceLastSpriteMovement);
            lastSpriteMovement = System.currentTimeMillis();

            final long timeDiff  = System.currentTimeMillis() - beforeTime;
            final long sleepTime = GAME_CYCLE_SLEEP_TIME - timeDiff;

            if (sleepTime > 0) {
                sleep(sleepTime);
            }

            beforeTime = System.currentTimeMillis();
        }
    }

    private boolean gameTimeIsUp() {
        return (System.currentTimeMillis() - game.getStartTime()) > game.getLevel().getTimeLimit();
    }

    private void runGameCycle(final long timeSinceLastCycle) {
        final List<java.awt.geom.Line2D> trackBounds = getLevelRectangles();
        for (final Ship ship : game.getShips()) {

            if (ship.isPassedGoal()) {
                continue;
            }

            final Rectangle shipRectangle = createShipRectangle(ship);

            if (shipPassedGoalLine(shipRectangle)) {
                ship.setHasPassedGoalLine();
                game.setShipPassedGoalLine(ship);
                continue;
            }

            checkWallCollisions(trackBounds, ship, shipRectangle);
        }
        moveShips(timeSinceLastCycle);
    }

    private List<java.awt.geom.Line2D> getLevelRectangles() {
        return game.getLevel().getTrackBorders().stream()
                .map(Line2D::convertToAWTLine2D)
                .collect(toList());
    }

    private Rectangle createShipRectangle(final Ship ship) {
        final Rectangle2D shipRectangle2D = new Rectangle2D((int) ship.getX(), (int) ship.getY(), ship.getWidth(), ship.getHeight());
        return shipRectangle2D.toAWTRectangle();
    }

    private boolean shipPassedGoalLine(final Rectangle shipRectangle) {
        return game.getLevel().getGoalLine().convertToAWTLine2D().intersects(shipRectangle);
    }

    private void checkWallCollisions(final List<java.awt.geom.Line2D> trackBounds, final Ship ship, final Rectangle shipRectangle) {
        for (final java.awt.geom.Line2D wallLine : trackBounds) {
            if (wallLine.intersects(shipRectangle)) {
                ship.reset(game.getLevel().getStartPosition());
            }
        }
    }

    private void moveShips(final long timeSinceLastCycle) {
        game.getShips().stream().forEach(ship -> ship.move(timeSinceLastCycle));
    }

    private void sleep(final long sleepTime) {
        try {
            Thread.sleep(sleepTime);
        }
        catch (final InterruptedException e) {
            System.out.println("Interrupted!");
        }
    }
}
