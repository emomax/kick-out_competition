package treasurehunter;

import commons.Logger;
import commons.board.Board;
import commons.gameengine.GameEngine;
import treasurehunter.board.PlayerColor;
import treasurehunter.board.TreasureHunterBoard;
import treasurehunter.gameengine.TreasureHunterGameEngine;

/**
 * Created by maxjonsson on 2016-10-13.
 */
public class TreasureHunterApplication {
    final GameEngine     redGameEngine;
    final GameEngine     yellowGameEngine;
    final Board          board;
    final boolean        graphicsEnabled;

    public TreasureHunterApplication(final GameEngine redGameEngine) {
        this.redGameEngine = redGameEngine;
        this.yellowGameEngine = new TreasureHunterGameEngine();
        this.board = new TreasureHunterBoard();
        this.graphicsEnabled = false;
    }

    public TreasureHunterApplication(final GameEngine redGameEngine, final boolean graphicsEnabled) {
        this.redGameEngine = redGameEngine;
        this.yellowGameEngine = new TreasureHunterGameEngine();
        //        this.yellowGameEngine = new ClosestGameEngine(MarkerColor.YELLOW);
        this.board = new TreasureHunterBoard();
        this.graphicsEnabled = graphicsEnabled;
        if (graphicsEnabled) {
            // TODO add graphics
        }
        else {
            // TODO omit graphics
        }
    }

    public void runGameOnce() {
        final boolean printBoardEveryRound = false;
        int           draws                = 0;
        int           redWins              = 0;
        int           yellowWins           = 0;
        final GameResult searchResult         = startGame(printBoardEveryRound);

        board.print();
    }

    public void runGameMultipleGames(final int numberOfGames) {
        final boolean printBoardEveryRound = false;
        int           draws                = 0;
        int           redWins              = 0;
        int           yellowWins           = 0;
        for (int i = 0; i < numberOfGames; i++) {
            final GameResult searchResult = startGame(printBoardEveryRound);
            if (searchResult.isDraw()) {
                System.out.println(i + ". " + "Draw");
                draws++;
            }
            else {
                System.out.println(i + ". " + searchResult.getWinnerColor());
                if (searchResult.getWinnerColor() == PlayerColor.RED) {
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

    private GameResult startGame(final boolean printBoardEveryRound) {
        boolean      isRedPlayerTurn = true;
        GameResult gameResult = GameResult.ResultWithoutWinner();
        while (!gameResult.isGameOver()) {
            try {
                playNextRound(isRedPlayerTurn);
            }
            catch (final NullPointerException exception) {
                throw new RuntimeException("Null is not a good place to be.", exception);
            }
            catch (final Exception e) {
                if (isRedPlayerTurn) {
                    System.out.println("Error when getting action for red player\n");
                    e.printStackTrace();
                }
                else {
                    System.out.println("Error when getting action for yellow player\n" + e);
                    e.printStackTrace();
                }
                return gameResult;
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
            gameResult = gameResult.getGameStatus();
        }

        return gameResult;
    }

    private void playNextRound(final boolean isRedPlayerTurn) {
        // TODO implement
    }

    public static void main(final String[] args) throws InterruptedException {
        Logger.setDebugLogOn(false);
        final TreasureHunterApplication application = new TreasureHunterApplication(new TreasureHunterGameEngine());
        //        application.runGameOnce();
        application.runGameMultipleGames(100);
    }
}
