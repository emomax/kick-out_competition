package spacerace.domain;

import java.awt.geom.Ellipse2D;

public class Missile {

    private static final double SPEED         = 0.45;
    public static final  int    RADIUS        = 10;
    private static final long   MAX_LIVE_TIME = 2_000;

    private volatile Vector2D position;
    private final    Vector2D direction;
    private final long startTime = System.currentTimeMillis();

    public Missile(final Vector2D position, final Vector2D direction) {
        this.position = position;
        this.direction = direction;
    }

    public Vector2D getPosition() {
        return position;
    }

    public Ellipse2D getShape() {
        return new Ellipse2D.Double(position.getX(), position.getY(), RADIUS, RADIUS);
    }

    public void move(final long timeElapsed) {
        final double distanceX = SPEED * timeElapsed * direction.getX();
        final double distanceY = SPEED * timeElapsed * direction.getY();
        position = new Vector2D(position.getX() + distanceX, position.getY() + distanceY);
    }

    public boolean shouldSelfDestruct() {
        return (System.currentTimeMillis() - startTime) > MAX_LIVE_TIME;
    }
}
