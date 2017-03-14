package spacerace.domain;

import java.io.Serializable;
import java.util.Objects;

public class Action implements Serializable {

    private final Acceleration accelerationX;
    private final Acceleration accelerationY;
    private final boolean      stabilize;
    private       Double       missileAngle;

    private Action(final Acceleration accelerationX, final Acceleration accelerationY, final boolean stabilize) {
        Objects.requireNonNull(accelerationX);
        Objects.requireNonNull(accelerationY);
        this.accelerationX = accelerationX;
        this.accelerationY = accelerationY;
        this.stabilize = stabilize;
    }

    public static Action withStabilize() {
        return new Action(Acceleration.NONE, Acceleration.NONE, true);
    }

    public static Action withAcceleration(final Acceleration accelerationX, final Acceleration accelerationY) {
        return new Action(accelerationX, accelerationY, false);
    }

    public void setMissileAngle(final double missileAngle) {
        this.missileAngle = missileAngle;
    }

    public Action andMissileAngle(final double missileAngle) {
        this.missileAngle = missileAngle;
        return this;
    }

    public Acceleration getAccelerationX() {
        return accelerationX;
    }

    public Acceleration getAccelerationY() {
        return accelerationY;
    }

    public boolean isStabilize() {
        return stabilize;
    }

    public Double getMissileAngle() {
        return missileAngle;
    }

    @Override
    public String toString() {
        return "Action{" +
               "accelerationX=" + accelerationX +
               ", accelerationY=" + accelerationY +
               ", stabilize=" + stabilize +
               ", missileAngle=" + missileAngle +
               '}';
    }
}
