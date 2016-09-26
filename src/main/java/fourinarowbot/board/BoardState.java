package fourinarowbot.board;

import fourinarowbot.domain.Marker;

public class BoardState {

    private Marker[][] markers;

    private BoardState() {
        // For JSON-serialization
    }

    public BoardState(final Marker[][] markers) {
        this.markers = markers;
    }

    public Marker[][] getMarkers() {
        return markers;
    }
}
