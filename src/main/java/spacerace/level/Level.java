package spacerace.level;

import java.awt.Graphics2D;
import java.io.Serializable;
import java.util.List;
import java.util.function.Consumer;

import spacerace.domain.Line2D;
import spacerace.domain.Require;
import spacerace.domain.Vector2D;
import spacerace.level.graphics.rocketfire.SimpleRocketFireGraphics;
import spacerace.level.graphics.ship.SimpleShipGraphics;

public class Level implements Serializable {

    private static final int                DEFAULT_WIDTH                = 900;
    private static final int                DEFAULT_HEIGHT               = 600;
    private static final ShipGraphics       DEFAULT_SHIP_GRAPHICS        = new SimpleShipGraphics();
    private static final RocketFireGraphics DEFAULT_ROCKET_FIRE_GRAPHICS = new SimpleRocketFireGraphics();

    private int                  number;
    private long                 timeLimit;
    private int                  width;
    private int                  height;
    private Vector2D             startPosition;
    private Line2D               goalLine;
    private List<Line2D>         trackBorders;
    private Consumer<Graphics2D> baseLayerPainter;
    private Consumer<Graphics2D> topLayerPainter;
    private ShipGraphics         shipGraphics;
    private RocketFireGraphics   rocketFireGraphics;

    private Level() {
        // Intentionally empty
    }

    int getNumber() {
        return number;
    }

    public long getTimeLimit() {
        return timeLimit;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public List<Line2D> getTrackBorders() {
        return trackBorders;
    }

    public Vector2D getStartPosition() {
        return startPosition;
    }

    public Line2D getGoalLine() {
        return goalLine;
    }

    public ShipGraphics getShipGraphics() {
        return shipGraphics;
    }

    public RocketFireGraphics getRocketFireGraphics() {
        return rocketFireGraphics;
    }

    public void paintBaseLayer(final Graphics2D graphics) {
        baseLayerPainter.accept(graphics);
    }

    public void paintTopLayer(final Graphics2D graphics) {
        topLayerPainter.accept(graphics);
    }

    static class Builder {
        private final Level level;

        private Builder(final Level level) {
            this.level = level;
        }

        static Builder aLevel() {
            final Level level = new Level();
            return new Builder(level);
        }

        Builder withNumber(final int number) {
            level.number = number;
            return this;
        }

        Builder withTimeLimit(final long timeLimit) {
            level.timeLimit = timeLimit;
            return this;
        }

        Builder withWidth(final int width) {
            level.width = width;
            return this;
        }

        Builder withHeight(final int height) {
            level.height = height;
            return this;
        }

        Builder withStartPosition(final Vector2D startPosition) {
            level.startPosition = startPosition;
            return this;
        }

        Builder withGoalLine(final Line2D goalLine) {
            level.goalLine = goalLine;
            return this;
        }

        Builder withTrackBoarders(final List<Line2D> trackBorders) {
            level.trackBorders = trackBorders;
            return this;
        }

        Builder withBaseLayerPainter(final Consumer<Graphics2D> baseLayerPainter) {
            level.baseLayerPainter = baseLayerPainter;
            return this;
        }

        public Builder withTopLayerPainter(final Consumer<Graphics2D> topLayerPainter) {
            level.topLayerPainter = topLayerPainter;
            return this;
        }

        public Builder withShipGraphics(final ShipGraphics shipGraphics) {
            level.shipGraphics = shipGraphics;
            return this;
        }

        public Builder withRocketFireGraphics(final RocketFireGraphics rocketFireGraphics) {
            level.rocketFireGraphics = rocketFireGraphics;
            return this;
        }

        Level build() {
            Require.that(level.number > 0, "Level number must be > 0");
            Require.that(level.timeLimit > 0, "Time limit must be > 0");
            Require.notNull(level.startPosition, level.goalLine, level.trackBorders);

            if (level.width == 0) {
                level.width = DEFAULT_WIDTH;
            }
            if (level.height == 0) {
                level.height = DEFAULT_HEIGHT;
            }
            if (level.shipGraphics == null) {
                level.shipGraphics = DEFAULT_SHIP_GRAPHICS;
            }
            if (level.rocketFireGraphics == null) {
                level.rocketFireGraphics = DEFAULT_ROCKET_FIRE_GRAPHICS;
            }
            if (level.baseLayerPainter == null) {
                level.baseLayerPainter = graphics -> {
                };
            }
            if (level.topLayerPainter == null) {
                level.topLayerPainter = graphics -> {
                };
            }
            return level;
        }
    }
}
