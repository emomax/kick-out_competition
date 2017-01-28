package spacerace.level.graphics;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RadialGradientPaint;
import java.awt.geom.Point2D;


public class Sphere {
    private final Color color;
    private final Color shadowColor;
    private final int   radius;
    private       int   centerX;
    private       int   centerY;

    public Sphere(final Color color, final Color shadowColor, final int radius, final int centerX, final int centerY) {
        this.color = color;
        this.shadowColor = shadowColor;
        this.radius = radius;
        this.centerX = centerX;
        this.centerY = centerY;
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
     * Ok, so this i kinda weird-looking but goes something like this. We set the paint to a standard Radial gradient with only two
     * colors. Then we offset that gradient relative the oval we draw next to get the focus of the gradient to the upper left of
     * the oval, giving the sphere illusion.
     * <p>
     * Read more about implementation and examples here: https://docs.oracle.com/javase/7/docs/api/java/awt/RadialGradientPaint.html
     */
    public void paint(final Graphics2D graphics) {
        final int diameter = radius * 2;
        final int circleX  = centerX - radius;
        final int circleY  = centerY - radius;

        final Point2D center = new Point2D.Float(
                new Double(circleX + 0.33 * diameter).floatValue(),
                new Double(circleY + 0.33 * diameter).floatValue());

        final float[] fractions = { 0.0f, 1.0f };
        final Color[] colors    = { color, shadowColor };
        final RadialGradientPaint paint = new RadialGradientPaint(
                center,
                diameter * 0.8f,
                fractions,
                colors);

        graphics.setPaint(paint);
        graphics.fillOval(circleX,
                          circleY,
                          diameter,
                          diameter);
    }
}
