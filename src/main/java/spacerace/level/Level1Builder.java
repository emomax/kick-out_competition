package spacerace.level;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

import spacerace.domain.Line2D;
import spacerace.domain.Rectangle2D;
import spacerace.domain.Vector2D;
import spacerace.graphics.GraphicsUtils;

class Level1Builder {

    private static final String SHIP_IMAGE_PATH = "../../spacerace/ship2.png";
    private static final String FIRE_IMAGE_PATH = "../../spacerace/fire_50px.png";

    private Level1Builder() {
        // Intentionally empty
    }

    static Level build() {
        final int levelNumber = 1;
        final int timeLimit   = 30_000;
        final int width       = 900;
        final int height      = 600;

        final List<Line2D> trackBorders = createTrackBoarders(width, height);

        final Vector2D startPosition = new Vector2D(150, height - 150);
        final Line2D   goalLine      = new Line2D(650, height - 75, 800, height - 75);

        final Level1Builder        builder          = new Level1Builder();
        final Consumer<Graphics2D> baseLayerPainter = graphics2D -> builder.paintBaseLayer(graphics2D, height, width);

        return Level.Builder.aLevel()
                .withNumber(levelNumber)
                .withTimeLimit(timeLimit)
                .withWidth(width)
                .withHeight(height)
                .withStartPosition(startPosition)
                .withGoalLine(goalLine)
                .withTrackBoarders(trackBorders)
                .withBaseLayerPainter(baseLayerPainter)
                .build();
    }

    private static List<Line2D> createTrackBoarders(final int width, final int height) {
        final Line2D start      = new Line2D(100, height - 25, 250, height - 25);
        final Line2D left       = new Line2D(100, 100, 100, height - 25);
        final Line2D innerLeft  = new Line2D(250, 300, 250, height - 25);
        final Line2D up         = new Line2D(100, 100, width - 100, 100);
        final Line2D down       = new Line2D(250, 300, width - 250, 300);
        final Line2D right      = new Line2D(width - 100, 100, width - 100, height - 25);
        final Line2D innerRight = new Line2D(width - 250, 300, width - 250, height - 25);
        final Line2D end        = new Line2D(650, height - 25, 800, height - 25);

        return Arrays.asList(
                left,
                innerLeft,
                up,
                down,
                start,
                right,
                innerRight,
                end);
    }

    private void paintBaseLayer(final Graphics2D graphics, final int height, final int width) {
        drawLevelRectangles(graphics, height, width);
        drawFinishLine(graphics, height);
    }

    private void drawLevelRectangles(final Graphics2D graphics, final int height, final int width) {
        final Rectangle2D start = new Rectangle2D(100, height - 25, 150, 25);
        final Rectangle2D left  = new Rectangle2D(0, 0, 100, height);
        final Rectangle2D up    = new Rectangle2D(100, 0, width - 100, 100);
        final Rectangle2D down  = new Rectangle2D(250, 300, 400, 300);
        final Rectangle2D right = new Rectangle2D(width - 100, 100, 100, height - 100);
        final Rectangle2D end   = new Rectangle2D(650, height - 25, 150, 25);

        final List<Rectangle2D> rectangles = new ArrayList<>();
        rectangles.add(left);
        rectangles.add(up);
        rectangles.add(down);
        rectangles.add(start);
        rectangles.add(right);
        rectangles.add(end);

        final GradientPaint gradientPaint = new GradientPaint(25, 25, Color.BLUE, 15, 25, Color.BLACK, true);
        rectangles.forEach(rectangle -> GraphicsUtils.drawRectangle(rectangle, gradientPaint, graphics));
    }

    private void drawFinishLine(final Graphics graphics, final int height) {
        final Rectangle2D   finish        = new Rectangle2D(650, height - 75, 150, 10);
        final GradientPaint gradientPaint = new GradientPaint(25, 25, Color.WHITE, 15, 25, Color.BLACK, true);
        GraphicsUtils.drawRectangle(finish, gradientPaint, graphics);
    }
}
