package treasurehunter.domain;

import commons.network.server.Parameterable;

public enum Move implements Parameterable {
    MOVE_FORWARD,
    ROTATE_RIGHT,
    ROTATE_LEFT;

    @Override
    public String toGetValueString() {
        return "move=" + this.toString();
    }
}
