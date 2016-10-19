package treasurehunter.domain;

import java.io.Serializable;
import commons.gameengine.board.PlayerColor;

public class PlayerMove implements Serializable {
    private final Move        move;
    private final PlayerColor playerColor;

    public PlayerMove(PlayerColor playerColor, Move move) {
        this.move = move;
        this.playerColor = playerColor;
    }

    public String getMove() {
        return move.toString();
    }

    public String getPlayerColor() {
        return playerColor.toString();
    }
}
