package spacerace.graphics;

import java.awt.event.KeyListener;
import java.io.IOException;

import spacerace.domain.GameState;
import spacerace.domain.GameStatistics;
import spacerace.level.Level;

public class SpaceRaceGraphicsFactory {

    public static SpaceRaceGraphics createGraphics(final Level level, final KeyListener keyListener, final GameState gameState, final GameStatistics gameStatistics) throws IOException {
        final SpaceRaceGraphicsPanel panel = new SpaceRaceGraphicsPanel(level, keyListener, gameState, gameStatistics);

        // Create and show frame containing panel
        new SpaceRaceGraphicsFrame(panel);

        return panel;
    }
}
