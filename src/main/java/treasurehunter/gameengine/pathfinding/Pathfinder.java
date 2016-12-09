package treasurehunter.gameengine.pathfinding;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import commons.gameengine.board.Coordinate;
import treasurehunter.board.Tile.TileState;
import treasurehunter.board.TreasureHunterBoard;
import treasurehunter.domain.Orientation;

public class Pathfinder {

    // Find shortest path between start and target, using A*
    public static PathfinderResult calculateShortestPath(final TreasureHunterBoard board, final Orientation startDirection, final PathNode startNode, final PathNode targetNode) {

        final List<PathNode> openList   = new ArrayList<>();
        final List<PathNode> closedList = new ArrayList<>();

        startNode.previousTravelCost = 0;
        openList.add(startNode);

        while (!openList.isEmpty()) {
            openList.sort((PathNode n1, PathNode n2) -> Integer.valueOf(n1.currentCost).compareTo(Integer.valueOf(n2.currentCost)));
            final PathNode currentNode = openList.remove(0);

            final Orientation currDirection = currentNode.parent == null ? startDirection : getRelativeOrientationOfNeighbour(currentNode.parent, currentNode);

            if (currentNode.equals(targetNode)) {
                return new PathfinderResult(currentNode.previousTravelCost, currDirection, currentNode);
            }

            closedList.add(currentNode);

            for (final PathNode neighbourNode : getNeighbourNodes(currentNode, board)) {
                if (closedList.contains(neighbourNode)) {
                    continue;
                }

                final int addedCost           = getTurnCost(currDirection, currentNode, neighbourNode);
                final int potentialTravelCost = currentNode.previousTravelCost + (currentNode.equals(startNode) ? 0 : 1) + addedCost; //TODO account for turning as well

                int openListIndex = openList.indexOf(neighbourNode);

                if (openListIndex == -1) {
                    openList.add(neighbourNode);
                    openListIndex = openList.size() - 1;
                }
                else if (potentialTravelCost >= openList.get(openListIndex).previousTravelCost) {
                    continue;
                }

                openList.get(openListIndex).parent = currentNode;
                openList.get(openListIndex).previousTravelCost = potentialTravelCost;
                openList.get(openListIndex).currentCost = potentialTravelCost + getManhattanHeuristic(openList.get(openListIndex), targetNode);
            }

            closedList.add(currentNode);
        }

        // No path could be found
        return new PathfinderResult(Integer.MAX_VALUE, null, null);
    }

    private static int getManhattanHeuristic(final PathNode startNode, final PathNode endNode) {
        return Math.abs(startNode.x - endNode.x) + Math.abs(startNode.y - endNode.y);
    }

    private static int getTurnCost(final Orientation currentOrientation, final PathNode currentNode, final PathNode targetNode) {
        final Orientation requiredOrientation = getRelativeOrientationOfNeighbour(currentNode, targetNode);

        if (currentOrientation == requiredOrientation) {
            return 0;
        }
        if ((currentOrientation == Orientation.UP || currentOrientation == Orientation.DOWN)
            && (requiredOrientation == Orientation.LEFT || requiredOrientation == Orientation.RIGHT)) {
            return 1;
        }
        if ((currentOrientation == Orientation.LEFT || currentOrientation == Orientation.RIGHT)
            && (requiredOrientation == Orientation.UP || requiredOrientation == Orientation.DOWN)) {
            return 1;
        }
        if (currentOrientation == Orientation.LEFT && requiredOrientation == Orientation.RIGHT
            || currentOrientation == Orientation.RIGHT && requiredOrientation == Orientation.LEFT
            || currentOrientation == Orientation.UP && requiredOrientation == Orientation.DOWN
            || currentOrientation == Orientation.DOWN && requiredOrientation == Orientation.UP) {
            return 2;
        }

        throw new RuntimeException("Did not expect to get here, unhandled case: " + currentOrientation + "->" + requiredOrientation);
    }

    public static Orientation getRelativeOrientationOfNeighbour(final PathNode originNode, final PathNode targetNode) {
        if (Math.abs(originNode.x - targetNode.x) > 1
            || Math.abs(originNode.y - targetNode.y) > 1
            || originNode.x == targetNode.x && originNode.y == targetNode.y) {
            throw new RuntimeException("Origin node " + originNode + " and target node " + targetNode + " are not direct neighbours!");
        }

        if (originNode.x > targetNode.x) {
            return Orientation.LEFT;
        }
        if (originNode.x < targetNode.x) {
            return Orientation.RIGHT;
        }
        if (originNode.y > targetNode.y) {
            return Orientation.UP;
        }
        if (originNode.y < targetNode.y) {
            return Orientation.DOWN;
        }

        throw new RuntimeException("Whoops, how did we end up here?");
    }

    private static List<PathNode> getNeighbourNodes(final PathNode sourceNode, final TreasureHunterBoard board) {
        final List<PathNode> neighbourNodes = new ArrayList<>();

        final List<Coordinate> neighbours = Arrays.asList(
                new Coordinate(sourceNode.x + 1, sourceNode.y),
                new Coordinate(sourceNode.x - 1, sourceNode.y),
                new Coordinate(sourceNode.x, sourceNode.y + 1),
                new Coordinate(sourceNode.x, sourceNode.y - 1)
        );

        for (final Coordinate neighbour : neighbours) {
            final Optional<PathNode> node = getWalkableNode(neighbour, board);
            if (node.isPresent()) {
                neighbourNodes.add(node.get());
            }
        }

        return neighbourNodes;
    }

    private static Optional<PathNode> getWalkableNode(final Coordinate nodeCoordinate, final TreasureHunterBoard board) {
        // WalkableTileStates could also include other player, since we'll be walking all over them
        final List<TileState> walkableTileStates = Arrays.asList(TileState.EMPTY, TileState.TREASURE);

        final boolean tileIsWalkable = !board.isOutsideBoard(nodeCoordinate) &&
                                       walkableTileStates.contains(board.getTile(nodeCoordinate.getX(), nodeCoordinate.getY()).getState());

        return tileIsWalkable ? Optional.of(new PathNode(nodeCoordinate.getX(), nodeCoordinate.getY())) : Optional.empty();
    }
}
