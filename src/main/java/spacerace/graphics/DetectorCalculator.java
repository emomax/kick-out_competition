package spacerace.graphics;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import spacerace.domain.Line2D;
import spacerace.domain.Rectangle2D;
import spacerace.domain.ShipState;
import spacerace.level.Level;

import static java.util.stream.Collectors.toList;

public class DetectorCalculator {

    private final int DETECTOR_BEAM_MAX_LENGTH = 100;

    private final int             shipPosX;
    private final int             shipPosY;
    private final int             shipHeight;
    private final int             shipWidth;
    private final List<Rectangle> rectangles;

    DetectorCalculator(final ShipState shipState, final BufferedImage shipImage, final Level level) {
        this.shipPosX = (int) shipState.getPosition().getX();
        this.shipPosY = (int) shipState.getPosition().getY();
        this.shipHeight = shipImage.getHeight();
        this.shipWidth = shipImage.getWidth();
        this.rectangles = level.getRectangles().stream()
                .map(Rectangle2D::toAWTRectangle)
                .collect(toList());
    }

    List<Line2D> getDetectorBeams() {
        // Left side
        final Line2D leftLine1 = new Line2D(shipPosX, shipPosY, shipPosX - DETECTOR_BEAM_MAX_LENGTH, shipPosY);
        final Line2D leftLine2 = new Line2D(shipPosX, (shipPosY + shipHeight / 2), shipPosX - DETECTOR_BEAM_MAX_LENGTH, (shipPosY + shipHeight / 2));
        final Line2D leftLine3 = new Line2D(shipPosX, (shipPosY + shipHeight), shipPosX - DETECTOR_BEAM_MAX_LENGTH, (shipPosY + shipHeight));

        // Right side
        final Line2D rightLine1 = new Line2D(shipPosX + shipWidth, shipPosY, shipPosX + shipWidth + DETECTOR_BEAM_MAX_LENGTH, shipPosY);
        final Line2D rightLine2 = new Line2D(shipPosX + shipWidth, (shipPosY + shipHeight / 2), shipPosX + shipWidth + DETECTOR_BEAM_MAX_LENGTH, (shipPosY + shipHeight / 2));
        final Line2D rightLine3 = new Line2D(shipPosX + shipWidth, (shipPosY + shipHeight), shipPosX + shipWidth + DETECTOR_BEAM_MAX_LENGTH, (shipPosY + shipHeight));

        // Upper side
        final Line2D upperLine1 = new Line2D(shipPosX, shipPosY, shipPosX, shipPosY - DETECTOR_BEAM_MAX_LENGTH);
        final Line2D upperLine2 = new Line2D((shipPosX + shipWidth / 2), shipPosY, (shipPosX + shipWidth / 2), shipPosY - DETECTOR_BEAM_MAX_LENGTH);
        final Line2D upperLine3 = new Line2D((shipPosX + shipWidth), shipPosY, (shipPosX + shipWidth), shipPosY - DETECTOR_BEAM_MAX_LENGTH);

        // Lower side
        final Line2D lowerLine1 = new Line2D(shipPosX, shipPosY + shipHeight, shipPosX, shipPosY + shipHeight + DETECTOR_BEAM_MAX_LENGTH);
        final Line2D lowerLine2 = new Line2D((shipPosX + shipWidth / 2), shipPosY + shipHeight, (shipPosX + shipWidth / 2), shipPosY + shipHeight + DETECTOR_BEAM_MAX_LENGTH);
        final Line2D lowerLine3 = new Line2D((shipPosX + shipWidth), shipPosY + shipHeight, (shipPosX + shipWidth), shipPosY + shipHeight + DETECTOR_BEAM_MAX_LENGTH);


        final Line2D leftBeam1 = getDetectorBeam(leftLine1, DetectorPosition.LEFT);
        final Line2D leftBeam2 = getDetectorBeam(leftLine2, DetectorPosition.LEFT);
        final Line2D leftBeam3 = getDetectorBeam(leftLine3, DetectorPosition.LEFT);

        final Line2D rightBeam1 = getDetectorBeam(rightLine1, DetectorPosition.RIGHT);
        final Line2D rightBeam2 = getDetectorBeam(rightLine2, DetectorPosition.RIGHT);
        final Line2D rightBeam3 = getDetectorBeam(rightLine3, DetectorPosition.RIGHT);

        final Line2D upperBeam1 = getDetectorBeam(upperLine1, DetectorPosition.UP);
        final Line2D upperBeam2 = getDetectorBeam(upperLine2, DetectorPosition.UP);
        final Line2D upperBeam3 = getDetectorBeam(upperLine3, DetectorPosition.UP);

        final Line2D lowerBeam1 = getDetectorBeam(lowerLine1, DetectorPosition.DOWN);
        final Line2D lowerBeam2 = getDetectorBeam(lowerLine2, DetectorPosition.DOWN);
        final Line2D lowerBeam3 = getDetectorBeam(lowerLine3, DetectorPosition.DOWN);

        final List<Line2D> beams = new ArrayList<>();
        beams.add(leftBeam1);
        beams.add(leftBeam2);
        beams.add(leftBeam3);
        beams.add(rightBeam1);
        beams.add(rightBeam2);
        beams.add(rightBeam3);
        beams.add(upperBeam1);
        beams.add(upperBeam2);
        beams.add(upperBeam3);
        beams.add(lowerBeam1);
        beams.add(lowerBeam2);
        beams.add(lowerBeam3);
        beams.removeIf(Objects::isNull);
        return beams;
    }

    private Line2D getDetectorBeam(final Line2D line, final DetectorPosition detectorPosition) {
        final java.awt.geom.Line2D awtLine2D          = line.convertToAWTLine2D();
        final Integer              detectorBeamLength = getDetectorBeamLength(awtLine2D, detectorPosition);

        if (detectorBeamLength == null) {
            return null;
        }

        return createDetectorBeam(line, detectorPosition, detectorBeamLength);
    }

    private Integer getDetectorBeamLength(final java.awt.geom.Line2D awtLine2D, final DetectorPosition detectorPosition) {
        Integer detectorBeamLength = null;

        for (final Rectangle rectangle : rectangles) {
            if (rectangle.intersectsLine(awtLine2D)) {

                final int distanceDetectorToRectangle = getDetectorToRectangleDistance(detectorPosition, rectangle);

                if (detectorBeamLength == null || distanceDetectorToRectangle < detectorBeamLength) {
                    detectorBeamLength = distanceDetectorToRectangle;
                }
            }
        }
        return detectorBeamLength;
    }

    private int getDetectorToRectangleDistance(final DetectorPosition detectorPosition, final Rectangle rectangle) {
        if (detectorPosition == DetectorPosition.UP) {
            return shipPosY - ((int) rectangle.getY() + (int) rectangle.getHeight());
        }
        else if (detectorPosition == DetectorPosition.DOWN) {
            return (int) rectangle.getY() - (shipPosY + shipHeight);
        }
        else if (detectorPosition == DetectorPosition.LEFT) {
            return shipPosX - ((int) rectangle.getX() + (int) rectangle.getWidth());
        }
        else {
            return (int) rectangle.getX() - (shipPosX + shipWidth);
        }
    }

    private Line2D createDetectorBeam(final Line2D line, final DetectorPosition detectorPosition, final Integer detectorBeamLength) {
        if (detectorPosition == DetectorPosition.UP) {
            return new Line2D(line.getX1(), line.getY1(), line.getX1(), line.getY1() - detectorBeamLength);
        }
        else if (detectorPosition == DetectorPosition.DOWN) {
            return new Line2D(line.getX1(), line.getY1(), line.getX1(), line.getY1() + detectorBeamLength);
        }
        else if (detectorPosition == DetectorPosition.LEFT) {
            return new Line2D(line.getX1(), line.getY1(), line.getX1() - detectorBeamLength, line.getY1());
        }
        else {
            return new Line2D(line.getX1(), line.getY1(), line.getX1() + detectorBeamLength, line.getY1());
        }
    }
}
