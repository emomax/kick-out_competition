package fourinarowbot.domain;

import commons.gameengine.Action;

public class FourInARowAction implements Action<Marker> {
    Marker marker;

    public FourInARowAction(Marker marker) {
        this.marker = marker;
    }

    public Marker get() {
        return marker;
    }

    public String getMethod() {
        return "placeMarker";
    }
}
