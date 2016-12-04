package treasurehunter;

import commons.Logger;
import commons.gameengine.board.PlayerColor;
import treasurehunter.board.TreasureHunterBoard;
import treasurehunter.domain.Move;
import treasurehunter.gameengine.MyN00bTreasureHunterGameEngine;
import treasurehunter.gameengine.SmartRandomTreasureHunterGameEngine;
import treasurehunter.gameengine.TreasureHunterGameEngine;

public class TreasureHunterApplication {
    private final TreasureHunterGameEngine redGameEngine;
    private final TreasureHunterGameEngine yellowGameEngine;
    private TreasureHunterBoard board;

    public TreasureHunterApplication(final TreasureHunterGameEngine redGameEngine) {
        this.redGameEngine = redGameEngine;
        this.yellowGameEngine = new SmartRandomTreasureHunterGameEngine();
    }

    public void runGameOnce() {
        System.out.println("Running game, once!");
        final boolean printBoardEveryRound = true;
        final GameResult searchResult         = startGame(printBoardEveryRound);

        searchResult.printScore();
        System.out.println("The winner is: " + searchResult.getWinnerPlayerColor());
    }

    private GameResult startGame(final boolean printBoardEveryRound) {
        boolean      isRedPlayerTurn = true;
        GameResult gameResult = GameResult.ResultWithoutWinner();

        board = new TreasureHunterBoard();
        GameResult.resetTurns();
        GameResult.resetTreasureCount();

        while (!gameResult.isGameOver(board)) {
            try {
                GameResult.incrementTurns();
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

                try {
                    Thread.sleep(60L);
                }
                catch (final InterruptedException e) {
                    e.printStackTrace();
                }
            }

            gameResult = gameResult.getGameStatus();
        }

        return gameResult;
    }

    private void playNextRound(final boolean isRedPlayerTurn) {
        Move        nextMoveToMake = getNextMoveToMake(isRedPlayerTurn);
        PlayerColor currentPlayer  = isRedPlayerTurn ? PlayerColor.RED : PlayerColor.YELLOW;

        board.movePlayer(currentPlayer, nextMoveToMake);
    }

    private Move getNextMoveToMake(boolean isRedPlayerTurn) {
        if (isRedPlayerTurn) {
            return redGameEngine.getNextMove(board, PlayerColor.RED).get();
        }
        else {
            return yellowGameEngine.getNextMove(board, PlayerColor.YELLOW).get();
        }
    }

    public void runGameMultipleTimes(int numberOfGames) {
        int yellowWins = 0;
        int redWins = 0;
        int draws = 0;

        for (int i = 0; i < numberOfGames; i++) {
            final GameResult searchResult = startGame(false);

            if (searchResult.isDraw()) {
                draws++;
            }
            else if (searchResult.getWinnerPlayerColor() == PlayerColor.RED) {
                redWins++;
            }
            else if (searchResult.getWinnerPlayerColor() == PlayerColor.YELLOW){
                yellowWins++;
            }
        }

        System.out.println("Final score (games won):\nRED: " + redWins + "\nYELLOW: " + yellowWins + "\nDRAWS:" + draws);
    }

    public static void main(final String[] args) throws InterruptedException {
        Logger.setDebugLogOn(false);
        final TreasureHunterApplication application = new TreasureHunterApplication(new MyN00bTreasureHunterGameEngine());

        application.runGameOnce();
    }
}
