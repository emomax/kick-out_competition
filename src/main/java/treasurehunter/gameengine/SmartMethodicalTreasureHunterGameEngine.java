package treasurehunter.gameengine;


import commons.gameengine.board.PlayerColor;

import java.util.List;

import treasurehunter.TreasureHunterApplication;
import treasurehunter.board.Tile;
import treasurehunter.board.TreasureHunterBoard;
import treasurehunter.domain.Move;
import treasurehunter.domain.Orientation;
import treasurehunter.gameengine.pathfinding.Pathfinder;
import treasurehunter.gameengine.pathfinding.PathfinderResult;
import treasurehunter.gameengine.pathfinding.BoardInfo;
import treasurehunter.gameengine.pathfinding.PathNode;


public class SmartMethodicalTreasureHunterGameEngine extends TreasureHunterGameEngine {

    public Move getNextMove(final TreasureHunterBoard board, final PlayerColor myColor) {
        final BoardInfo boardInfo = BoardInfo.of(board, myColor);
        return getNextMove(board, boardInfo.myTile, boardInfo.treasures);
    }

    private Move getNextMove(final TreasureHunterBoard board, final Tile player, final List<PathNode> treasures) {
        int shortestRouteCost = Integer.MAX_VALUE;
        List<PathNode> shortestPath = null;

        for (final PathNode treasure : treasures) {
            final PathfinderResult pathfinderResult = Pathfinder.calculateShortestPath(board, player.getOrientation(), new PathNode(player.getCoordinates().getX(), player.getCoordinates().getY()), treasure);

            if (pathfinderResult.travelCost < shortestRouteCost) {
                shortestPath = pathfinderResult.path;
                shortestRouteCost = pathfinderResult.travelCost;
            }
        }

        // Whoops, did not find any treasures we could reach - let's charge forward!
        if (shortestPath == null || shortestPath.isEmpty()) {
            return Move.MOVE_FORWARD;
        }

        // Get second element in path as target, since first node is the one we're standing on
        return getMoveToReachTarget(player, shortestPath.get(1));
    }

    private Move getMoveToReachTarget(final Tile player, final PathNode target) {
        final Orientation targetOrientation = Pathfinder.getRelativeOrientationOfNeighbour(new PathNode(player.getCoordinates().getX(), player.getCoordinates().getY()), target);

        if (player.getOrientation() == Orientation.LEFT && (targetOrientation == Orientation.UP || targetOrientation == Orientation.RIGHT)
            || player.getOrientation() == Orientation.UP && (targetOrientation == Orientation.RIGHT || targetOrientation == Orientation.DOWN)
            || player.getOrientation() == Orientation.RIGHT && (targetOrientation == Orientation.DOWN || targetOrientation == Orientation.LEFT)
            || player.getOrientation() == Orientation.DOWN && targetOrientation == Orientation.LEFT) {
            return Move.ROTATE_RIGHT;
        }

        if (player.getOrientation() == Orientation.LEFT && targetOrientation == Orientation.DOWN
            || player.getOrientation() == Orientation.UP && targetOrientation == Orientation.LEFT
            || player.getOrientation() == Orientation.RIGHT && targetOrientation == Orientation.UP
            || player.getOrientation() == Orientation.DOWN && (targetOrientation == Orientation.RIGHT || targetOrientation == Orientation.UP)) {
            return Move.ROTATE_LEFT;
        }

        return Move.MOVE_FORWARD;
    }

    public static void main(final String[] args) {
        final TreasureHunterApplication treasureHunterApplication = new TreasureHunterApplication(new SmartMethodicalTreasureHunterGameEngine());

        treasureHunterApplication.runGameMultipleTimes(1000);
    }
}
