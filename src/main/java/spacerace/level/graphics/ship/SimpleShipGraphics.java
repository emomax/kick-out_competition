package spacerace.level.graphics.ship;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics2D;

import spacerace.domain.Rectangle2D;
import spacerace.domain.ShipState;
import spacerace.graphics.GraphicsUtils;
import spacerace.level.ShipGraphics;
import spacerace.level.graphics.Sphere;

public class SimpleShipGraphics implements ShipGraphics {

    private static final int    WIDTH                     = 50;
    private static final int    HEIGHT                    = 50;
    private static final double WINDOW_FADING_SPEED       = 150.0;
    public static final  double WINDOW_FADING_ALPHA_LIMIT = 0.6;

    private boolean fadeWindow = true;

    private int paintCycleCount = 0;

    @Override
    public int getWidth() {
        return WIDTH;
    }

    @Override
    public int getHeight() {
        return HEIGHT;
    }

    @Override
    public void paint(final ShipState shipState, final Graphics2D graphics) {
        final Color playerColor = new Color(shipState.getColorRGB());
        final int   x           = (int) shipState.getPosition().getX();
        final int   y           = (int) shipState.getPosition().getY();

        final Rectangle2D colorRectangle = new Rectangle2D(x, y, getWidth(), getHeight());
        GraphicsUtils.drawRectangle(colorRectangle, playerColor, graphics);

        drawShip(x, y, playerColor, graphics);
    }

    private void drawShip(final int x, final int y, final Color playerColor, final Graphics2D graphics) {
        paintBorders(x, y, playerColor, graphics);
        paintWindow(x, y, graphics);
    }

    private void paintWindow(final int x, final int y, final Graphics2D graphics) {
        final int centerX = x + WIDTH / 2;
        final int centerY = y + HEIGHT / 2;

        // Paint window frame
        final int    frameRadius = 18;
        final Sphere frame       = new Sphere(Color.ORANGE, new Color(0x806F04), frameRadius, centerX, centerY);
        frame.paint(graphics);

        final int    windowRadius = 14;
        final Sphere window       = new Sphere(Color.RED, Color.RED, windowRadius, centerX, centerY);
        window.paint(graphics);

        paintWindowGlass(graphics, centerX, centerY);
    }

    private void paintWindowGlass(final Graphics2D graphics, final int centerX, final int centerY) {
        final int windowRadius = 14;

        paintCycleCount++;
        final float alpha;
        if (fadeWindow) {
            alpha = new Double((WINDOW_FADING_SPEED - paintCycleCount) / WINDOW_FADING_SPEED).floatValue();
        }
        else {
            alpha = new Double((1.0 * (paintCycleCount - WINDOW_FADING_SPEED)) / WINDOW_FADING_SPEED).floatValue();
        }
        if (fadeWindow && alpha < WINDOW_FADING_ALPHA_LIMIT) {
            fadeWindow = false;
            paintCycleCount = (int) (WINDOW_FADING_SPEED + WINDOW_FADING_ALPHA_LIMIT * WINDOW_FADING_SPEED);
        }
        else if (paintCycleCount == 2 * WINDOW_FADING_SPEED) {
            paintCycleCount = 0;
            fadeWindow = true;
        }
        final Color  color       = GraphicsUtils.createColorWithAlpha(Color.WHITE, alpha);
        final Color  shadowColor = GraphicsUtils.createColorWithAlpha(Color.CYAN, alpha);
        final Sphere window      = new Sphere(color, shadowColor, windowRadius, centerX, centerY);
        window.paint(graphics);
    }

    private void paintBorders(final int x, final int y, final Color playerColor, final Graphics2D graphics) {
        final int           frameWidth    = 5;
        final Color         shadowColor   = GraphicsUtils.createColorWithAlpha(new Color(0x515151), 0.7f);
        final GradientPaint upperGradient = new GradientPaint(x, y, shadowColor, x, y + frameWidth, playerColor, true);
        final GradientPaint lowerGradient = new GradientPaint(x, y, shadowColor, x, y - frameWidth, playerColor, true);
        final GradientPaint leftGradient  = new GradientPaint(x, y, shadowColor, x + frameWidth, y, playerColor, true);
        final GradientPaint rightGradient = new GradientPaint(x, y, shadowColor, x + frameWidth, y, playerColor, true);

        final Rectangle2D upperRectangle = new Rectangle2D(x + frameWidth, y, WIDTH - frameWidth * 2, frameWidth);
        final Rectangle2D leftRectangle  = new Rectangle2D(x, y + frameWidth, frameWidth, HEIGHT - 2 * frameWidth);
        final Rectangle2D rightRectangle = new Rectangle2D(x + WIDTH - frameWidth, y + frameWidth, frameWidth, HEIGHT - 2 * frameWidth);
        final Rectangle2D lowerRectangle = new Rectangle2D(x + frameWidth, y + HEIGHT - frameWidth, WIDTH - 2 * frameWidth, frameWidth);

        GraphicsUtils.drawRectangle(upperRectangle, upperGradient, graphics);
        GraphicsUtils.drawRectangle(leftRectangle, leftGradient, graphics);
        GraphicsUtils.drawRectangle(rightRectangle, rightGradient, graphics);
        GraphicsUtils.drawRectangle(lowerRectangle, lowerGradient, graphics);

        final Color       detectorColor  = playerColor;
        final Rectangle2D upperDetector1 = new Rectangle2D(x, y, frameWidth, frameWidth);
        final Rectangle2D upperDetector2 = new Rectangle2D(x + WIDTH / 2 - frameWidth / 2, y, frameWidth, frameWidth);
        final Rectangle2D upperDetector3 = new Rectangle2D(x, y, frameWidth, frameWidth);
        final Rectangle2D leftDetector   = new Rectangle2D(x, y + HEIGHT / 2 - frameWidth / 2, frameWidth, frameWidth);
        final Rectangle2D rightDetector  = new Rectangle2D(x + WIDTH - frameWidth, y + HEIGHT / 2 - frameWidth / 2, frameWidth, frameWidth);
        final Rectangle2D lowerDetector1 = new Rectangle2D(x, y + HEIGHT - frameWidth, frameWidth, frameWidth);
        final Rectangle2D lowerDetector2 = new Rectangle2D(x + WIDTH / 2 - frameWidth / 2, y + HEIGHT - frameWidth, frameWidth, frameWidth);
        final Rectangle2D lowerDetector3 = new Rectangle2D(x, y + HEIGHT - frameWidth, frameWidth, frameWidth);

        GraphicsUtils.drawRectangle(upperDetector1, detectorColor, graphics);
        GraphicsUtils.drawRectangle(upperDetector2, detectorColor, graphics);
        GraphicsUtils.drawRectangle(upperDetector3, detectorColor, graphics);
        GraphicsUtils.drawRectangle(leftDetector, detectorColor, graphics);
        GraphicsUtils.drawRectangle(rightDetector, detectorColor, graphics);
        GraphicsUtils.drawRectangle(lowerDetector1, detectorColor, graphics);
        GraphicsUtils.drawRectangle(lowerDetector2, detectorColor, graphics);
        GraphicsUtils.drawRectangle(lowerDetector3, detectorColor, graphics);
    }
}
