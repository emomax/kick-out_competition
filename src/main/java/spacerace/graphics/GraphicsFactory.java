package spacerace.graphics;

import java.awt.event.KeyListener;
import java.io.IOException;
import java.util.List;

import spacerace.domain.GameState;
import spacerace.domain.Statistics;
import spacerace.graphics.panel.PlayerPanel;
import spacerace.graphics.panel.ViewerPanel;
import spacerace.level.Level;

public class GraphicsFactory {

    public static PlayerGraphics createPlayerGraphics(final Level level, final List<KeyListener> keyListeners, final GameState gameState, final Statistics gameCycleStatistics, final Statistics responseTimeStatistics, final String playerName) throws IOException {
        final PlayerPanel panel = new PlayerPanel(level, keyListeners, gameState, gameCycleStatistics, responseTimeStatistics, playerName);

        // Create and show frame containing panel
        new GraphicsFrame(panel, level);

        return panel;
    }

    public static ViewerGraphics createViewerGraphics(final GameState gameState, final Statistics gameCycleStatistics, final Statistics responseTimeStatistics, final String gameName) throws IOException {
        final ViewerPanel panel = new ViewerPanel(gameState, gameCycleStatistics, responseTimeStatistics, gameName);

        // Create and show frame containing panel
        final GraphicsFrame frame = new GraphicsFrame(panel);

        return new ViewerGraphicsImpl(panel, frame);
    }
}
