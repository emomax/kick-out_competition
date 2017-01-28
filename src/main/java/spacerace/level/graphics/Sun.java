package spacerace.level.graphics;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RadialGradientPaint;
import java.awt.geom.Point2D;

public class Sun {
    private final int radius;
    private final int centerX;
    private final int centerY;

    public Sun(final int radius, final int centerX, final int centerY) {
        this.radius = radius;
        this.centerX = centerX;
        this.centerY = centerY;
    }

    public void paint(final Graphics2D graphics) {
        final Color color       = Color.YELLOW;
        final Color shadowColor = Color.ORANGE;

        final int     diameter  = radius * 2;
        final int     circleX   = centerX - radius;
        final int     circleY   = centerY - radius;
        final Point2D center    = new Point2D.Float(centerX, centerY);
        final float[] fractions = { 0.2f, 1.0f };
        final Color[] colors    = { color, shadowColor };
        final RadialGradientPaint paint = new RadialGradientPaint(
                center,
                diameter * 0.5f,
                fractions,
                colors);

        graphics.setPaint(paint);
        graphics.fillOval(circleX,
                          circleY,
                          diameter,
                          diameter);
    }
}
