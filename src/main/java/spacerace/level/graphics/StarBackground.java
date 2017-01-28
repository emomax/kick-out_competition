package spacerace.level.graphics;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RadialGradientPaint;
import java.awt.Shape;
import java.awt.geom.Path2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class StarBackground {

    private final int numberOfStars;
    private final int width;
    private final int height;
    private final int backgroundChangeSpeed;
    private int paintCycleCount = 0;
    private List<Point> oldCenterPoints;
    private List<Point> starCenterPoints;

    public StarBackground(final int numberOfStars, final int width, final int height, final int backgroundChangeSpeed) {
        this.numberOfStars = numberOfStars;
        this.width = width;
        this.height = height;
        this.backgroundChangeSpeed = backgroundChangeSpeed;
    }


    public void paintStars(final Graphics2D graphics) {
        paintCycleCount++;
        if (paintCycleCount == 1) {
            if (starCenterPoints != null) {
                oldCenterPoints = new ArrayList<>(starCenterPoints);
            }
            starCenterPoints = generateStarCenterPoints();
        }
        if (oldCenterPoints != null) {
            final float oldStarAlpha = new Double(((1.0 * backgroundChangeSpeed - paintCycleCount) / (1.0 * backgroundChangeSpeed))).floatValue();
            oldCenterPoints.forEach(center -> paintStar(graphics, center, oldStarAlpha));
        }
        final float alpha = new Double((1.0 * paintCycleCount / backgroundChangeSpeed)).floatValue();
        starCenterPoints.forEach(center -> paintStar(graphics, center, alpha));

        if (paintCycleCount == backgroundChangeSpeed) {
            paintCycleCount = 0;
        }
    }

    private List<Point> generateStarCenterPoints() {
        final Random      random           = new Random();
        final List<Point> starCenterPoints = new ArrayList<>();
        for (int i = 0; i < numberOfStars; i++) {
            final int   x          = random.nextInt(width);
            final int   y          = random.nextInt(height);
            final Point starCenter = new Point(x, y);
            starCenterPoints.add(starCenter);
        }
        return starCenterPoints;
    }

    private void paintStar(final Graphics2D graphics, final Point center, final float alpha) {
        if (alpha < 0 || alpha > 1) {
            throw new IllegalArgumentException("Illegal alpha: " + alpha);
        }
        final Color   color1          = Color.WHITE;
        final Color   color2          = Color.YELLOW;
        final float[] rgb1            = color1.getRGBColorComponents(null);
        final float[] rgb2            = color2.getRGBColorComponents(null);
        final Color   color1WithAlpha = new Color(rgb1[0], rgb1[1], rgb1[2], alpha);
        final Color   color2WithAlpha = new Color(rgb2[0], rgb2[1], rgb2[2], alpha);
        final Color[] colors          = { color1WithAlpha, color2WithAlpha };
        final float[] fractions       = { 0, 0.8f };
        final int     radius          = 5;
        final RadialGradientPaint paint = new RadialGradientPaint(
                center,
                radius,
                fractions,
                colors);
        graphics.setPaint(paint);

        final int   innerRadius   = 20;
        final int   numRays       = 6;
        final int   startAngleRad = 0;
        final Shape star          = createStar(center, innerRadius, radius, numRays, startAngleRad);
        graphics.fill(star);
    }

    // To be honest, the star paint code below is actually stolen....I mean borrowed.
    private Shape createStar(final Point center,
            final double innerRadius, final double outerRadius, final int numRays,
            final double startAngleRad) {
        final Path2D path          = new Path2D.Double();
        final double deltaAngleRad = Math.PI / numRays;
        for (int i = 0; i < numRays * 2; i++) {
            final double angleRad = startAngleRad + i * deltaAngleRad;
            final double ca       = Math.cos(angleRad);
            final double sa       = Math.sin(angleRad);
            double       relX     = ca;
            double       relY     = sa;
            if ((i & 1) == 0) {
                relX *= outerRadius;
                relY *= outerRadius;
            }
            else {
                relX *= innerRadius;
                relY *= innerRadius;
            }
            if (i == 0) {
                path.moveTo(center.getX() + relX, center.getY() + relY);
            }
            else {
                path.lineTo(center.getX() + relX, center.getY() + relY);
            }
        }
        path.closePath();
        return path;
    }
}
