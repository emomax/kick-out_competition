package spacerace.graphics;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RadialGradientPaint;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.Path2D;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class DrawStarShape {
    public static void main(final String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                createAndShowGUI();
            }
        });
    }

    private static void createAndShowGUI() {
        final JFrame f = new JFrame();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.getContentPane().add(new DrawStarShapePanel());
        f.setSize(600, 600);
        f.setLocationRelativeTo(null);
        f.setVisible(true);
    }
}

class DrawStarShapePanel extends JPanel {
    @Override
    protected void paintComponent(final Graphics gr) {
        super.paintComponent(gr);
        final Graphics2D graphics = (Graphics2D) gr;
        //        graphics.setColor(Color.WHITE);
        //        graphics.fillRect(0, 0, getWidth(), getHeight());

        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                  RenderingHints.VALUE_ANTIALIAS_ON);

        graphics.setPaint(new RadialGradientPaint(
                new Point(400, 200), 60, new float[]{ 0, 1 },
                new Color[]{ Color.YELLOW, Color.WHITE }));
        graphics.fill(createStar(400, 200, 20, 60, 8, 0));
    }

    private static Shape createStar(final double centerX, final double centerY,
            final double innerRadius, final double outerRadius, final int numRays,
            final double startAngleRad) {
        final Path2D path          = new Path2D.Double();
        final double deltaAngleRad = Math.PI / numRays;
        for (int i = 0; i < numRays * 2; i++) {
            final double angleRad = startAngleRad + i * deltaAngleRad;
            final double ca       = Math.cos(angleRad);
            final double sa       = Math.sin(angleRad);
            double       relX     = ca;
            double       relY     = sa;
            if ((i & 1) == 0) {
                relX *= outerRadius;
                relY *= outerRadius;
            }
            else {
                relX *= innerRadius;
                relY *= innerRadius;
            }
            if (i == 0) {
                path.moveTo(centerX + relX, centerY + relY);
            }
            else {
                path.lineTo(centerX + relX, centerY + relY);
            }
        }
        path.closePath();
        return path;
    }
}
