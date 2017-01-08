package spacerace.domain;

import java.util.IntSummaryStatistics;
import java.util.LinkedList;

/**
 * This is sort of a circular fifo queue for game cycle times.
 */
public class GameStatistics {

    private static final int MAX_NUMBER_OF_ELEMENTS = 100;

    private final LinkedList<Integer> linkedList = new LinkedList<>();

    public void addCycleTime(final Integer element) {
        linkedList.add(element);
        while (linkedList.size() > MAX_NUMBER_OF_ELEMENTS) {
            linkedList.remove();
        }
    }

    public IntSummaryStatistics getGameCycleStatistics() {
        return linkedList.stream().mapToInt(Integer::intValue).summaryStatistics();
    }
}
