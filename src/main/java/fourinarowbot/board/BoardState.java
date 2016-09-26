package fourinarowbot.board;

import fourinarowbot.domain.Marker;

public class BoardState {

    private Marker[][] markers;

    public BoardState(final Marker[][] markers) {
        this.markers = markers;
    }

    public Marker[][] getMarkers() {
        return markers;
    }

    public void setMarkers(final Marker[][] markers) {
        this.markers = markers;
    }
}
