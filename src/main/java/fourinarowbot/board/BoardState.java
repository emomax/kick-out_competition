package fourinarowbot.board;

import java.io.Serializable;

import fourinarowbot.domain.Marker;

public class BoardState implements Serializable {

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
