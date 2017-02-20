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

class Level5Builder {
    private static final double STARS_ROTATION_ANGULAR_SPEED = 0.05;
    private double starsBackgroundAngle = 0;

    private static final int radius = 900 / 14;

    private Level5Builder() {
        // Intentionally empty
    }

    static Level build() {
        final int levelNumber = 5;
        final int timeLimit   = 120_000;
        final int height      = 768;
        final int width       = 1020;

        final List<Line2D> trackBorders = createTrackBorders(width, height);

        final Vector2D startPosition = new Vector2D(width - 70, height - 70);
        final Line2D   goalLine      = new Line2D(width / 6, height / 6 + radius,
                width / 6,(height / 6) * 3 - radius);

        final StarBackground       starBackground   = new StarBackground(30, width, height, 200);
        final Level5Builder builder          = new Level5Builder();
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

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                resultList.addAll(generateCircle(radius, width / 3 * i + width / 3 / 2, height / 3 * j + height / 3 / 2));
            }
        }

        return resultList;
    }

    private static List<Line2D> generateCircle(int radius, int originX, int originY) {
        final List<Line2D> resultList = new LinkedList<>();

        final double edges = 40f;
        final double radiansPerEdge = Math.PI * 2 / edges;

        double lastX = originX + radius;
        double lastY = originY;
        double x = originX + Math.cos(radiansPerEdge) * radius;
        double y = originY + Math.sin(radiansPerEdge) * radius;

        resultList.add(new Line2D((int)lastX, (int)lastY, (int)x, (int)y));

        for (float angle = 0f; angle <= Math.PI * 2f; angle += radiansPerEdge) {
            x = originX + Math.cos(angle) * radius;
            y = originY + Math.sin(angle) * radius;

            final Line2D currentLine = new Line2D((int)lastX, (int)lastY, (int)x, (int)y);
            final Line2D lastLine = resultList.get(resultList.size() - 1);

            if (currentLine.getX1() == currentLine.getX2() && currentLine.getY1() == currentLine.getY2()) {
                // Same point, continue
                continue;
            }

            lastX = currentLine.getX2();
            lastY = currentLine.getY2();

            resultList.add(currentLine);
        }

        final Line2D firstLine = resultList.get(0);
        final Line2D lastLine = resultList.get(resultList.size() - 1);

        if (firstLine.getX1() != lastLine.getX2() || firstLine.getY2() != lastLine.getY2()) {
            // last line doesn't connect to first one => make a new one
            Line2D finalLine = new Line2D(lastLine.getX2(), lastLine.getY2(), firstLine.getX1(), firstLine.getY1());
            resultList.add(finalLine);
        }

        return resultList;
    }

    private void paintBaseLayer(final Graphics2D graphics, final int height, final int width, final StarBackground starBackground) {
        final int offSet = 16;

        paintStars(graphics, height, width, starBackground);
        drawFinishLine(graphics, width / 6 - offSet, height / 6 + radius, width / 6 + offSet,(height / 6) * 3 - radius);
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

    private void drawFinishLine(final Graphics graphics, int startX, int startY, int endX, int endY) {
        final Rectangle2D finish = new Rectangle2D(startX, startY, endX - startX, endY - startY);
        final GradientPaint gradientPaint = new GradientPaint(25, 25, Color.WHITE, 25, 15, Color.BLACK, true);
        GraphicsUtils.drawRectangle(finish, gradientPaint, graphics);
    }
}
