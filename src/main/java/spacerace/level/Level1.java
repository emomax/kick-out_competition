package spacerace.level;

import java.util.ArrayList;
import java.util.List;

import spacerace.domain.Rectangle2D;
import spacerace.domain.Vector2D;

public class Level1 {

    public static Level create() {
        final Rectangle2D rectangle1 = new Rectangle2D(0, 0, 100, Level.HEIGHT);
        final Rectangle2D rectangle2 = new Rectangle2D(100, 0, Level.WIDTH - 100, 100);
        final Rectangle2D rectangle3 = new Rectangle2D(300, 300, Level.WIDTH, Level.HEIGHT);
        final Rectangle2D rectangle4 = new Rectangle2D(100, Level.HEIGHT - 25, 300, 25);
        final Rectangle2D rectangle5 = new Rectangle2D(Level.WIDTH - 25, 100, 25, 300);

        final List<Rectangle2D> rectangles = new ArrayList<>();
        rectangles.add(rectangle1);
        rectangles.add(rectangle2);
        rectangles.add(rectangle3);
        rectangles.add(rectangle4);
        rectangles.add(rectangle5);

        final Vector2D startPosition = new Vector2D(150, Level.HEIGHT - 150);

        return new Level(1, rectangles, startPosition);
    }
}
