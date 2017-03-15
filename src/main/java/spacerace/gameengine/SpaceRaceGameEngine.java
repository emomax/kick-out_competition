package spacerace.gameengine;

import java.util.List;

import spacerace.domain.Action;
import spacerace.domain.ShipState;
import spacerace.domain.Vector2D;

public interface SpaceRaceGameEngine {
    Action getAction(ShipState shipState, final List<Vector2D> otherShipPositions, final List<Vector2D> otherShipMissilePositions);
}
