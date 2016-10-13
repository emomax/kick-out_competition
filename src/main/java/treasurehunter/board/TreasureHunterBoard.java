package treasurehunter.board;

import commons.board.Board;

public class TreasureHunterBoard implements Board {
    @Override
    public int getNumberOfRows() {
        return 0;
    }

    @Override
    public int getNumberOfCols() {
        return 0;
    }

    @Override
    public boolean isOutsideBoard(final int x, final int y) {
        return false;
    }

    @Override
    public void print() {}

    public void reset() {}
}
