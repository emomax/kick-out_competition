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

        // Check for wall collisions or goal
        final List<Rectangle> rectangles = game.getLevel().getRectangles().stream()
                .map(Rectangle2D::toAWTRectangle)
                .collect(toList());
        for (final Ship ship : game.getShips()) {

            final Rectangle2D shipRectangle2D = new Rectangle2D((int) ship.getX(), (int) ship.getY(), ship.getWidth(), ship.getHeight());
            final Rectangle   shipRectangle   = shipRectangle2D.toAWTRectangle();

            // Goal
            if (shipRectangle.intersects(game.getLevel().getGoal().toAWTRectangle())) {
                ship.reset(game.getLevel().getStartPosition());
                continue;
            }

            // Wall collision
            for (final Rectangle wallRectangle : rectangles) {
                if (shipRectangle.intersects(wallRectangle)) {
                    ship.reset(game.getLevel().getStartPosition());
                }
            }
        }


        // Move ships
        game.getShips().stream().forEach(ship -> ship.move(timeSinceLastCycle));
    }
}
