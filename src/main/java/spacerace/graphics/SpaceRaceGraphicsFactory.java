package spacerace.graphics;

import java.awt.event.KeyListener;
import java.io.IOException;
import java.util.List;

import spacerace.domain.GameState;
import spacerace.domain.Statistics;
import spacerace.level.Level;

public class SpaceRaceGraphicsFactory {

    public static SpaceRaceGraphics createGraphics(final Level level, final List<KeyListener> keyListeners, final GameState gameState, final Statistics gameCycleStatistics, final Statistics responseTimeStatistics, final String playerName) throws IOException {
        final SpaceRaceGraphicsPanel panel = new SpaceRaceGraphicsPanel(level, keyListeners, gameState, gameCycleStatistics, responseTimeStatistics, playerName);

        // Create and show frame containing panel
        new SpaceRaceGraphicsFrame(panel, level);

        return panel;
    }
}
