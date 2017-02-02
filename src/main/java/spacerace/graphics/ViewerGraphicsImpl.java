package spacerace.graphics;

import java.awt.Dimension;
import java.util.List;
import javax.swing.JFrame;

import spacerace.domain.GameState;
import spacerace.domain.PlayerResult;
import spacerace.graphics.panel.ViewerPanel;
import spacerace.level.Level;
import spacerace.level.LevelRepository;

class ViewerGraphicsImpl implements ViewerGraphics {

    private final ViewerPanel panel;
    private final JFrame      frame;

    ViewerGraphicsImpl(final ViewerPanel panel, final JFrame frame) {
        this.panel = panel;
        this.frame = frame;
    }


    @Override
    public void setLevelNumber(final int levelNumber) {
        if (!panel.isLevelSet()) {
            final Level level = LevelRepository.getLevel(levelNumber);

            // Resize window to level size now that we know it
            frame.getContentPane().setPreferredSize(new Dimension(level.getWidth(), level.getHeight()));
            frame.pack();

            // Update graphics with level now that we know it
            panel.setLevel(level);
        }
    }

    @Override
    public void setPlayerResults(final List<PlayerResult> playerResults) {
        panel.setPlayerResults(playerResults);
    }

    @Override
    public void setState(final GameState gameState) {
        panel.setState(gameState);
    }
}
