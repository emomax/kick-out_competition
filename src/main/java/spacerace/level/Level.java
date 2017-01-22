package spacerace.level;

import java.awt.Graphics2D;
import java.io.Serializable;
import java.util.List;
import java.util.function.Consumer;

import spacerace.domain.Line2D;
import spacerace.domain.Require;
import spacerace.domain.Vector2D;

public class Level implements Serializable {

    static final         int    DEFAULT_WIDTH           = 900;
    static final         int    DEFAULT_HEIGHT          = 600;
    private static final String DEFAULT_SHIP_IMAGE_PATH = "../../spacerace/ship2.png";
    private static final String DEFAULT_FIRE_IMAGE_PATH = "../../spacerace/fire_50px.png";

    private int                  number;
    private int                  width;
    private int                  height;
    private Vector2D             startPosition;
    private Line2D               goalLine;
    private List<Line2D>         trackBorders;
    private Consumer<Graphics2D> baseLayerPainter;
    private Consumer<Graphics2D> topLayerPainter;
    private String               shipImagePath;
    private String               fireImagePath;

    private Level() {
        // Intentionally empty
    }

    int getNumber() {
        return number;
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

    public String getShipImagePath() {
        return shipImagePath;
    }

    public String getFireImagePath() {
        return fireImagePath;
    }

    public void paintBaseLayer(final Graphics2D graphics) {
        baseLayerPainter.accept(graphics);
    }

    public void paintTopLayer(final Graphics2D graphics) {
        topLayerPainter.accept(graphics);
    }

    public static class Builder {
        private final Level level;

        private Builder(final Level level) {
            this.level = level;
        }

        public static Builder aLevel() {
            final Level level = new Level();
            return new Builder(level);
        }

        public Builder withNumber(final int number) {
            level.number = number;
            return this;
        }

        public Builder withWidth(final int width) {
            level.width = width;
            return this;
        }

        public Builder withHeight(final int height) {
            level.height = height;
            return this;
        }

        public Builder withStartPosition(final Vector2D startPosition) {
            level.startPosition = startPosition;
            return this;
        }

        public Builder withGoalLine(final Line2D goalLine) {
            level.goalLine = goalLine;
            return this;
        }

        public Builder withTrackBoarders(final List<Line2D> trackBorders) {
            level.trackBorders = trackBorders;
            return this;
        }

        public Builder withBaseLayerPainter(final Consumer<Graphics2D> baseLayerPainter) {
            level.baseLayerPainter = baseLayerPainter;
            return this;
        }

        public Builder withTopLayerPainter(final Consumer<Graphics2D> topLayerPainter) {
            level.topLayerPainter = topLayerPainter;
            return this;
        }

        public Builder withShipImagePath(final String shipImagePath) {
            level.shipImagePath = shipImagePath;
            return this;
        }

        public Builder withFireImagePath(final String fireImagePath) {
            level.fireImagePath = fireImagePath;
            return this;
        }

        public Level build() {
            Require.that(level.number > 0, "Level number must be > 0");
            Require.notNull(level.startPosition, level.goalLine, level.trackBorders);

            if (level.width == 0) {
                level.width = DEFAULT_WIDTH;
            }
            if (level.height == 0) {
                level.height = DEFAULT_HEIGHT;
            }
            if (level.shipImagePath == null) {
                level.shipImagePath = DEFAULT_SHIP_IMAGE_PATH;
            }
            if (level.fireImagePath == null) {
                level.fireImagePath = DEFAULT_FIRE_IMAGE_PATH;
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
