package spacerace.level.graphics.rocketfire;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

import spacerace.domain.ShipState;
import spacerace.graphics.GraphicsUtils;
import spacerace.level.RocketFireGraphics;
import spacerace.level.ShipGraphics;

public class SimpleRocketFireGraphics implements RocketFireGraphics {

    private static final String ROCKET_FIRE_IMAGE_PATH = "../../../../spacerace/fire_50px.png";

    private BufferedImage loadedRocketFireImage;

    @Override
    public void paint(final ShipState shipState, final ShipGraphics shipGraphics, final Graphics2D graphics) {
        final BufferedImage rocketFireImage = getImage();
        if (shipState.getAccelerationDirection().getY() < 0 || shipState.isStabilize()) {
            graphics.drawImage(rocketFireImage, (int) shipState.getPosition().getX(), (int) shipState.getPosition().getY() + shipGraphics.getHeight(), null);
        }
        if (shipState.getAccelerationDirection().getY() > 0 || shipState.isStabilize()) {
            final BufferedImage rotatedImage = GraphicsUtils.rotateImage(rocketFireImage, 180);
            graphics.drawImage(rotatedImage, (int) shipState.getPosition().getX(), (int) shipState.getPosition().getY() - rocketFireImage.getHeight(), null);
        }
        if (shipState.getAccelerationDirection().getX() < 0 || shipState.isStabilize()) {
            final BufferedImage rotatedImage = GraphicsUtils.rotateImage(rocketFireImage, 270);
            graphics.drawImage(rotatedImage, (int) shipState.getPosition().getX() + shipGraphics.getWidth(), (int) shipState.getPosition().getY(), null);
        }
        if (shipState.getAccelerationDirection().getX() > 0 || shipState.isStabilize()) {
            final BufferedImage rotatedImage = GraphicsUtils.rotateImage(rocketFireImage, 90);
            graphics.drawImage(rotatedImage, (int) shipState.getPosition().getX() - rotatedImage.getWidth(), (int) shipState.getPosition().getY(), null);
        }
    }

    private BufferedImage getImage() {
        // Lazy
        if (loadedRocketFireImage == null) {
            try {
                this.loadedRocketFireImage = ImageIO.read(new File(getClass().getResource(ROCKET_FIRE_IMAGE_PATH).getFile()));
            }
            catch (final IOException e) {
                throw new IllegalArgumentException("Failed to load rocketfire image.", e);
            }
        }
        return loadedRocketFireImage;
    }
}
