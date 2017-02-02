package spacerace.graphics;

import java.util.List;

import spacerace.domain.GameState;
import spacerace.domain.PlayerResult;

public interface ViewerGraphics {

    void setLevelNumber(int levelNumber);

    void setPlayerResults(List<PlayerResult> playerResults);

    void setState(GameState gameState);
}
