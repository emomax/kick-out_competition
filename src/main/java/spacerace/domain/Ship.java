package spacerace.domain;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;


public class Ship {

    private static final String SHIP_IMAGE_DIR         = "../../spacerace/ship2.png";
    private static final double MAX_SPEED              = 0.4;
    private static final double ACCELERATION           = 0.7;
    private static final double STABILIZE_ACCELERATION = 0.4;
    private static final double STABILIZE_STOP_SPEED   = 0.01;

    private final String        name;
    private final Color         color;
    private       Vector2D      position;
    private final BufferedImage image;
    private          Vector2D speed                 = new Vector2D(0, 0);
    private volatile Vector2D accelerationDirection = new Vector2D(0, 0);
    private volatile boolean  stabilize             = false;

    public Ship(final String name, final Color color, final Vector2D startPosition) throws IOException {
        this.image = ImageIO.read(new File(getClass().getResource(SHIP_IMAGE_DIR).getFile()));
        this.name = name;
        this.color = color;
        this.position = new Vector2D(startPosition.getX(), startPosition.getY());
    }

    public String getName() {
        return name;
    }

    public double getX() {
        return position.getX();
    }

    public double getY() {
        return position.getY();
    }

    public Vector2D getPosition() {
        return position;
    }

    public void setPosition(final Vector2D position) {
        this.position = position;
    }

    public int getWidth() {
        return image.getWidth();
    }

    public int getHeight() {
        return image.getHeight();
    }

    public double getSpeedX() {
        return speed.getX();
    }

    public double getSpeedY() {
        return speed.getY();
    }

    public Vector2D getSpeed() {
        return speed;
    }

    public void setSpeed(final Vector2D speed) {
        this.speed = speed;
    }

    public double getAccelerationDirectionX() {
        return accelerationDirection.getX();
    }

    public double getAccelerationDirectionY() {
        return accelerationDirection.getY();
    }

    public Vector2D getAccelerationDirection() {
        return accelerationDirection;
    }

    public void setAccelerationDirection(final Vector2D accelerationDirection) {
        this.accelerationDirection = accelerationDirection;
    }

    public boolean isStabilize() {
        return stabilize;
    }

    public void setStabilize(final boolean stabilize) {
        this.stabilize = stabilize;
    }

    public Color getColor() {
        return color;
    }

    public void reset(final Vector2D startPosition) {
        position.setX(startPosition.getX());
        position.setY(startPosition.getY());
        speed.setX(0);
        speed.setY(0);
        accelerationDirection.setX(0);
        accelerationDirection.setY(0);
        stabilize = false;
    }

    public void move(final long timeElapsed) {
        final double newSpeedX = getSpeed(speed.getX(), accelerationDirection.getX(), timeElapsed);
        final double newSpeedY = getSpeed(speed.getY(), accelerationDirection.getY(), timeElapsed);
        speed.setX(newSpeedX);
        speed.setY(newSpeedY);

        final double distanceX = speed.getX() * timeElapsed;
        final double distanceY = speed.getY() * timeElapsed;
        position.setX(position.getX() + distanceX);
        position.setY(position.getY() + distanceY);
    }

    private double getSpeed(final double currentSpeed, final double accelerationDirection, final long timeElapsed) {
        if (stabilize) {
            return calculateStabilizationSpeed(currentSpeed, timeElapsed);
        }
        else if (accelerationDirection != 0) {
            return calculateSpeed(currentSpeed, accelerationDirection, timeElapsed);
        }
        else {
            return currentSpeed;
        }
    }

    private double calculateStabilizationSpeed(final double currentSpeed, final long timeElapsed) {
        if (currentSpeed == 0) {
            return 0;
        }
        // We accelerate in the opposite direction of movement, ignoring any other acceleration.  v1 = v0 + at
        final double accelerationDirection = -Math.copySign(1, currentSpeed);
        final double calculatedSpeed       = currentSpeed + (Long.valueOf(timeElapsed).doubleValue() / 1000d) * STABILIZE_ACCELERATION * accelerationDirection;
        if (Math.abs(calculatedSpeed) < STABILIZE_STOP_SPEED) {
            return 0;
        }
        else {
            return calculatedSpeed;
        }
    }

    private double calculateSpeed(final double currentSpeed, final double accelerationDirection, final long timeElapsed) {
        // This can increase or decrease speed depending on acceleration direction. v1 = v0 + at
        final double calculatedSpeed = currentSpeed + (Long.valueOf(timeElapsed).doubleValue() / 1000d) * ACCELERATION * accelerationDirection;
        if (Math.abs(calculatedSpeed) > MAX_SPEED) {
            return Math.copySign(MAX_SPEED, currentSpeed);
        }
        else {
            return calculatedSpeed;
        }
    }

    public void keyPressed(final KeyEvent e) {
        final int key = e.getKeyCode();

        if (key == KeyEvent.VK_SPACE) {
            stabilize = true;
        }
        else if (key == KeyEvent.VK_LEFT) {
            accelerationDirection.setX(-1);
        }
        else if (key == KeyEvent.VK_RIGHT) {
            accelerationDirection.setX(1);
        }
        else if (key == KeyEvent.VK_UP) {
            accelerationDirection.setY(-1);
        }
        else if (key == KeyEvent.VK_DOWN) {
            accelerationDirection.setY(1);
        }
    }

    public void keyReleased(final KeyEvent e) {
        final int key = e.getKeyCode();

        if (key == KeyEvent.VK_SPACE) {
            stabilize = false;
        }
        else if (key == KeyEvent.VK_LEFT || key == KeyEvent.VK_RIGHT) {
            accelerationDirection.setX(0);
        }
        else if (key == KeyEvent.VK_UP || key == KeyEvent.VK_DOWN) {
            accelerationDirection.setY(0);
        }
    }
}
