package spacerace.level;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

import spacerace.domain.Line2D;
import spacerace.domain.Rectangle2D;
import spacerace.domain.Vector2D;
import spacerace.graphics.GraphicsUtils;
import spacerace.level.graphics.StarBackground;

class Level7Builder {
    private static final double STARS_ROTATION_ANGULAR_SPEED = 0.05;
    private double starsBackgroundAngle = 0;

    private static final int corridorWidth = 90;

    private Level7Builder() {
        // Intentionally empty
    }

    static Level build() {
        final int levelNumber = 7;
        final int timeLimit   = 120_000;
        final int height      = 1000;
        final int width       = 1630;

        final List<Line2D> trackBorders = createTrackBorders(width, height);

        final Vector2D startPosition = new Vector2D(25, 25);
        final Line2D   goalLine      = new Line2D(1490, 455,
                1490,545);

        final StarBackground       starBackground   = new StarBackground(30, width, height, 200);
        final Level7Builder        builder          = new Level7Builder();
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

    private static List<Line2D> createTrackBorders(final int width, final int height) {
        final List<Line2D> resultList = new ArrayList<>();

        resultList.addAll(generateRectangle(0, 0, 18, 11));

        resultList.addAll(generateRectangle(1, 0, 3, 1));
        resultList.addAll(generateRectangle(4, 0, 6, 1));
        resultList.addAll(generateRectangle(7, 0, 8, 1));
        resultList.addAll(generateRectangle(9, 0, 10, 1));
        resultList.addAll(generateRectangle(11, 0, 12, 1));
        resultList.addAll(generateRectangle(13, 0, 14, 1));
        resultList.addAll(generateRectangle(15, 0, 18, 1));

        resultList.add(generateLine(1, 1, 1, 2));
        resultList.add(generateLine(9.5f, 1, 9.5f, 2));

        resultList.addAll(generateRectangle(1, 2, 3, 4));
        resultList.addAll(generateRectangle(4, 2, 6, 4));
        resultList.addAll(generateRectangle(7, 2, 8, 4));
        resultList.addAll(generateRectangle(9, 2, 10, 4));
        resultList.addAll(generateRectangle(11, 2, 12, 3));
        resultList.addAll(generateRectangle(13, 2, 14, 3));
        resultList.addAll(generateRectangle(15, 2, 18, 3));

        resultList.addAll(generateRectangle(13, 4, 14, 5));
        resultList.addAll(generateRectangle(15, 4, 16, 5));
        resultList.addAll(generateRectangle(17, 4, 18, 5));

        resultList.addAll(generateRectangle(2, 5, 3, 6));
        resultList.addAll(generateRectangle(4, 5, 6, 6));
        resultList.addAll(generateRectangle(7, 5, 8, 6));
        resultList.addAll(generateRectangle(9, 5, 10, 6));

        resultList.add(generateLine(0, 6, 1, 6));
        resultList.add(generateLine(1, 6, 1, 5));
        resultList.add(generateLine(1, 5, 2, 5));
        resultList.add(generateLine(5, 4, 5, 5));

        resultList.addAll(generateRectangle(0, 7, 1, 9));
        resultList.addAll(generateRectangle(2, 7, 3, 9));
        resultList.addAll(generateRectangle(3, 8, 4, 9));
        resultList.addAll(generateRectangle(4, 7, 6, 9));
        resultList.addAll(generateRectangle(7, 7, 8, 9));
        resultList.addAll(generateRectangle(9, 7, 10, 8));
        resultList.addAll(generateRectangle(11, 7, 12, 8));
        resultList.addAll(generateRectangle(13, 6, 14, 8));
        resultList.addAll(generateRectangle(15, 6, 16, 8));
        resultList.addAll(generateRectangle(17, 6, 18, 8));

        resultList.add(generateLine(11, 3, 11, 4));
        resultList.add(generateLine(11, 4, 12, 4));
        resultList.add(generateLine(12, 4, 12, 5));
        resultList.add(generateLine(11, 5, 13, 5));
        resultList.add(generateLine(11, 5, 11, 6));
        resultList.add(generateLine(11, 6, 13, 6));
        resultList.add(generateLine(12, 6, 12, 7));

        resultList.add(generateLine(16, 4, 17, 4));
        resultList.add(generateLine(16, 7, 17, 7));

        resultList.addAll(generateRectangle(0, 10, 1, 11));
        resultList.addAll(generateRectangle(2, 10, 6, 11));
        resultList.addAll(generateRectangle(7, 10, 8, 11));
        resultList.addAll(generateRectangle(9, 10, 10, 11));
        resultList.addAll(generateRectangle(11, 9, 12, 11));
        resultList.addAll(generateRectangle(13, 9, 14, 11));
        resultList.addAll(generateRectangle(15, 9, 16, 11));
        resultList.addAll(generateRectangle(17, 9, 18, 11));
        resultList.addAll(generateRectangle(16, 10, 17, 11));

        resultList.add(generateLine(9, 8, 9, 9));
        resultList.add(generateLine(9, 9, 10, 9));
        resultList.add(generateLine(10, 9, 10, 10));

        return resultList;
    }

    private static List<Line2D> generateRectangle(final int x1, final int y1, final int x2, final int y2) {
        final List<Line2D> resultList = new LinkedList<>();

        resultList.add(new Line2D(getProjectedCoordinate(x1), getProjectedCoordinate(y1), getProjectedCoordinate(x1), getProjectedCoordinate(y2)));
        resultList.add(new Line2D(getProjectedCoordinate(x1), getProjectedCoordinate(y1), getProjectedCoordinate(x2), getProjectedCoordinate(y1)));
        resultList.add(new Line2D(getProjectedCoordinate(x2), getProjectedCoordinate(y1), getProjectedCoordinate(x2), getProjectedCoordinate(y2)));
        resultList.add(new Line2D(getProjectedCoordinate(x1), getProjectedCoordinate(y2), getProjectedCoordinate(x2), getProjectedCoordinate(y2)));

        return resultList;
    }

    private static Line2D generateLine(final float x1, final float y1, final float x2, final float y2) {
        return new Line2D(getProjectedCoordinate(x1), getProjectedCoordinate(y1), getProjectedCoordinate(x2), getProjectedCoordinate(y2));
    }

    private static int getProjectedCoordinate(final float coordinate) {
        final float result = coordinate * corridorWidth + 5;
        return (int) result;
    }

    private void paintBaseLayer(final Graphics2D graphics, final int height, final int width, final StarBackground starBackground) {
        final int offSet = 16;

        paintStars(graphics, height, width, starBackground);
        drawFinishLine(graphics, 1490 - offSet, 455, 1490 + offSet,545);
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

    private void drawFinishLine(final Graphics graphics, final int startX, final int startY, final int endX, final int endY) {
        final Rectangle2D finish = new Rectangle2D(startX, startY, endX - startX, endY - startY);
        final GradientPaint gradientPaint = new GradientPaint(25, 25, Color.ORANGE, 25, 15, Color.BLUE, true);
        GraphicsUtils.drawRectangle(finish, gradientPaint, graphics);
    }
}