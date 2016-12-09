package treasurehunter.gameengine.pathfinding;

public class PathNode {
    public final int x;
    public final int y;
    public PathNode parent;
    public int currentCost        = Integer.MAX_VALUE;
    public int previousTravelCost = Integer.MAX_VALUE;

    public PathNode(final int x, final int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return x + ":" + y;
    }

    @Override
    public boolean equals(final Object obj) {
        return obj instanceof PathNode && ((PathNode) obj).x == x && ((PathNode) obj).y == y;
    }
}
