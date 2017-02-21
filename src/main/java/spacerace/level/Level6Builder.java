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

class Level6Builder {
    private static final double STARS_ROTATION_ANGULAR_SPEED = 0.05;
    private double starsBackgroundAngle = 0;

    private Level6Builder() {
        // Intentionally empty
    }

    static Level build() {
        final int levelNumber = 6;
        final int timeLimit   = 120_000;
        final int height      = 900;
        final int width       = 1200;

        final List<Line2D> trackBorders = createTrackBorders(width, height);

        final Vector2D startPosition = new Vector2D(width - 155, height - 155);
        final Line2D   goalLine      = new Line2D(255, height / 2,
                385,height / 2);

        final StarBackground       starBackground   = new StarBackground(30, width, height, 200);
        final Level6Builder builder          = new Level6Builder();
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

        // Upper left corner
        resultList.addAll(generateCircle(100, 255, 255, Math.PI, Math.PI * 1.5, false));
        resultList.addAll(generateCircle(250, 255, 255, Math.PI, Math.PI * 1.5, false));
        resultList.add(new Line2D(155, 255, 255, 255));
        resultList.add(new Line2D(255, 155, 255, 255));

        // Upper right corner
        resultList.addAll(generateCircle(100, width - 255, 255, Math.PI * 1.5, Math.PI * 2, false));
        resultList.addAll(generateCircle(250, width - 255, 255, Math.PI * 1.5, Math.PI * 2, false));
        resultList.add(new Line2D(width - 155, 255, width - 255, 255));
        resultList.add(new Line2D(width - 255, 155, width - 255, 255));

        // Bottom left corner
        resultList.addAll(generateCircle(100, 255, height - 255, Math.PI * 0.5, Math.PI, false));
        resultList.addAll(generateCircle(250, 255, height - 255, Math.PI * 0.5, Math.PI, false));
        resultList.add(new Line2D(155, height - 255, 255, height - 255));
        resultList.add(new Line2D(255, height - 155, 255, height - 255));

        // Bottom right corner
        resultList.addAll(generateCircle(100, width - 255, height - 255, 0, Math.PI * 0.5, false));
        resultList.addAll(generateCircle(250, width - 255, height - 255, 0, Math.PI * 0.5, false));
        resultList.add(new Line2D(width - 155, height - 255, width - 255, height - 255));
        resultList.add(new Line2D(width - 255, height - 155, width - 255, height - 255));

        // Wall lines
        resultList.add(new Line2D(5, 255, 5, height - 255));
        resultList.add(new Line2D(width - 5, 255, width - 5, height - 255));
        resultList.add(new Line2D(255, 5, width - 255, 5));
        resultList.add(new Line2D(255, height - 5, width - 255, height - 5));


        resultList.add(new Line2D(155, 385, 155, 515));
        resultList.add(new Line2D(255, 385, 255, 515));
        resultList.add(new Line2D(155, 385, 255, 385));
        resultList.add(new Line2D(155, 515, 255, 515));


        resultList.add(new Line2D(385, 385, 385, 515));
        resultList.add(new Line2D(385, 385, 535, 385));
        resultList.add(new Line2D(535, 385, 535, 515));
        resultList.add(new Line2D(385, 515, 535, 515));

        resultList.add(new Line2D(385, 155, 385, 255));
        resultList.add(new Line2D(385, 155, 535, 155));
        resultList.add(new Line2D(535, 155, 535, 255));
        resultList.add(new Line2D(385, 255, 535, 255));

        resultList.add(new Line2D(width - 385, 155, width - 385, 255));
        resultList.add(new Line2D(width - 385, 155, width - 535, 155));
        resultList.add(new Line2D(width - 535, 155, width - 535, 255));
        resultList.add(new Line2D(width - 385, 255, width - 535, 255));

        resultList.add(new Line2D(width - 385, height - 155, width - 385, height - 255));
        resultList.add(new Line2D(width - 385, height - 155, width - 535, height - 155));
        resultList.add(new Line2D(width - 535, height - 155, width - 535, height - 255));
        resultList.add(new Line2D(width - 385, height - 255, width - 535, height - 255));

        resultList.add(new Line2D(385, height - 155, 385, height - 255));
        resultList.add(new Line2D(385, height - 155, 535, height - 155));
        resultList.add(new Line2D(535, height - 155, 535, height - 255));
        resultList.add(new Line2D(385, height - 255, 535, height - 255));

        resultList.add(new Line2D(width - 385, 385, width - 385, 515));
        resultList.add(new Line2D(width - 385, 385, width - 535, 385));
        resultList.add(new Line2D(width - 535, 385, width - 535, 515));
        resultList.add(new Line2D(width - 385, 515, width - 535, 515));


        resultList.add(new Line2D(width - 155, 385, width - 155, 515));
        resultList.add(new Line2D(width - 255, 385, width - 255, 515));
        resultList.add(new Line2D(width - 155, 385, width - 255, 385));
        resultList.add(new Line2D(width - 155, 515, width - 255, 515));

        return resultList;
    }

    private static List<Line2D> generateCircle(int radius, int originX, int originY, double startAngle, double endAngle, boolean close) {
        final List<Line2D> resultList = new LinkedList<>();

        final double edges = 40f;
        final double radiansPerEdge = Math.PI * 2 / edges;

        double lastX = originX + Math.cos(startAngle) * radius;
        double lastY = originY + Math.sin(startAngle) * radius;

        for (double angle = startAngle; angle <= endAngle; angle += radiansPerEdge) {
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

        return resultList;
    }

    private void paintBaseLayer(final Graphics2D graphics, final int height, final int width, final StarBackground starBackground) {
        final int offSet = 16;

        paintStars(graphics, height, width, starBackground);
        drawFinishLine(graphics, 255, height / 2 - offSet, 385,height / 2 + offSet);
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
