package spacerace.level;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

import spacerace.domain.Line2D;
import spacerace.domain.Rectangle2D;
import spacerace.domain.Vector2D;
import spacerace.graphics.GraphicsUtils;
import spacerace.level.graphics.StarBackground;

class Level3Builder {

    private static final double STARS_ROTATION_ANGULAR_SPEED = 0.05;

    private double starsBackgroundAngle = 0;

    private Level3Builder() {
        // Intentionally empty
    }

    static Level build() {
        final int levelNumber = 3;
        final int timeLimit   = 30_000;
        final int width       = 900;
        final int height      = 700;

        final List<Line2D> trackBorders = createTrackBoarders(width, height);

        final Vector2D startPosition = new Vector2D((width / 2) - 25, height - 113);
        final Line2D   goalLine      = new Line2D((width / 2), 100, (width / 2), 225);

        final StarBackground       starBackground   = new StarBackground(30, width, height, 200);
        final Level3Builder        builder          = new Level3Builder();
        final Consumer<Graphics2D> baseLayerPainter = graphics2D -> builder.paintBaseLayer(graphics2D, height, width, starBackground);

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
        final Line2D upper1 = new Line2D(25, 100, width - 25, 100);
        final Line2D upper2 = new Line2D(150, 225, width - 150, 225);

        final Line2D bottom1 = new Line2D(25, height - 25, width - 25, height - 25);
        final Line2D bottom2 = new Line2D(150, height - 150, width - 150, height - 150);

        final Line2D left1 = new Line2D(25, 100, 25, height - 25);
        final Line2D left2 = new Line2D(150, 225, 150, height - 150);

        final Line2D right1 = new Line2D(width - 25, 100, width - 25, height - 25);
        final Line2D right2 = new Line2D(width - 150, 225, width - 150, height - 150);

        return Arrays.asList(
                upper1,
                upper2,
                bottom1,
                bottom2,
                left1,
                left2,
                right1,
                right2
        );
    }

    private void paintBaseLayer(final Graphics2D graphics, final int height, final int width, final StarBackground starBackground) {
        paintStars(graphics, height, width, starBackground);
        paintTrackBackground(graphics, width);
        drawFinishLine(graphics, width);
    }

    private void paintStars(final Graphics2D graphics, final int height, final int width, final StarBackground starBackground) {
        final AffineTransform old = graphics.getTransform();
        if (starsBackgroundAngle > 360) {
            starsBackgroundAngle = 0;
        }
        graphics.rotate(Math.toRadians(starsBackgroundAngle), -width, -height);
        starsBackgroundAngle += STARS_ROTATION_ANGULAR_SPEED;
        starBackground.paintStars(graphics);
        graphics.setTransform(old);
    }

    private void drawFinishLine(final Graphics graphics, final int width) {
        final Rectangle2D   finish        = new Rectangle2D((width / 2) - 5, 100, 10, 125);
        final GradientPaint gradientPaint = new GradientPaint(25, 25, Color.WHITE, 25, 15, Color.BLACK, true);
        GraphicsUtils.drawRectangle(finish, gradientPaint, graphics);
    }

    private void paintTrackBackground(final Graphics2D graphics, final int width) {
        final Color trackBackgroundColor = new Color(0x000D62);
        final Color colorWithAlpha       = GraphicsUtils.createColorWithAlpha(trackBackgroundColor, 0.6f);
        graphics.setColor(colorWithAlpha);

        // Fill track up -> down
        graphics.fillRect(25, 100, width - 50, 125);
        graphics.fillRect(25, 225, 125, 325);
        graphics.fillRect(width - 150, 225, 125, 325);
        graphics.fillRect(25, 550, 850, 125);
    }
}
