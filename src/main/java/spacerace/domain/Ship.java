package spacerace.domain;

import java.awt.Color;
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
    private static final long   RESET_PAUSE_TIME       = 500;

    private final    String        name;
    private final    Color         color;
    private final    BufferedImage image;
    private volatile Vector2D      position;
    private volatile Vector2D speed                 = new Vector2D(0, 0);
    private volatile Vector2D accelerationDirection = new Vector2D(0, 0);
    private volatile boolean  stabilize             = false;
    private volatile boolean  resetFrozen           = false;
    private volatile Long     resetTime             = null;
    private          boolean  passedGoal            = false;
    private volatile Missile  missile               = null;

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

    public Vector2D getCenter() {
        final double centerX = getPosition().getX() + (getWidth() / 2);
        final double centerY = getPosition().getY() + (getHeight() / 2);
        return new Vector2D(centerX, centerY);
    }

    public Vector2D getSpeed() {
        return speed;
    }

    public void setSpeed(final Vector2D speed) {
        this.speed = speed;
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

    public boolean isResetFrozen() {
        return resetFrozen;
    }

    public Long getResetTime() {
        return resetTime;
    }

    public boolean isPassedGoal() {
        return passedGoal;
    }

    public Missile getMissile() {
        return missile;
    }

    public void setMissile(final Missile missile) {
        this.missile = missile;
    }

    public void reset(final Vector2D startPosition) {
        // Creating new vectors (instead of modifying existing ones) to enable thread sync with the volatile modifier. Volatile does
        // not seem to work if modifying existing object.
        setPosition(new Vector2D(startPosition.getX(), (startPosition.getY())));
        setSpeed(new Vector2D(0, 0));
        setAccelerationDirection(new Vector2D(0, 0));
        stabilize = false;
        resetFrozen = true;
        resetTime = System.currentTimeMillis();
    }

    public void setHasPassedGoalLine() {
        passedGoal = true;
        // Creating new vectors (instead of modifying existing ones) to enable thread sync with the volatile modifier. Volatile does
        // not seem to work if modifying existing object.
        setSpeed(new Vector2D(0, 0));
        setAccelerationDirection(new Vector2D(0, 0));
    }

    public void move(final long timeElapsed) {
        if (passedGoal || checkAndUpdateResetFrozen()) {
            return;
        }

        final double newSpeedX = getSpeed(speed.getX(), accelerationDirection.getX(), timeElapsed);
        final double newSpeedY = getSpeed(speed.getY(), accelerationDirection.getY(), timeElapsed);
        // Creating new vector (instead of modifying existing one) to enable thread sync with the volatile modifier. Volatile does not seem to work if modifying
        // existing object.
        setSpeed(new Vector2D(newSpeedX, newSpeedY));

        final double distanceX = speed.getX() * timeElapsed;
        final double distanceY = speed.getY() * timeElapsed;
        // Creating new vector (instead of modifying existing one) to enable thread sync with the volatile modifier. Volatile does not seem to work if modifying
        // existing object.
        setPosition(new Vector2D(position.getX() + distanceX, position.getY() + distanceY));
    }

    private boolean checkAndUpdateResetFrozen() {
        if (!resetFrozen) {
            return false;
        }
        else if ((System.currentTimeMillis() - resetTime) < RESET_PAUSE_TIME) {
            return true;
        }
        else {
            // Reset time has passed, move on
            resetFrozen = false;
            return false;
        }
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
}
