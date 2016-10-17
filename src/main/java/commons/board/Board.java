package commons.board;

import commons.gameengine.Coordinate;

public interface Board {
    int getNumberOfRows();
    int getNumberOfCols();

    boolean isOutsideBoard(Coordinate coordinate);

    void print();
    void reset();
}
