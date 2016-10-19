package fourinarowbot.graphics;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JComponent;

import commons.gameengine.board.PlayerColor;
import fourinarowbot.board.FourInARowbotBoard;
import fourinarowbot.domain.Marker;

class BoardGraphics extends JComponent {
    private static final int CELL_SIZE    = 80;
    private static final int BOARD_MARGIN = 15;

    private final FourInARowbotBoard board;
    private final List<Marker> redMarkers    = new ArrayList<>();
    private final List<Marker> yellowMarkers = new ArrayList<>();

    private final int boardWidth;
    private final int boardHeight;

    public BoardGraphics(final FourInARowbotBoard board) {
        this.board = board;
        boardWidth = board.getNumberOfCols() * CELL_SIZE + BOARD_MARGIN * 2;
        boardHeight = board.getNumberOfRows() * CELL_SIZE + BOARD_MARGIN * 2;

        this.setPreferredSize(new Dimension(boardWidth, boardHeight));
    }

    public void addNextMarkerToPaint(final Marker nextMarkerToPlace) {
        if (nextMarkerToPlace.getColor() == PlayerColor.RED) {
            redMarkers.add(nextMarkerToPlace);
        }
        else {
            yellowMarkers.add(nextMarkerToPlace);
        }
    }

    @Override
    public void paintComponent(final Graphics g) {
        final Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        paintBackground(g2);
        paintCircles(g2);
    }

    private void paintBackground(final Graphics2D g2) {
        g2.setColor(Color.BLUE);
        g2.fillRect(0, 0, boardWidth, boardHeight);
    }

    private void paintCircles(final Graphics2D g2) {
        final int circleMargin = (CELL_SIZE / 20);
        final int diameter     = CELL_SIZE - (2 * circleMargin);

        for (int col = 0; col < board.getNumberOfCols(); col++) {
            for (int row = 0; row < board.getNumberOfRows(); row++) {
                final int circleUpperLeftCornerX = (col * CELL_SIZE) + circleMargin + BOARD_MARGIN;
                final int circleUpperLeftCornerY = (row * CELL_SIZE) + circleMargin + BOARD_MARGIN;
                final int circleCenterX          = circleUpperLeftCornerX + diameter / 2;
                final int circleCenterY          = circleUpperLeftCornerY + diameter / 2;

                final Marker marker = board.get(col, row);
                if (marker != null) {
                    paintMarker(g2, diameter, circleUpperLeftCornerX, circleUpperLeftCornerY, marker);
                    paintMarkerNumber(g2, circleCenterX, circleCenterY, marker);
                }
                else {
                    paintHole(g2, diameter, circleUpperLeftCornerX, circleUpperLeftCornerY);
                }
            }
        }
    }

    private void paintMarker(final Graphics2D g2, final int diameter, final int circleUpperLeftCornerX,
            final int circleUpperLeftCornerY, final Marker marker) {
        final Color color = convertPlayerColorToAwt(marker.getColor());
        g2.setColor(color);
        g2.fillOval(circleUpperLeftCornerX, circleUpperLeftCornerY, diameter, diameter);
    }

    private void paintMarkerNumber(final Graphics2D g2, final int circleCenterX, final int circleCenterY, final Marker marker) {
        if (marker.getColor() == PlayerColor.RED) {
            g2.setColor(Color.WHITE);
            final String markerNumber = String.valueOf(redMarkers.indexOf(marker) + 1);
            g2.drawString(String.valueOf(markerNumber), circleCenterX, circleCenterY);
        }
        else {
            g2.setColor(Color.BLACK);
            final String markerNumber = String.valueOf(yellowMarkers.indexOf(marker) + 1);
            g2.drawString(String.valueOf(markerNumber), circleCenterX, circleCenterY);
        }
    }

    private Color convertPlayerColorToAwt(final PlayerColor markerColor) {
        if (markerColor == PlayerColor.RED) {
            return Color.RED;
        }
        else {
            return Color.YELLOW;
        }
    }

    private void paintHole(final Graphics2D g2, final int diameter, final int circleUpperLeftCornerX, final int circleUpperLeftCornerY) {
        g2.setColor(Color.WHITE);
        g2.fillOval(circleUpperLeftCornerX, circleUpperLeftCornerY, diameter, diameter);
    }
}
