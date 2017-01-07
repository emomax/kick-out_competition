package spacerace.graphics;

import java.io.IOException;
import javax.swing.JFrame;

import spacerace.level.Level;

class SpaceRaceGraphicsFrame extends JFrame {

    SpaceRaceGraphicsFrame(final SpaceRaceGraphicsPanel panel) throws IOException {
        add(panel);
        setTitle("Space Race");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize((int) Level.DIMENSIONS.getX(), (int) Level.DIMENSIONS.getY());
        setLocationRelativeTo(null);
        setVisible(true);
        setResizable(false);
    }
}
