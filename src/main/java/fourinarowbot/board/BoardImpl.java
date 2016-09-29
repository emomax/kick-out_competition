package fourinarowbot.board;

import java.io.Serializable;

import fourinarowbot.domain.Coordinates;
import fourinarowbot.domain.Marker;
import fourinarowbot.domain.MarkerColor;

public class BoardImpl implements Board, Serializable {

    private static final int NUMBER_OF_ROWS = 6;
    private static final int NUMBER_OF_COLS = 7;

    private Marker[][] board;

    public Marker[][] getBoard() {
        return board;
    }

    public BoardImpl() {
        this.board = new Marker[NUMBER_OF_COLS][NUMBER_OF_ROWS];
    }

    public BoardImpl(final Marker[][] board) {
        this.board = board;
    }

    public int getNumberOfRows() {
        return NUMBER_OF_ROWS;
    }

    public int getNumberOfCols() {
        return NUMBER_OF_COLS;
    }

    public Marker getMarker(final int x, final int y) {
        verifyCoordinatesInsideBoard(x, y);
        return board[x][y];
    }

    public boolean isAnyMarkerAt(final int x, final int y) {
        verifyCoordinatesInsideBoard(x, y);
        return getMarker(x, y) != null;
    }

    private void verifyCoordinatesInsideBoard(final int x, final int y) {
        if (isOutsideBoard(x, y)) {
            throw new IllegalArgumentException("Coordinates outside fourinarow.board: [" + x + ", " + y + "]");
        }
    }

    public void placeMarker(final Marker marker) {
        final int x = marker.getCoordinates().getX();
        final int y = marker.getCoordinates().getY();
        if (isAnyMarkerAt(x, y)) {
            throw new IllegalArgumentException("Already marker in place at: " + marker.getCoordinates());
        }
        else if (!isBottomRow(y) && noMarkerBelow(x, y)) {
            throw new IllegalArgumentException("Marker must be placed on bottom row or on other marker: " + marker.getCoordinates());
        }
        else {
            board[x][y] = marker;
        }
    }

    private boolean isBottomRow(final int y) {
        return y == getNumberOfRows() - 1;
    }

    private boolean noMarkerBelow(final int x, final int y) {
        return !isAnyMarkerAt(x, y + 1);
    }

    public boolean isOutsideBoard(final int x, final int y) {
        if (x < 0 || x > (NUMBER_OF_COLS - 1)) {
            return true;
        }
        else if (y < 0 || y > (NUMBER_OF_ROWS - 1)) {
            return true;
        }
        return false;
    }

    public void reset() {
        board = new Marker[NUMBER_OF_COLS][NUMBER_OF_ROWS];
    }

    public void print() {
        System.out.print((char) 27 + "[34;43m");

        for (int row = 0; row < NUMBER_OF_ROWS; row++) {
            String result = "";
            for (int col = 0; col < NUMBER_OF_COLS; col++) {
                final Marker marker = board[col][row];
                if (marker == null) {
                    result += "O";
                }
                else if (marker.getColor() == MarkerColor.RED) {
                    result += "R";
                }
                else {
                    result += "Y";
                }
                result += "  ";
            }
            System.out.println(result);
        }
        System.out.println("");
    }

    public static void main(final String[] args) {
        final BoardImpl board = new BoardImpl();
        board.placeMarker(new Marker(MarkerColor.RED, new Coordinates(3, 5)));
        board.print();
    }
}
