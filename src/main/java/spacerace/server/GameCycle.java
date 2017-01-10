package spacerace.server;

import java.awt.Rectangle;
import java.util.List;

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
            final long timeSinceLastSpriteMovement = System.currentTimeMillis() - lastSpriteMovement;
            animationCycle(timeSinceLastSpriteMovement);
            lastSpriteMovement = System.currentTimeMillis();

            final long timeDiff  = System.currentTimeMillis() - beforeTime;
            final long sleepTime = GAME_CYCLE_SLEEP_TIME - timeDiff;

            if (sleepTime > 0) {
                sleep(sleepTime);
            }

            beforeTime = System.currentTimeMillis();
        }
    }

    private void sleep(final long sleepTime) {
        try {
            Thread.sleep(sleepTime);
        }
        catch (final InterruptedException e) {
            System.out.println("Interrupted!");
        }
    }


    private void animationCycle(final long timeSinceLastCycle) {
        final List<Rectangle> rectangles = getLevelRectangles();
        for (final Ship ship : game.getShips()) {
            final Rectangle shipRectangle = createShipRectangle(ship);

            if (shipPassedGoalLine(shipRectangle)) {
                ship.reset(game.getLevel().getStartPosition());
                continue;
            }

            checkWallCollisions(rectangles, ship, shipRectangle);
        }
        moveShips(timeSinceLastCycle);
    }

    private List<Rectangle> getLevelRectangles() {
        return game.getLevel().getRectangles().stream()
                .map(Rectangle2D::toAWTRectangle)
                .collect(toList());
    }

    private Rectangle createShipRectangle(final Ship ship) {
        final Rectangle2D shipRectangle2D = new Rectangle2D((int) ship.getX(), (int) ship.getY(), ship.getWidth(), ship.getHeight());
        return shipRectangle2D.toAWTRectangle();
    }

    private boolean shipPassedGoalLine(final Rectangle shipRectangle) {
        return shipRectangle.intersects(game.getLevel().getGoal().toAWTRectangle());
    }

    private void checkWallCollisions(final List<Rectangle> rectangles, final Ship ship, final Rectangle shipRectangle) {
        for (final Rectangle wallRectangle : rectangles) {
            if (shipRectangle.intersects(wallRectangle)) {
                ship.reset(game.getLevel().getStartPosition());
            }
        }
    }

    private void moveShips(final long timeSinceLastCycle) {
        game.getShips().stream().forEach(ship -> ship.move(timeSinceLastCycle));
    }
}
