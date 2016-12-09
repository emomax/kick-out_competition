package treasurehunter.gameengine.pathfinding;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import treasurehunter.domain.Orientation;

public class PathfinderResult {
    public final int            travelCost;
    public final Orientation    endOrientation;
    public final List<PathNode> path = new ArrayList<>();

    public PathfinderResult(final int travelCost, final Orientation endOrientation, final PathNode finalNode) {
        this.travelCost = travelCost;
        this.endOrientation = endOrientation;

        if (finalNode != null) {
            PathNode currentNode = finalNode;

            path.add(currentNode);
            while (currentNode.parent != null) {
                currentNode = currentNode.parent;
                path.add(currentNode);
            }
        }

        Collections.reverse(path);
    }
}
