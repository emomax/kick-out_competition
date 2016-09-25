package fourinarowbot;

import fourinarowbot.domain.Marker;

public interface Board {

    public int getNumberOfRows();

    public int getNumberOfCols();

    public Marker getMarker(final int x, final int y);

    public boolean isAnyMarkerAt(final int x, final int y);

    public void placeMarker(final Marker marker);

    public boolean isOutsideBoard(final int x, final int y);
}
