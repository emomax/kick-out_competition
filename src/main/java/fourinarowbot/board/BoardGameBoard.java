package fourinarowbot.board;

import fourinarowbot.domain.Marker;

public interface BoardGameBoard {

    int getNumberOfRows();
    int getNumberOfCols();

    boolean isOutsideBoard(final int x, final int y);

    // TODO these shouldn't be here.
    Marker getMarker(final int x, final int y);
    boolean isAnyMarkerAt(final int x, final int y);
    void placeMarker(final Marker marker);
}
