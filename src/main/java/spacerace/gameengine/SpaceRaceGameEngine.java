package spacerace.gameengine;

import spacerace.domain.Action;
import spacerace.domain.ShipState;

public interface SpaceRaceGameEngine {
    Action getAction(ShipState shipState);
}
