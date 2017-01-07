package spacerace.domain;

import java.util.Collection;
import java.util.LinkedList;

public class CircularFifoQueue<E> {

    private final int limit;
    private final LinkedList<E> linkedList = new LinkedList<>();

    public CircularFifoQueue(final int limit) {
        this.limit = limit;
    }

    public void add(final E element) {
        linkedList.add(element);
        while (linkedList.size() > limit) {
            linkedList.remove();
        }
    }

    public Collection<E> asCollection() {
        return linkedList;
    }
}
