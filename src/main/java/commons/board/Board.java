package commons.board;

/**
 * Created by maxjonsson on 2016-10-13.
 */
public interface Board {
    int getNumberOfRows();
    int getNumberOfCols();

    boolean isOutsideBoard(final int x, final int y);
}
