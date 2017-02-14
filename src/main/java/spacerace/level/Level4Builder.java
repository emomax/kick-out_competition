package spacerace.level;


import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import spacerace.domain.Line2D;
import spacerace.domain.Rectangle2D;
import spacerace.domain.Vector2D;
import spacerace.graphics.GraphicsUtils;
import spacerace.level.graphics.StarBackground;

class Level4Builder {

    private static final double STARS_ROTATION_ANGULAR_SPEED = 0.05;

    private double starsBackgroundAngle = 0;

    private Level4Builder() {
        // Intentionally empty
    }

    static Level build() {
        final int levelNumber = 4;
        final int timeLimit   = 120_000;
        final int width       = 900;
        final int height      = 700;

        final List<Line2D> trackBorders = createTrackBoarders(width, height);

        final int innerBoxWidth = width / 7;
        final int innerBoxHeight = height / 7;

        final Vector2D startPosition = new Vector2D(width - 100, height - 100);
        final Line2D   goalLine      = new Line2D(2 * innerBoxWidth, 3 * innerBoxHeight + innerBoxHeight/2,
                                                  3 * innerBoxWidth, 3 * innerBoxHeight + innerBoxHeight/2);

        final StarBackground       starBackground   = new StarBackground(30, width, height, 200);
        final Level4Builder        builder          = new Level4Builder();
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
        final List<Line2D> resultList = new ArrayList<>();

        resultList.addAll(generateLineBox(0, 0, width, height));

        final int innerBoxWidth = width / 7;
        final int innerBoxHeight = height / 7;

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                resultList.addAll(generateLineBox(innerBoxWidth + 2 * i * innerBoxWidth,
                                                  innerBoxHeight + 2 * j * innerBoxHeight,
                                                  innerBoxWidth, innerBoxHeight));
            }
        }

        return resultList;
    }

    private static List<Line2D> generateLineBox(final int x, final int y, final int width, final int height) {
        final List<Line2D> resultList = new ArrayList<>();
        resultList.add(new Line2D(x, y, x + width, y));
        resultList.add(new Line2D(x + width, y, x + width, y + height));
        resultList.add(new Line2D(x + width, y + height, x, y + height));
        resultList.add(new Line2D(x, y + height, x, y));
        return resultList;
    }

    private void paintBaseLayer(final Graphics2D graphics, final int height, final int width, final StarBackground starBackground) {
        paintStars(graphics, height, width, starBackground);
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
        final Rectangle2D finish = new Rectangle2D(2 * width / 7, 3 * width / 7 - 40, width / 7, 10);
        final GradientPaint gradientPaint = new GradientPaint(25, 25, Color.WHITE, 15, 25, Color.BLACK, true);
        GraphicsUtils.drawRectangle(finish, gradientPaint, graphics);
    }
}
