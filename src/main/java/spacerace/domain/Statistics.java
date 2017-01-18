package spacerace.domain;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.IntSummaryStatistics;
import java.util.LinkedList;
import java.util.List;

/**
 * This is sort of a circular fifo queue for time measurement.
 */
public class Statistics {

    private static final int MAX_NUMBER_OF_ELEMENTS = 100;

    private final LinkedList<Integer> linkedList = new LinkedList<>();

    public synchronized void add(final Integer element) {
        linkedList.add(element);
        while (linkedList.size() > MAX_NUMBER_OF_ELEMENTS) {
            linkedList.remove();
        }
    }

    public synchronized IntSummaryStatistics getStatistics() {
        return linkedList.stream().mapToInt(Integer::intValue).summaryStatistics();
    }

    public synchronized int getMedian() {
        final List<Integer> list = new ArrayList<>(linkedList);
        list.sort(Comparator.naturalOrder());
        return list.get(Math.round(MAX_NUMBER_OF_ELEMENTS / 2.0f));
    }
}
