package treasurehunter.board;

import java.awt.geom.Dimension2D;

import commons.board.Board;

public class TreasureHunterBoard implements Board {
    private int numberOfColumns = 20;
    private int numberOfRows = 20;

    @Override
    public int getNumberOfRows() {
        return numberOfRows;
    }

    @Override
    public int getNumberOfCols() {
        return numberOfColumns;
    }

    @Override
    public boolean isOutsideBoard(final int x, final int y) {
        return (x < 0 || y < 0 || x > getNumberOfCols() || y > getNumberOfRows());
    }

    @Override
    public void print() {}

    public void reset() {
        generateMap();
    }

    private void generateMap() {

    }
}
