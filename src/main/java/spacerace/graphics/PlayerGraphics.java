package spacerace.graphics;

import java.util.List;

import spacerace.domain.Detector;
import spacerace.domain.GameState;
import spacerace.domain.PlayerResult;

public interface PlayerGraphics {

    void setPlayerResults(List<PlayerResult> playerResults);

    void setState(GameState gameState);

    void setDetectors(List<Detector> detectors);
}
