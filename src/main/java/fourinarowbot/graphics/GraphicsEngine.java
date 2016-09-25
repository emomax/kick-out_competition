package fourinarowbot.graphics;

import java.awt.BorderLayout;
import javax.swing.JApplet;
import javax.swing.JFrame;

import fourinarowbot.domain.Marker;
import fourinarowbot.gameengine.RandomGameEngine;
import fourinarowbot.Board;
import fourinarowbot.FourInARowApplication;

public class GraphicsEngine extends JApplet {

    private final BoardGraphics boardGraphics;

    public GraphicsEngine(final Board board) {
        boardGraphics = new BoardGraphics(board);

        this.setLayout(new BorderLayout());
        this.add(boardGraphics, BorderLayout.CENTER);

        final JFrame window = new JFrame("Four in a row");
        window.setContentPane(this);
        window.pack();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setLocationRelativeTo(null); // Center window.
        window.setResizable(false);
        window.setVisible(true);
    }

    public void repaintGraphics(final Marker nextMarkerToPlace) {
        boardGraphics.addNextMarkerToPaint(nextMarkerToPlace);
        boardGraphics.repaint();
    }

    public static void main(final String[] args) {
        final FourInARowApplication fourInARowApplication = new FourInARowApplication(new RandomGameEngine());
        fourInARowApplication.runGameOnce();
    }
}
