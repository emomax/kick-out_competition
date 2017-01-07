package spacerace.level;

import java.util.Arrays;
import java.util.List;

import spacerace.domain.Rectangle2D;
import spacerace.domain.Vector2D;

public class Level1 {

    public static Level create() {
        final Rectangle2D       rectangle1 = new Rectangle2D(200, 200, 500, 100);
        final List<Rectangle2D> rectangles = Arrays.asList(rectangle1);

        final Vector2D startPosition = new Vector2D(0, 0);

        return new Level(1, rectangles, startPosition);
    }
}
