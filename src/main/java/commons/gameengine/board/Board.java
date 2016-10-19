package commons.gameengine.board;

import java.io.Serializable;

import commons.gameengine.Positionable;

public interface Board <T extends Positionable> extends Serializable {
    int getNumberOfRows();
    int getNumberOfCols();

    boolean isOutsideBoard(Coordinate coordinate);

    void print();
    void reset();

    T[][] getCells();
}
