package spacerace.level;

import spacerace.domain.Line2D;
import spacerace.domain.Rectangle2D;
import spacerace.domain.Vector2D;
import spacerace.graphics.GraphicsUtils;
import spacerace.level.graphics.StarBackground;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

class Level8Builder {
    private static final double STARS_ROTATION_ANGULAR_SPEED = 0.05;
    private double starsBackgroundAngle = 0;

    private Level8Builder() {
        // Intentionally empty
    }

    static Level build() {
        final int levelNumber = 8;
        final int timeLimit   = 120_000;
        final int height      = 1000;
        final int width       = 1000;

        final List<Line2D> trackBorders = createTrackBorders(width, height);

        final Vector2D startPosition = new Vector2D(50, 500);
        final Line2D   goalLine      = new Line2D(500, 640,
                500,740);

        final StarBackground       starBackground   = new StarBackground(30, width, height, 200);
        final Level8Builder builder          = new Level8Builder();
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

        resultList.addAll(generateCircle(500, 500, 500, 0, 2 * Math.PI, true));
        resultList.addAll(generateCircle(350, 500, 500, 10 * 2*Math.PI / 360, 2 * Math.PI - 10 * 2*Math.PI / 360, false));

        resultList.add(new Line2D(844, 560, 500, 560));
        resultList.add(new Line2D(844, 440, 760, 440));
        resultList.add(new Line2D(590, 440, 500, 440));
        resultList.add(new Line2D(500, 560, 500, 440));

        resultList.addAll(generateCircle(109, 500, 500, 34 * Math.PI / 180, 2 * Math.PI - 34 * Math.PI / 180, false));
        resultList.addAll(generateCircle(266, 500, 500, 13 * Math.PI / 180, 2 * Math.PI - 13 * Math.PI / 180, false));

        return resultList;
    }

    private static List<Line2D> generateCircle(int radius, int originX, int originY, double startAngle, double endAngle, boolean close) {
        final List<Line2D> resultList = new LinkedList<>();

        final double edges = 80f;
        final double radiansPerEdge = Math.PI * 2 / edges;

        double lastX = originX + Math.cos(startAngle) * radius;
        double lastY = originY + Math.sin(startAngle) * radius;

        double endX = Math.ceil(originX + Math.cos(endAngle) * radius);
        double endY = Math.ceil(originY + Math.sin(endAngle) * radius);


        for (double angle = startAngle; angle < endAngle; angle += radiansPerEdge) {
            double x = originX + Math.cos(angle) * radius;
            double y = originY + Math.sin(angle) * radius;

            final Line2D currentLine = new Line2D((int)lastX, (int)lastY, (int)x, (int)y);
            if (currentLine.getX1() == currentLine.getX2() && currentLine.getY1() == currentLine.getY2()) {
                // Same point, continue
                continue;
            }

            lastX = currentLine.getX2();
            lastY = currentLine.getY2();

            resultList.add(currentLine);
        }


        if (close) {
            final Line2D firstLine = resultList.get(0);
            final Line2D lastLine = resultList.get(resultList.size() - 1);

            if (firstLine.getX1() != lastLine.getX2() || firstLine.getY2() != lastLine.getY2()) {
                // last line doesn't connect to first one => make a new one
                Line2D finalLine = new Line2D(lastLine.getX2(), lastLine.getY2(), firstLine.getX1(), firstLine.getY1());
                resultList.add(finalLine);
            }
        }
        else {
            final Line2D currentLine = new Line2D((int)lastX, (int)lastY, (int)endX, (int)endY);
            resultList.add(currentLine);
        }

        return resultList;
    }

    private void paintBaseLayer(final Graphics2D graphics, final int height, final int width, final StarBackground starBackground) {
        final int offSet = 16;

        paintStars(graphics, height, width, starBackground);
        drawFinishLine(graphics, 500 - offSet, 640, 500 + offSet,740);
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