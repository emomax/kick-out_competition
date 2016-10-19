package fourinarowbot;

import commons.Logger;
import commons.gameengine.board.Coordinate;
import commons.gameengine.board.PlayerColor;
import fourinarowbot.board.FourInARowbotBoard;
import fourinarowbot.domain.Marker;
import fourinarowbot.gameengine.FourInARowbotGameEngine;
import fourinarowbot.gameengine.MyN00bGameEngine;
import fourinarowbot.graphics.GraphicsEngine;

public class FourInARowApplication {

    final FourInARowbotGameEngine         redGameEngine;
    final FourInARowbotGameEngine         yellowGameEngine;
    final FourInARowbotBoard board;
    final GraphicsEngine     graphicsEngine;
    final BoardSearcher      boardSearcher;
    final boolean            graphicsEnabled;

    public FourInARowApplication(final FourInARowbotGameEngine redGameEngine) {
        this.redGameEngine = redGameEngine;
        this.yellowGameEngine = new MyN00bGameEngine();
        this.board = new FourInARowbotBoard();
        this.boardSearcher = new BoardSearcher(board);
        this.graphicsEngine = new GraphicsEngine(board);
        this.graphicsEnabled = false;
    }

    public FourInARowApplication(final FourInARowbotGameEngine redGameEngine, final boolean graphicsEnabled) {
        this.redGameEngine = redGameEngine;
        this.yellowGameEngine = new MyN00bGameEngine();
        //        this.yellowGameEngine = new ClosestGameEngine(PlayerColor.YELLOW);
        this.board = new FourInARowbotBoard();
        this.boardSearcher = new BoardSearcher(board);
        this.graphicsEnabled = graphicsEnabled;
        if (graphicsEnabled) {
            this.graphicsEngine = new GraphicsEngine(board);
        }
        else {
            this.graphicsEngine = null;
        }
    }

    public void runGameOnce() {
        final boolean      printBoardEveryRound = false;
        final SearchResult searchResult         = startGame(printBoardEveryRound);
        if (searchResult.isDraw()) {
            System.out.println("It's a draw!");
        }
        else {
            System.out.println("The winner is " + searchResult.getWinnerPlayerColor() + "!!!");
        }
        board.print();
    }

    public void runGameMultipleGames(final int numberOfGames) {
        final boolean printBoardEveryRound = false;
        int           draws                = 0;
        int           redWins              = 0;
        int           yellowWins           = 0;
        for (int i = 0; i < numberOfGames; i++) {
            final SearchResult searchResult = startGame(printBoardEveryRound);
            if (searchResult.isDraw()) {
                System.out.println(i + ". " + "Draw");
                draws++;
            }
            else {
                System.out.println(i + ". " + searchResult.getWinnerPlayerColor());
                if (searchResult.getWinnerPlayerColor() == PlayerColor.RED) {
                    redWins++;
                }
                else {
                    yellowWins++;
                }
            }
            board.reset();
        }
        System.out.println("\nResult:");
        System.out.println("   " + draws + " draws");
        System.out.println("   " + redWins + " red wins");
        System.out.println("   " + yellowWins + " yellow wins");
    }

    private SearchResult startGame(final boolean printBoardEveryRound) {
        boolean      isRedPlayerTurn = true;
        SearchResult searchResult    = SearchResult.resultWithoutWinner();
        while (!searchResult.isGameOver()) {
            try {
                playNextRound(isRedPlayerTurn);
            }
            catch (final NullPointerException exception) {
                throw new RuntimeException("Null is not a good place to be.", exception);
            }
            catch (final Exception e) {
                if (isRedPlayerTurn) {
                    System.out.println("Error when getting marker for red player\n");
                    e.printStackTrace();
                }
                else {
                    System.out.println("Error when getting marker for yellow player\n" + e);
                    e.printStackTrace();
                }
                return searchResult;
            }
            isRedPlayerTurn = !isRedPlayerTurn;

            // Print to console, for development
            if (printBoardEveryRound) {
                board.print();
            }

            // A nice pace =o)
            try {
                Thread.sleep(300);
            }
            catch (final InterruptedException e) {
                e.printStackTrace();
            }
            searchResult = boardSearcher.getGameStatus();
        }

        return searchResult;
    }

    private void playNextRound(final boolean isRedPlayerTurn) {
        final Marker nextMarkerToPlace = getNextMarkerToPlace(isRedPlayerTurn);

        board.placeMarker(nextMarkerToPlace);
        if (graphicsEnabled) {
            graphicsEngine.repaintGraphics(nextMarkerToPlace);
        }
    }

    private Marker getNextMarkerToPlace(final boolean isRedPlayerTurn) {
        if (isRedPlayerTurn) {
            final Coordinate coordinates = redGameEngine.getCoordinatesForNextMakerToPlace(board, PlayerColor.RED);
            return new Marker(PlayerColor.RED, coordinates);
        }
        else {
            final Coordinate coordinates = yellowGameEngine.getCoordinatesForNextMakerToPlace(board, PlayerColor.YELLOW);
            return new Marker(PlayerColor.YELLOW, coordinates);
        }
    }

    public static void main(final String[] args) throws InterruptedException {
        Logger.setDebugLogOn(false);
        final FourInARowApplication application = new FourInARowApplication(new MyN00bGameEngine());

        //        application.runGameOnce();
        application.runGameMultipleGames(100);
    }
}
