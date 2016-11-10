package treasurehunter.server;

import java.util.List;

import commons.gameengine.board.BoardState;
import commons.gameengine.board.PlayerColor;
import treasurehunter.GameResult;
import treasurehunter.board.TreasureHunterBoard;
import treasurehunter.domain.Move;

public class GameHandler {

    private final GameRepository gameRepository = new GameRepository();

    // Returns the board when it's your turn
    public TreasureHunterBoard getBoard(final String gameName, final String playerName) throws InterruptedException {
        final TreasureHunterGame game = gameRepository.getOrCreateGame(gameName, playerName);
        if (!game.getRedPlayerName().equals(playerName) && game.getYellowPlayerName() == null) {
            // It's the first time yellow plays, set name...
            game.setYellowPlayerName(playerName);
        }

        game.waitForMyTurn(playerName);
        game.getTimer().start(playerName);
        return game.getBoard();
    }

    public TreasureHunterGame getGame(final String gameName) {
        return gameRepository.getGame(gameName);
    }

    public TreasureHunterGame move(final String gameName, final String playerName, final Move move) {
        final TreasureHunterGame game = gameRepository.getGame(gameName);
        final TreasureHunterBoard board = game.getBoard();
        final PlayerColor playerColor = game.getPlayerColor(playerName);

        final int treasuresLeft = GameResult.getTreasuresLeft(board);

        game.getTimer().stop(playerName);
        board.movePlayer(playerColor, move);
        game.addBoardState(new BoardState<>(board.getCells()));

        if (treasuresLeft > GameResult.getTreasuresLeft(board)) {
            // Player picked up a treasure
            System.out.println("Picked a treasure!");
            game.playerCollected(playerColor);
        }

        game.finishMyTurn();
        return game;
    }

    public void killGame(final String gameName) {
        System.out.println("Game result: " +
                           "Yellow: " + gameRepository.getGame(gameName).getYellowPlayerTreasures() +
                           "Red: " + gameRepository.getGame(gameName).getRedPlayerTreasures());
        gameRepository.killGame(gameName);
    }

    public List<TreasureHunterGame> getFinishedGames() {
        return gameRepository.getFinishedGames();
    }
}
