package spacerace.graphics;

import java.awt.Dimension;
import java.io.IOException;
import javax.swing.JFrame;

import spacerace.graphics.panel.GraphicsPanel;
import spacerace.graphics.panel.ViewerPanel;
import spacerace.level.Level;

class GraphicsFrame extends JFrame {

    GraphicsFrame(final GraphicsPanel panel) throws IOException {
        this(panel, null);
    }

    GraphicsFrame(final GraphicsPanel panel, final Level level) throws IOException {
        add(panel);
        setTitle("Space Race");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        if (level != null) {
            getContentPane().setPreferredSize(new Dimension(level.getWidth(), level.getHeight()));
        }
        else {
            final Dimension size = new Dimension(ViewerPanel.WAITING_FRAME_WIDTH, ViewerPanel.WAITING_FRAME_HEIGHT);
            getContentPane().setPreferredSize(size);
        }
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
        setResizable(false);
    }
}
