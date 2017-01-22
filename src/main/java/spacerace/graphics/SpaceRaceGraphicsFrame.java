package spacerace.graphics;

import java.awt.Dimension;
import java.io.IOException;
import javax.swing.JFrame;

import spacerace.level.Level;

class SpaceRaceGraphicsFrame extends JFrame {

    SpaceRaceGraphicsFrame(final SpaceRaceGraphicsPanel panel, final Level level) throws IOException {
        add(panel);
        setTitle("Space Race");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        getContentPane().setPreferredSize(new Dimension(level.getWidth(), level.getHeight()));
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
        setResizable(false);
    }
}
