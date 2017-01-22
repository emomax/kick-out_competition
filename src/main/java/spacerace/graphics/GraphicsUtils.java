package spacerace.graphics;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;

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
}
