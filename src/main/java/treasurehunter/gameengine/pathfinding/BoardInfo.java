package treasurehunter.gameengine.pathfinding;

import java.util.ArrayList;
import java.util.List;

import commons.gameengine.board.PlayerColor;
import treasurehunter.board.Tile;
import treasurehunter.board.TreasureHunterBoard;

public class BoardInfo {

    public final List<PathNode> treasures;
    public final Tile           myTile;
    public final Tile           otherPlayerTile;

    private BoardInfo(final List<PathNode> treasures, final Tile myTile, final Tile otherPlayerTile) {
        this.treasures = treasures;
        this.myTile = myTile;
        this.otherPlayerTile = otherPlayerTile;
    }

    public static BoardInfo of(final TreasureHunterBoard board, final PlayerColor myColor) {
        final List<PathNode> treasures           = new ArrayList<>();
        Tile                 myPosition          = null;
        Tile                 otherPlayerPosition = null;

        for (int x = 0; x < board.getNumberOfCols(); x++) {
            for (int y = 0; y < board.getNumberOfRows(); y++) {
                final Tile tile = board.getTile(x, y);

                switch (tile.getState()) {
                    case TREASURE:
                        treasures.add(new PathNode(tile.getCoordinates().getX(), tile.getCoordinates().getY()));
                        break;
                    case RED:
                        if (myColor == PlayerColor.RED) {
                            myPosition = tile;
                        }
                        else {
                            otherPlayerPosition = tile;
                        }
                        break;
                    case YELLOW:
                        if (myColor == PlayerColor.YELLOW) {
                            myPosition = tile;
                        }
                        else {
                            otherPlayerPosition = tile;
                        }
                        break;
                    default:
                        break;
                }
            }
        }

        return new BoardInfo(treasures, myPosition, otherPlayerPosition);
    }
}
