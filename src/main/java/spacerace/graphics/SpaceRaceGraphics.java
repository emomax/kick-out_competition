package spacerace.graphics;

import java.awt.event.KeyListener;
import java.io.IOException;

import spacerace.domain.GameState;
import spacerace.level.Level;

public class SpaceRaceGraphics {

    private final SpaceRaceGraphicsPanel panel;

    public SpaceRaceGraphics(final Level level, final KeyListener keyListener, final GameState gameState) throws IOException {
        panel = new SpaceRaceGraphicsPanel(level, keyListener, gameState);
        // Create and show frame containing panel
        new SpaceRaceGraphicsFrame(panel);
    }

    public void updateGraphics(final GameState gameState, final String updateTimeText, final String fpsText) {
        panel.updateGraphics(gameState, updateTimeText, fpsText);
    }
}
