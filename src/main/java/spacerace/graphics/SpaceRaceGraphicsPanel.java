package spacerace.graphics;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

import spacerace.domain.GameState;
import spacerace.domain.Line2D;
import spacerace.domain.Rectangle2D;
import spacerace.domain.ShipState;
import spacerace.level.Level;

public class SpaceRaceGraphicsPanel extends JPanel {

    private static final String SHIP_IMAGE_DIR = "../../spacerace/ship.png";

    private final Level         level;
    private final BufferedImage shipImage;
    private       GameState     gameState;
    private       String        updateTimeText;
    private       String        fpsText;

    public SpaceRaceGraphicsPanel(final Level level, final KeyListener keyListener, final GameState gameState) throws IOException {
        this.level = level;
        this.gameState = gameState;

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

    public void updateGraphics(final GameState gameState, final String updateTimeText, final String fpsText) {
        this.gameState = gameState;
        this.updateTimeText = updateTimeText;
        this.fpsText = fpsText;
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
        graphics.setColor(Color.WHITE);
        level.getRectangles().forEach(rectangle -> drawRectangle(rectangle, graphics));
    }

    private void drawShip(final ShipState shipState, final Graphics graphics) {
        graphics.drawImage(shipImage, (int) shipState.getPosition().getX(), (int) shipState.getPosition().getY(), this);
        drawDetectors(shipState, graphics);
    }

    private void drawDetectors(final ShipState shipState, final Graphics graphics) {
        final int lineLength = 100;
        final int shipPosX   = (int) shipState.getPosition().getX();
        final int shipPosY   = (int) shipState.getPosition().getY();
        final int shipHeight = shipImage.getHeight();
        final int shipWidth  = shipImage.getWidth();

        // Left side
        final Line2D leftLine1 = new Line2D(shipPosX, shipPosY, shipPosX - lineLength, shipPosY);
        final Line2D leftLine2 = new Line2D(shipPosX, (shipPosY + shipHeight / 2), shipPosX - lineLength, (shipPosY + shipHeight / 2));
        final Line2D leftLine3 = new Line2D(shipPosX, (shipPosY + shipHeight), shipPosX - lineLength, (shipPosY + shipHeight));

        // Right side
        final Line2D rightLine1 = new Line2D(shipPosX + shipWidth, shipPosY, shipPosX + shipWidth + lineLength, shipPosY);
        final Line2D rightLine2 = new Line2D(shipPosX + shipWidth, (shipPosY + shipHeight / 2), shipPosX + shipWidth + lineLength, (shipPosY + shipHeight / 2));
        final Line2D rightLine3 = new Line2D(shipPosX + shipWidth, (shipPosY + shipHeight), shipPosX + shipWidth + lineLength, (shipPosY + shipHeight));

        // Upper side
        final Line2D upperLine1 = new Line2D(shipPosX, shipPosY, shipPosX, shipPosY - lineLength);
        final Line2D upperLine2 = new Line2D((shipPosX + shipWidth / 2), shipPosY, (shipPosX + shipWidth / 2), shipPosY - lineLength);
        final Line2D upperLine3 = new Line2D((shipPosX + shipWidth), shipPosY, (shipPosX + shipWidth), shipPosY - lineLength);

        // Lower side
        final Line2D lowerLine1 = new Line2D(shipPosX, shipPosY + shipHeight, shipPosX, shipPosY + shipHeight + lineLength);
        final Line2D lowerLine2 = new Line2D((shipPosX + shipWidth / 2), shipPosY + shipHeight, (shipPosX + shipWidth / 2), shipPosY + shipHeight + lineLength);
        final Line2D lowerLine3 = new Line2D((shipPosX + shipWidth), shipPosY + shipHeight, (shipPosX + shipWidth), shipPosY + shipHeight + lineLength);

        drawDetector(leftLine1, graphics);
        drawDetector(leftLine2, graphics);
        drawDetector(leftLine3, graphics);

        drawDetector(rightLine1, graphics);
        drawDetector(rightLine2, graphics);
        drawDetector(rightLine3, graphics);

        drawDetector(upperLine1, graphics);
        drawDetector(upperLine2, graphics);
        drawDetector(upperLine3, graphics);

        drawDetector(lowerLine1, graphics);
        drawDetector(lowerLine2, graphics);
        drawDetector(lowerLine3, graphics);
    }

    private void drawDetector(final Line2D line, final Graphics graphics) {
        final Color color = getDetectorColor(line, graphics);
        graphics.setColor(color);
        drawLine(line, graphics);
    }

    private Color getDetectorColor(final Line2D line, final Graphics graphics) {
        final java.awt.geom.Line2D awtLine2D = line.convertToAWTLine2D();
        for (final Rectangle2D rectangle : level.getRectangles()) {
            final Rectangle awtRectangle = rectangle.toAWTRectangle();
            if (awtRectangle.intersectsLine(awtLine2D)) {
                return Color.RED;
            }
        }
        return Color.GREEN;
    }

    private void drawLine(final Line2D line, final Graphics graphics) {
        graphics.drawLine(line.getX1(), line.getY1(), line.getX2(), line.getY2());
    }

    private void drawRectangle(final Rectangle2D rectangle, final Graphics graphics) {
        graphics.fillRect((int) rectangle.getX(), (int) rectangle.getY(), (int) rectangle.getWidth(), (int) rectangle.getHeight());
    }

    private void drawInfoPanel(final Graphics graphics) {
        final DecimalFormat formatter = new DecimalFormat("#0.0000");
        final Font          font      = new Font("Helvetica", Font.BOLD, 14);

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

        graphics.setColor(Color.YELLOW);
        graphics.setFont(font);
        graphics.drawString(updateTimeText, 400, 20);
        graphics.drawString(fpsText, 400, 40);
    }
}
