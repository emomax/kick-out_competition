package spacerace.domain;

import spacerace.graphics.DetectorPosition;

public class Detector {

    private final DetectorPosition detectorPosition;
    private final int              number;
    private final int              x;
    private final int              y;
    private final Line2D           beam;

    public Detector(final DetectorPosition detectorPosition, final int number, final int x, final int y, final Line2D beam) {
        this.detectorPosition = detectorPosition;
        this.number = number;
        this.x = x;
        this.y = y;
        this.beam = beam;
    }

    private DetectorPosition getDetectorPosition() {
        return detectorPosition;
    }

    public int getNumber() {
        return number;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Line2D getBeam() {
        return beam;
    }

    public Integer getBeamLength() {
        if (beam == null) {
            return null;
        }
        // One of these two will be 0 but still, look better to split than just to add them.
        return Math.abs(beam.getX1() - beam.getX2()) + Math.abs(beam.getY1() - beam.getY2());
    }
}
