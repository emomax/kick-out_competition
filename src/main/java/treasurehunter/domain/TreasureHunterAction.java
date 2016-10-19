package treasurehunter.domain;

import commons.gameengine.Action;

public class TreasureHunterAction implements Action<Move> {
    Move move;

    public TreasureHunterAction(Move move) {
        this.move = move;
        System.out.println("Move is now: " + move.toString());
    }

    @Override
    public Move get() {
        return move;
    }

    @Override
    public String getMethod() {
        return "move";
    }
}
