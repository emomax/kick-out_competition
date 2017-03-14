package spacerace.domain;

import java.io.Serializable;
import java.util.List;

public class ShipState implements Serializable {

    private String         name;
    private Vector2D       speed;
    private Vector2D       position;
    private Vector2D       accelerationDirection;
    private boolean        stabilize;
    private int            colorRGB;
    private List<Detector> detectors;
    private boolean        resetFrozen;
    private boolean        passedGoal;
    private Vector2D       missilePosition;

    public ShipState() {
        // For JSON conversion
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public Vector2D getPosition() {
        return position;
    }

    public void setPosition(final Vector2D position) {
        this.position = position;
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

    public int getColorRGB() {
        return colorRGB;
    }

    public void setColorRGB(final int colorRGB) {
        this.colorRGB = colorRGB;
    }

    public List<Detector> getDetectors() {
        return detectors;
    }

    public void setDetectors(final List<Detector> detectors) {
        this.detectors = detectors;
    }

    public boolean isResetFrozen() {
        return resetFrozen;
    }

    public void setResetFrozen(final boolean resetFrozen) {
        this.resetFrozen = resetFrozen;
    }

    public boolean isPassedGoal() {
        return passedGoal;
    }

    public void setPassedGoal(final boolean passedGoal) {
        this.passedGoal = passedGoal;
    }

    public Vector2D getMissilePosition() {
        return missilePosition;
    }

    public void setMissilePosition(final Vector2D missilePosition) {
        this.missilePosition = missilePosition;
    }

    @Override
    public String toString() {
        return "ShipState{" +
               "name='" + name + '\'' +
               ", speed=" + speed +
               ", position=" + position +
               ", accelerationDirection=" + accelerationDirection +
               ", stabilize=" + stabilize +
               ", colorRGB=" + colorRGB +
               ", detectors=" + detectors +
               ", resetFrozen=" + resetFrozen +
               ", passedGoal=" + passedGoal +
               ", missilePosition=" + missilePosition +
               '}';
    }
}
