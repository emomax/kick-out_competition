package commons.gameengine.board;

import java.io.Serializable;

public class BoardState<T extends Serializable> implements Serializable {
    private T[][] cells;

    private BoardState() {}

    public BoardState(final T[][] cells) {
        this.cells = cells;
    }

    public T[][] getCells() {
        return cells;
    }
}
