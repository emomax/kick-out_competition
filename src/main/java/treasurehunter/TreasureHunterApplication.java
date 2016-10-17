package treasurehunter;

import commons.Logger;
import commons.board.Board;
import commons.gameengine.GameEngine;
import commons.board.PlayerColor;
import treasurehunter.board.TreasureHunterBoard;
import treasurehunter.domain.TreasureHunterAction;
import treasurehunter.gameengine.MyN00bTreasureHunterGameEngine;
import treasurehunter.gameengine.SmartRandomTreasureHunterGameEngine;

public class TreasureHunterApplication {
    private final GameEngine     redGameEngine;
    private final GameEngine     yellowGameEngine;
    private final Board          board;
    private final boolean        graphicsEnabled;

    public TreasureHunterApplication(final GameEngine redGameEngine) {
        this(redGameEngine, false);
    }

    public TreasureHunterApplication(final GameEngine redGameEngine, final boolean graphicsEnabled) {
        this.redGameEngine = redGameEngine;
        this.yellowGameEngine = new SmartRandomTreasureHunterGameEngine();
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
        System.out.println("Running game, once!");
        final boolean printBoardEveryRound = true;
        final GameResult searchResult         = startGame(printBoardEveryRound);

        searchResult.printScore();
        System.out.println("The winner is: " + searchResult.getWinnerColor());
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
        while (!gameResult.noMoreTreasures()) {
            try {
                playNextRound(isRedPlayerTurn);
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
                gameResult.printScore();
                board.print();
            }

            // A nice pace =o)
            try {
                Thread.sleep(60L);
            }
            catch (final InterruptedException e) {
                e.printStackTrace();
            }
            gameResult = gameResult.getGameStatus();
        }

        return gameResult;
    }

    private void playNextRound(final boolean isRedPlayerTurn) {
        TreasureHunterAction nextMoveToMake = getNextMoveToMake(isRedPlayerTurn);
        PlayerColor          currentPlayer  = isRedPlayerTurn ? PlayerColor.RED : PlayerColor.YELLOW;

        ((TreasureHunterBoard) board).movePlayer(currentPlayer, nextMoveToMake);
    }

    private TreasureHunterAction getNextMoveToMake(boolean isRedPlayerTurn) {
        if (isRedPlayerTurn) {
            return (TreasureHunterAction) redGameEngine.getNextMove(board, PlayerColor.RED);
        }
        else {
            return (TreasureHunterAction) yellowGameEngine.getNextMove(board, PlayerColor.YELLOW);
        }
    }

    public static void main(final String[] args) throws InterruptedException {
        Logger.setDebugLogOn(false);
        final TreasureHunterApplication application = new TreasureHunterApplication(new MyN00bTreasureHunterGameEngine());

        //        application.runGameOnce();
        //application.runGameMultipleGames(100);
    }
}
