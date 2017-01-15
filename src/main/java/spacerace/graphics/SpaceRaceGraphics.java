package spacerace.graphics;

import java.awt.Dimension;
import java.util.List;

import spacerace.domain.Detector;
import spacerace.domain.GameState;
import spacerace.domain.PlayerResult;

public interface SpaceRaceGraphics {

    void updateGraphics(final GameState gameState, final List<Detector> detectorBeams);

    public Dimension getShipImageDimension();

    void setPlayerResults(List<PlayerResult> playerResults);
}
