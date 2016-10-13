package fourinarowbot;

import commons.Logger;
import commons.gameengine.Coordinate;
import fourinarowbot.board.BoardGameBoard;
import fourinarowbot.board.FourInARowbotBoard;
import fourinarowbot.domain.Marker;
import fourinarowbot.domain.MarkerColor;

public class BoardSearcher {

    private final BoardGameBoard board;

    public BoardSearcher(final BoardGameBoard board) {
        this.board = board;
    }

    public SearchResult getGameStatus() {
        final SearchResult searchResult = searchForWinner();
        if (searchResult.isWinnerFound()) {
            return searchResult;
        }
        else if (isBoardFull()) {
            return SearchResult.drawResult();
        }
        else {
            return SearchResult.resultWithoutWinner();
        }
    }

    private boolean isBoardFull() {
        for (int row = 0; row < board.getNumberOfRows(); row++) {
            for (int col = 0; col < board.getNumberOfCols(); col++) {
                if (board.getMarker(col, row) == null) {
                    return false;
                }
            }
        }
        return true;
    }

    public SearchResult searchForWinner() {
        final SearchResult hohrizontalResult = searchHorizontalForWinner();
        if (hohrizontalResult.isWinnerFound()) {
            Logger.logDebug("Found horizontal winner");
            return hohrizontalResult;
        }

        final SearchResult verticalResult = searchVerticalForWinner();
        if (verticalResult.isWinnerFound()) {
            Logger.logDebug("Found vertical winner");
            return verticalResult;
        }

        final SearchResult leftToRightDiagonalResult = searchForLeftToRightDiagonalWinner();
        if (leftToRightDiagonalResult.isWinnerFound()) {
            return leftToRightDiagonalResult;
        }

        return searchForRightToLeftDiagonalWinner();
    }

    private SearchResult searchHorizontalForWinner() {
        for (int row = 0; row < board.getNumberOfRows(); row++) {
            int redInARow    = 0;
            int yellowInARow = 0;
            for (int col = 0; col < board.getNumberOfCols(); col++) {
                final Marker marker = board.getMarker(col, row);
                if (marker == null) {
                    redInARow = 0;
                    yellowInARow = 0;
                    continue;
                }
                if (marker.getColor() == MarkerColor.RED) {
                    redInARow++;
                    yellowInARow = 0;
                }
                else {
                    yellowInARow++;
                    redInARow = 0;
                }
                if (redInARow == 4) {
                    return SearchResult.resultWithWinner(MarkerColor.RED);
                }
                else if (yellowInARow == 4) {
                    return SearchResult.resultWithWinner(MarkerColor.YELLOW);
                }
            }
        }
        return SearchResult.resultWithoutWinner();
    }

    private SearchResult searchVerticalForWinner() {
        for (int col = 0; col < board.getNumberOfCols(); col++) {
            int redInARow    = 0;
            int yellowInARow = 0;
            for (int row = 0; row < board.getNumberOfRows(); row++) {
                final Marker marker = board.getMarker(col, row);
                if (marker == null) {
                    redInARow = 0;
                    yellowInARow = 0;
                    continue;
                }
                if (marker.getColor() == MarkerColor.RED) {
                    redInARow++;
                    yellowInARow = 0;
                }
                else {
                    yellowInARow++;
                    redInARow = 0;
                }
                if (redInARow == 4) {
                    return SearchResult.resultWithWinner(MarkerColor.RED);
                }
                else if (yellowInARow == 4) {
                    return SearchResult.resultWithWinner(MarkerColor.YELLOW);
                }
            }
        }
        return SearchResult.resultWithoutWinner();
    }

    private SearchResult searchForLeftToRightDiagonalWinner() {
        // Search starting at all positions at left border
        for (int startRow = 0; startRow < board.getNumberOfRows(); startRow++) {
            final SearchResult searchResult = searchLeftToRightDiagonallyStartingAt(startRow, 0);
            if (searchResult.isWinnerFound()) {
                Logger.logDebug("Found winner when searching diagonally left to right starting at left side");
                return searchResult;
            }
        }

        // Search starting at all positions at top border
        for (int startCol = 0; startCol < board.getNumberOfCols(); startCol++) {
            final SearchResult searchResult = searchLeftToRightDiagonallyStartingAt(0, startCol);
            if (searchResult.isWinnerFound()) {
                Logger.logDebug("Found winner when searching diagonally left to right starting at top side");
                return searchResult;
            }
        }

        return SearchResult.resultWithoutWinner();
    }

    private SearchResult searchLeftToRightDiagonallyStartingAt(final int startRow, final int startCol) {
        int redInARow    = 0;
        int yellowInARow = 0;
        int row          = startRow;

        for (int col = startCol; col < board.getNumberOfCols(); col++) {
            if (board.isOutsideBoard(col, row)) {
                break;
            }
            final Marker marker = board.getMarker(col, row);
            if (marker == null) {
                redInARow = 0;
                yellowInARow = 0;
                row++;
                continue;
            }
            if (marker.getColor() == MarkerColor.RED) {
                redInARow++;
                yellowInARow = 0;
            }
            else {
                yellowInARow++;
                redInARow = 0;
            }
            if (redInARow == 4) {
                return SearchResult.resultWithWinner(MarkerColor.RED);
            }
            else if (yellowInARow == 4) {
                return SearchResult.resultWithWinner(MarkerColor.YELLOW);
            }

            row++;
        }
        return SearchResult.resultWithoutWinner();
    }

    private SearchResult searchForRightToLeftDiagonalWinner() {
        // Search starting at all positions at right border
        for (int startRow = 0; startRow < board.getNumberOfRows(); startRow++) {
            final SearchResult searchResult = searchRightToLeftDiagonallyStartingAt(startRow, 6);
            if (searchResult.isWinnerFound()) {
                Logger.logDebug("Found winner when searching diagonally right to left starting at right side");
                return searchResult;
            }
        }

        // Search starting at all positions at top border
        for (int startCol = 0; startCol < board.getNumberOfCols(); startCol++) {
            final SearchResult searchResult = searchRightToLeftDiagonallyStartingAt(0, startCol);
            if (searchResult.isWinnerFound()) {
                Logger.logDebug("Found winner when searching diagonally right to left starting at top side");
                return searchResult;
            }
        }

        return SearchResult.resultWithoutWinner();
    }

    private SearchResult searchRightToLeftDiagonallyStartingAt(final int startRow, final int startCol) {
        int redInARow    = 0;
        int yellowInARow = 0;
        int row          = startRow;

        for (int col = startCol; col >= 0; col--) {
            if (board.isOutsideBoard(col, row)) {
                break;
            }
            final Marker marker = board.getMarker(col, row);
            if (marker == null) {
                redInARow = 0;
                yellowInARow = 0;
                row++;
                continue;
            }
            if (marker.getColor() == MarkerColor.RED) {
                redInARow++;
                yellowInARow = 0;
            }
            else {
                yellowInARow++;
                redInARow = 0;
            }
            if (redInARow == 4) {
                System.out.println("" + row + " " + col);
                return SearchResult.resultWithWinner(MarkerColor.RED);
            }
            else if (yellowInARow == 4) {
                return SearchResult.resultWithWinner(MarkerColor.YELLOW);
            }

            row++;
        }
        return SearchResult.resultWithoutWinner();
    }

    public static void main(final String[] args) {
        final FourInARowbotBoard board         = new FourInARowbotBoard();
        final BoardSearcher      boardSearcher = new BoardSearcher(board);

        // Find left -> righr diagonal when starting from "left side"
        //        fourinarowbot.board.placeMarker(new Marker(MarkerColor.RED, new Coordinate(0, 0)));
        //        fourinarowbot.board.placeMarker(new Marker(MarkerColor.RED, new Coordinate(1, 2)));
        //        fourinarowbot.board.placeMarker(new Marker(MarkerColor.RED, new Coordinate(2, 3)));
        //        fourinarowbot.board.placeMarker(new Marker(MarkerColor.RED, new Coordinate(3, 4)));
        //        fourinarowbot.board.placeMarker(new Marker(MarkerColor.RED, new Coordinate(4, 5)));
        //        System.out.println("Is winner found: " + boardSearcher.searchForLeftToRightDiagonalWinner().isWinnerFound());

        // Find left -> right when starting from "top side"
        //        fourinarowbot.board.placeMarker(new Marker(MarkerColor.RED, new Coordinate(3, 2)));
        //        fourinarowbot.board.placeMarker(new Marker(MarkerColor.RED, new Coordinate(4, 3)));
        //        fourinarowbot.board.placeMarker(new Marker(MarkerColor.RED, new Coordinate(5, 4)));
        //        fourinarowbot.board.placeMarker(new Marker(MarkerColor.RED, new Coordinate(6, 5)));
        //        System.out.println("Is winner found: " + boardSearcher.searchForLeftToRightDiagonalWinner().isWinnerFound());

        // Find right -> left diagonal when starting from "right side"
        //        fourinarowbot.board.placeMarker(new Marker(MarkerColor.RED, new Coordinate(6, 1)));
        //        fourinarowbot.board.placeMarker(new Marker(MarkerColor.RED, new Coordinate(5, 2)));
        //        fourinarowbot.board.placeMarker(new Marker(MarkerColor.RED, new Coordinate(4, 3)));
        //        fourinarowbot.board.placeMarker(new Marker(MarkerColor.RED, new Coordinate(3, 4)));
        //        System.out.println("Is winner found: " + boardSearcher.searchForRightToLeftDiagonalWinner().isWinnerFound());

        // Find right -> left when starting from "top side"
        board.placeMarker(new Marker(MarkerColor.RED, new Coordinate(3, 1)));
        board.placeMarker(new Marker(MarkerColor.RED, new Coordinate(2, 2)));
        board.placeMarker(new Marker(MarkerColor.RED, new Coordinate(1, 3)));
        board.placeMarker(new Marker(MarkerColor.RED, new Coordinate(0, 4)));
        System.out.println("Is winner found: " + boardSearcher.searchForRightToLeftDiagonalWinner().isWinnerFound());
        board.print();
    }
}
