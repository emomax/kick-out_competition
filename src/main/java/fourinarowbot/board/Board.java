package fourinarowbot.board;

import fourinarowbot.domain.Marker;

public interface Board {

    int getNumberOfRows();

    int getNumberOfCols();

    Marker getMarker(final int x, final int y);

    boolean isAnyMarkerAt(final int x, final int y);

    void placeMarker(final Marker marker);

    boolean isOutsideBoard(final int x, final int y);
}
