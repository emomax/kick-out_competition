package treasurehunter.board;

import java.io.Serializable;

import treasurehunter.board.Tile.TileState;

public class BoardStateUpdate implements Serializable {

    private final Tile oldTile;
    private final Tile newTile;

    public BoardStateUpdate(final Tile oldTile, final Tile newTile) {
        this.oldTile = oldTile.clone();
        this.newTile = newTile.clone();
    }

    public String toString() {
        final StringBuilder sb = new StringBuilder();

        sb.append(createTileString("old", oldTile));
        sb.append(createTileString("new", newTile));

        return sb.toString();
    }

    private String createTileString(String label, Tile tile) {
        String stateString = "state" + tile.getState();
        if (tile.getState() == TileState.RED || tile.getState() == TileState.YELLOW) {
            stateString += "_" + oldTile.getOrientation();
        }

        int x = tile.getCoordinates().getX();
        int y = tile.getCoordinates().getY();

        return label + "-" + x + ":" + y + stateString;
    }
}
