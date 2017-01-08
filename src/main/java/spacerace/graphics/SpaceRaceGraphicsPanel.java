package spacerace.graphics;

import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.IntSummaryStatistics;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

import spacerace.domain.GameState;
import spacerace.domain.GameStatistics;
import spacerace.domain.Line2D;
import spacerace.domain.Rectangle2D;
import spacerace.domain.ShipState;
import spacerace.level.Level;

public class SpaceRaceGraphicsPanel extends JPanel implements SpaceRaceGraphics {

    private static final String SHIP_IMAGE_DIR           = "../../spacerace/ship.png";
    private final        int    DETECTOR_BEAM_MAX_LENGTH = 100;

    private final Level          level;
    private final BufferedImage  shipImage;
    private       GameState      gameState;
    private final GameStatistics gameStatistics;

    public SpaceRaceGraphicsPanel(final Level level, final KeyListener keyListener, final GameState gameState, final GameStatistics gameStatistics) throws IOException {
        this.level = level;
        this.gameState = gameState;
        this.gameStatistics = gameStatistics;

        addKeyListener(keyListener);
        setFocusable(true);
        setBackground(Color.black);
        setDoubleBuffered(true);

        this.shipImage = ImageIO.read(new File(getClass().getResource(SHIP_IMAGE_DIR).getFile()));
    }

    // ToDo: Must this be here?
    public void addNotify() {
        super.addNotify();
    }

    // ToDo: Must this be here?
    @Override
    public void update(final Graphics g) {
        paint(g);
    }

    @Override
    public void updateGraphics(final GameState gameState) {
        this.gameState = gameState;
        repaint();
    }

    @Override
    public void paint(final Graphics graphics) {
        super.paint(graphics);

        drawLevel(graphics);

        for (final ShipState shipState : gameState.getShipStates()) {
            drawShip(shipState, graphics);
        }

        drawInfoPanel(graphics);

        Toolkit.getDefaultToolkit().sync();
        graphics.dispose();
    }

    private void drawLevel(final Graphics graphics) {
        final GradientPaint gradientPaint = new GradientPaint(25, 25, Color.BLUE, 15, 25, Color.BLACK, true);
        level.getRectangles().forEach(rectangle -> drawRectangle(rectangle, gradientPaint, graphics));
    }

    private void drawShip(final ShipState shipState, final Graphics graphics) {
        graphics.drawImage(shipImage, (int) shipState.getPosition().getX(), (int) shipState.getPosition().getY(), this);
        drawDetectors(shipState, graphics);
    }

    private void drawDetectors(final ShipState shipState, final Graphics graphics) {
        final DetectorCalculator detectorCalculator = new DetectorCalculator(shipState, shipImage, level);
        final List<Line2D>       detectorBeams      = detectorCalculator.getDetectorBeams();
        detectorBeams.forEach(beam -> drawLine(beam, Color.RED, graphics));
    }

    private void drawLine(final Line2D line, final Color color, final Graphics graphics) {
        graphics.setColor(color);
        graphics.drawLine(line.getX1(), line.getY1(), line.getX2(), line.getY2());
    }

    private void drawRectangle(final Rectangle2D rectangle, final Color color, final Graphics graphics) {
        graphics.setColor(color);
        graphics.fillRect(rectangle.getX(), rectangle.getY(), rectangle.getWidth(), rectangle.getHeight());
    }

    private void drawRectangle(final Rectangle2D rectangle, final GradientPaint gradientPaint, final Graphics graphics) {
        ((Graphics2D) graphics).setPaint(gradientPaint);
        graphics.fillRect(rectangle.getX(), rectangle.getY(), rectangle.getWidth(), rectangle.getHeight());
    }

    private void drawInfoPanel(final Graphics graphics) {
        final DecimalFormat formatter = new DecimalFormat("#0.0000");
        final Font          font      = new Font("Helvetica", Font.BOLD, 14);

        // Draw ship speed and acceleration
        int linePosY = 20;
        for (final ShipState shipState : gameState.getShipStates()) {
            final String speedXText = formatter.format(shipState.getSpeed().getX());
            final String speedYText = formatter.format(shipState.getSpeed().getY());
            final String speedText  = "Speed: [" + speedXText + ", " + speedYText + "]";

            final String accelerationXText = formatter.format(shipState.getAccelerationDirection().getX());
            final String accelerationYText = formatter.format(shipState.getAccelerationDirection().getY());
            final String accelerationText  = "Acceleration: [" + accelerationXText + ", " + accelerationYText + "]";

            final String text = speedText + "   " + accelerationText;
            graphics.setColor(Color.YELLOW);
            graphics.setFont(font);
            graphics.drawString(text, 5, linePosY);

            linePosY = linePosY + 10;
        }

        // Draw update times and FPS statistics
        final IntSummaryStatistics updateTimeStatistics = gameStatistics.getGameCycleStatistics();

        final String updateTimeText = "Update time"
                                      + "  min: " + updateTimeStatistics.getMin()
                                      + "  max: " + updateTimeStatistics.getMax()
                                      + "  average: " + ((int) updateTimeStatistics.getAverage());
        final String fpsText = "FPS3    "
                               + "  min: " + (1000 / updateTimeStatistics.getMax())
                               + "  max: " + (1000 / updateTimeStatistics.getMin())
                               + "  average: " + ((int) (1000 / updateTimeStatistics.getAverage()));
        graphics.setColor(Color.YELLOW);
        graphics.setFont(font);
        graphics.drawString(updateTimeText, 400, 20);
        graphics.drawString(fpsText, 400, 40);
    }
}
