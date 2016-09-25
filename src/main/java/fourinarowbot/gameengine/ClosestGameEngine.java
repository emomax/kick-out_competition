package fourinarowbot.gameengine;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.IntStream;

import fourinarowbot.Board;
import fourinarowbot.domain.Coordinates;
import fourinarowbot.domain.Marker;
import fourinarowbot.Logger;
import fourinarowbot.domain.MarkerColor;

import static java.util.stream.Collectors.toList;

public class ClosestGameEngine implements GameEngine {

    private final MarkerColor      markerColor;
    private final RandomGameEngine randomGameEngine;

    public ClosestGameEngine(final MarkerColor markerColor) {
        this.markerColor = markerColor;
        this.randomGameEngine = new RandomGameEngine();
    }

    @Override
    public Coordinates getCoordinatesForNextMakerToPlace(final Board board) {

        final List<Integer> colIndexes = IntStream.range(0, board.getNumberOfCols())
                .boxed()
                .collect(toList());


        // Iterate all columns top-down and if we find one of our markers, try to place a marker next to it
        for (final Integer colIndex : colIndexes) {
            for (int rowIndex = 0; rowIndex < board.getNumberOfRows(); rowIndex++) {
                //            for (int rowIndex = fourinarowbot.board.getNumberOfRows() - 1; rowIndex >= 0; rowIndex--) {

                final Marker marker = board.getMarker(colIndex, rowIndex);
                if (marker == null || marker.getColor() != markerColor) {
                    // No marker found or not our color
                    continue;
                }

                final List<Coordinates> coordinatedAroundMarker = getCoordinatedAroundMarker(colIndex, rowIndex);
                for (final Coordinates coordinates : coordinatedAroundMarker) {
                    if (isValidToPlaceMarkerAt(coordinates, board)) {

                        Logger.logDebug("### Marker at " + marker.getCoordinates());
                        Logger.logDebug("### Allowed around it are:");
                        coordinatedAroundMarker.stream().map(c -> c.toString()).forEach(Logger::logDebug);
                        Logger.logDebug("### chose: " + coordinates);
                        //                        try {
                        //                            Thread.sleep(500);
                        //                        } catch (final InterruptedException e) {
                        //                            e.printStackTrace();
                        //                        }

                        return coordinates;
                    }
                }
            }
        }

        Logger.logDebug("Closest algorithm failed...");

        // Ok we can't find a sweet spot to place our marker at. Could be first round. Just place is anywhere where there is room then
        return randomGameEngine.getCoordinatesForNextMakerToPlace(board);

        //        throw new IllegalStateException("I couldnt decide where to put the marker!");
    }

    private List<Coordinates> getCoordinatedAroundMarker(final int x, final int y) {
        final List<Coordinates> coordinates = new ArrayList<>();

        final Coordinates left  = new Coordinates(x - 1, y);
        final Coordinates above = new Coordinates(x, y - 1);
        final Coordinates right = new Coordinates(x + 1, y);

        // The order here decides in which direction we would prefer to go. Vertical placing feels smartest when playing against bots.
        return Arrays.asList(above, left, right);
    }


    private boolean isValidToPlaceMarkerAt(final Coordinates coordinates, final Board board) {
        // Replace by predicates...
        final Predicate<Coordinates> leftOfBoard             = c -> c.getX() < 0;
        final Predicate<Coordinates> rightOfBoard            = c -> c.getX() > (board.getNumberOfCols() - 1);
        final Predicate<Coordinates> aboveBoard              = c -> c.getY() < 0;
        final Predicate<Coordinates> alreadyMarkerAtPosition = c -> board.getMarker(c.getX(), c.getY()) != null;
        final Predicate<Coordinates> isNoMarkerBelow = c ->
                (c.getY() < board.getNumberOfRows() - 1) && board.getMarker(c.getX(), c.getY() + 1) == null;

        Logger.logDebug("%% about to test: " + coordinates);

        if (leftOfBoard.test(coordinates)) {
            Logger.logDebug("%% left of fourinarowbot.board");
            return false;
        }
        else if (rightOfBoard.test(coordinates)) {
            Logger.logDebug("%% right of fourinarowbot.board");
        }
        else if (aboveBoard.test(coordinates)) {
            Logger.logDebug("%% above fourinarowbot.board");
        }
        else if (alreadyMarkerAtPosition.test(coordinates)) {
            Logger.logDebug("%% already marker att position");
        }
        else if (isNoMarkerBelow.test(coordinates)) {
            Logger.logDebug("%% is no marker below");
        }

        final Predicate<Coordinates> isInvalidCoordinatesPredicate =
                leftOfBoard.or(
                        rightOfBoard.or(
                                aboveBoard.or(
                                        alreadyMarkerAtPosition.or(
                                                isNoMarkerBelow
                                        ))));


        return isInvalidCoordinatesPredicate.negate().test(coordinates);
    }
}
