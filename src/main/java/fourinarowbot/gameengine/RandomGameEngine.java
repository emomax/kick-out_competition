package fourinarowbot.gameengine;

import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

import fourinarowbot.Board;
import fourinarowbot.domain.Coordinates;

import static java.util.stream.Collectors.toList;

public class RandomGameEngine implements GameEngine {

    @Override
    public Coordinates getCoordinatesForNextMakerToPlace(final Board board) {

        // Get column index in random order
        final List<Integer> colIndexes = IntStream.range(0, board.getNumberOfCols())
                .boxed()
                .collect(toList());

        final long seed = System.nanoTime();
        Collections.shuffle(colIndexes, new Random(seed));

        // Add a marker in the first column with free space
        for (final Integer colIndex : colIndexes) {
            for (int rowIndex = board.getNumberOfRows() - 1; rowIndex >= 0; rowIndex--) {
                if (!board.isAnyMarkerAt(colIndex, rowIndex)) {
                    return new Coordinates(colIndex, rowIndex);
                }
            }
        }

        throw new IllegalStateException("I couldnt decide where to put the marker!");
    }
}
