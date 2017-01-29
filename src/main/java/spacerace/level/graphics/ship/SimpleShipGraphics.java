package spacerace.level.graphics.ship;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

import spacerace.domain.Rectangle2D;
import spacerace.domain.ShipState;
import spacerace.graphics.GraphicsUtils;
import spacerace.level.ShipGraphics;

public class SimpleShipGraphics implements ShipGraphics {

    private static final String SHIP_IMAGE_PATH = "../../../../spacerace/ship2.png";

    private BufferedImage shipImage;

    @Override
    public int getWidth() {
        return getImage().getWidth();
    }

    @Override
    public int getHeight() {
        return getImage().getHeight();
    }

    @Override
    public void paint(final ShipState shipState, final Graphics2D graphics) {
        final Color       playerColor    = new Color(shipState.getColorRGB());
        final int         x              = (int) shipState.getPosition().getX();
        final int         y              = (int) shipState.getPosition().getY();
        final Rectangle2D colorRectangle = new Rectangle2D(x, y, getWidth(), getHeight());

        GraphicsUtils.drawRectangle(colorRectangle, playerColor, graphics);
        graphics.drawImage(getImage(), x, y, null);
    }

    private BufferedImage getImage() {
        // Lazy load
        if (shipImage == null) {
            try {
                this.shipImage = ImageIO.read(new File(getClass().getResource(SHIP_IMAGE_PATH).getFile()));
            }
            catch (final IOException e) {
                throw new IllegalArgumentException("Failed to load ship image.", e);
            }
        }
        return shipImage;
    }
}
