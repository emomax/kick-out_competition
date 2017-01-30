package spacerace.level.graphics;

import java.awt.Color;
import java.awt.Graphics2D;

/**
 * The ellipse equation is (x/a)^2 + (y/b)^2 = 1
 * a and b are the longest and shortest distance from center to the edges. The parameter b is called the semi-minor axis by
 * analogy with the parameter a, which is called the semi-major axis (assuming b<a).
 */
public class Ellipse {
    private final int   a;
    private final int   b;
    private       int   centerX;
    private       int   centerY;
    private       Color color;

    public Ellipse(final int a, final int b, final int centerX, final int centerY, final Color color) {
        this.a = a;
        this.b = b;
        this.centerX = centerX;
        this.centerY = centerY;
        this.color = color;
    }

    public Ellipse(final int a, final int b, final int centerX, final int centerY) {
        this.a = a;
        this.b = b;
        this.centerX = centerX;
        this.centerY = centerY;
    }

    public int getA() {
        return a;
    }

    public int getCenterX() {
        return centerX;
    }

    public int getCenterY() {
        return centerY;
    }

    public void setCenterX(final int centerX) {
        this.centerX = centerX;
    }

    public void setCenterY(final int centerY) {
        this.centerY = centerY;
    }

    /**
     * y = b * sqrt( 1 - (x/a)^2 )
     */
    public double calculateY(final double x) {
        if (x < -a || x > a) {
            throw new IllegalArgumentException("X was " + x + ", must be within [-" + a + ", " + a + "]");
        }
        return b * Math.sqrt(1 - Math.pow(x / a, 2));
    }

    public void paintUpperPart(final Graphics2D graphics) {
        final int upperLeftCornerX = centerX - a;
        final int upperLeftCornerY = centerY - b;
        final int width            = a * 2;
        final int height           = b * 2;
        final int startAngle       = 0;
        final int endAngle         = 180;
        graphics.setColor(color);
        graphics.drawArc(upperLeftCornerX, upperLeftCornerY, width, height, startAngle, endAngle);
    }

    public void paintLowerPart(final Graphics2D graphics) {
        final int upperLeftCornerX = centerX - a;
        final int upperLeftCornerY = centerY - b;
        final int width            = a * 2;
        final int height           = b * 2;
        final int startAngle       = 180;
        final int endAngle         = 180;
        graphics.setColor(color);
        graphics.drawArc(upperLeftCornerX, upperLeftCornerY, width, height, startAngle, endAngle);
    }

    public void paint(final Graphics2D graphics2D) {
        final int x = centerX - a;
        final int y = centerY - b;
        graphics2D.setColor(color);
        graphics2D.drawOval(x, y, a * 2, b * 2);
    }
}
