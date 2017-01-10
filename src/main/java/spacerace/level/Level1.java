package spacerace.level;

import java.util.ArrayList;
import java.util.List;

import spacerace.domain.Rectangle2D;
import spacerace.domain.Vector2D;

public class Level1 {

    public static Level create() {
        final Rectangle2D start  = new Rectangle2D(100, Level.HEIGHT - 25, 150, 25);
        final Rectangle2D finish = new Rectangle2D(650, Level.HEIGHT - 25, 150, 25);
        final Rectangle2D left   = new Rectangle2D(0, 0, 100, Level.HEIGHT);
        final Rectangle2D up     = new Rectangle2D(100, 0, Level.WIDTH - 100, 100);
        final Rectangle2D down   = new Rectangle2D(250, 300, 400, 300);

        final Rectangle2D right = new Rectangle2D(Level.WIDTH - 100, 100, 100, Level.HEIGHT - 100);

        final List<Rectangle2D> rectangles = new ArrayList<>();
        rectangles.add(left);
        rectangles.add(up);
        rectangles.add(down);
        rectangles.add(start);
        rectangles.add(finish);
        rectangles.add(right);

        final Vector2D startPosition = new Vector2D(150, Level.HEIGHT - 150);

        final Rectangle2D goal = new Rectangle2D(650, Level.HEIGHT - 75, 150, 10);

        return new Level(1, rectangles, startPosition, goal);
    }
}
