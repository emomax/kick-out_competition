package spacerace.graphics;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

import spacerace.domain.Line2D;
import spacerace.domain.Rectangle2D;

public class GraphicsUtils {

    private GraphicsUtils() {
        // No constructor for you!
    }

    public static void drawLine(final Line2D line, final Color color, final Graphics graphics) {
        graphics.setColor(color);
        graphics.drawLine(line.getX1(), line.getY1(), line.getX2(), line.getY2());
    }

    public static void drawRectangle(final Rectangle2D rectangle, final Color color, final Graphics graphics) {
        graphics.setColor(color);
        graphics.fillRect(rectangle.getX(), rectangle.getY(), rectangle.getWidth(), rectangle.getHeight());
    }

    public static void drawRectangle(final Rectangle2D rectangle, final GradientPaint gradientPaint, final Graphics graphics) {
        ((Graphics2D) graphics).setPaint(gradientPaint);
        graphics.fillRect(rectangle.getX(), rectangle.getY(), rectangle.getWidth(), rectangle.getHeight());
    }

    public static BufferedImage rotateImage(final BufferedImage image, final int angle) {
        final double            rotationRequired = Math.toRadians(angle);
        final double            locationX        = image.getWidth() / 2;
        final double            locationY        = image.getHeight() / 2;
        final AffineTransform   transform        = AffineTransform.getRotateInstance(rotationRequired, locationX, locationY);
        final AffineTransformOp transformOp      = new AffineTransformOp(transform, AffineTransformOp.TYPE_BILINEAR);
        return transformOp.filter(image, null);
    }
}
