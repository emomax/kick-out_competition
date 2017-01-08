package spacerace.graphics;

import java.awt.Dimension;
import java.io.IOException;
import javax.swing.JFrame;

import spacerace.level.Level;

class SpaceRaceGraphicsFrame extends JFrame {

    SpaceRaceGraphicsFrame(final SpaceRaceGraphicsPanel panel) throws IOException {
        add(panel);
        setTitle("Space Race");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        getContentPane().setPreferredSize(new Dimension(Level.WIDTH, Level.HEIGHT));
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
        setResizable(false);
    }
}
