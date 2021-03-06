package fourinarowbot.server;

import java.util.List;

import commons.gameengine.board.PlayerColor;
import commons.network.server.Game;
import fourinarowbot.BoardSearcher;
import fourinarowbot.SearchResult;
import fourinarowbot.board.FourInARowbotBoard;
import commons.gameengine.board.Coordinate;
import fourinarowbot.domain.Marker;

public class GameHandler {

    private final GameRepository gameRepository = new GameRepository();

    // Returns the board when it's your turn
    public FourInARowbotBoard getBoard(final String gameName, final String playerName) throws InterruptedException {
        final Game game = gameRepository.getOrCreateGame(gameName, playerName);
        if (!game.getRedPlayerName().equals(playerName) && game.getYellowPlayerName() == null) {
            // It's the first time yellow plays, set name...
            game.setYellowPlayerName(playerName);
        }
        game.waitForMyTurn(playerName);
        game.getTimer().start(playerName);
        return (FourInARowbotBoard) game.getBoard();
    }

    public FourInARowbotGame getGame(final String gameName) {
        return gameRepository.getGame(gameName);
    }

    public FourInARowbotGame placeMarker(final String gameName, final String playerName, final Coordinate coordinates) {
        final FourInARowbotGame game = gameRepository.getGame(gameName);
        game.getTimer().stop(playerName);
        final FourInARowbotBoard board         = game.getBoard();
        final BoardSearcher      boardSearcher = new BoardSearcher(board);

        final PlayerColor playerColor = game.getPlayerColor(playerName);
        final Marker      marker      = new Marker(playerColor, coordinates);
        board.placeMarker(marker);
        final SearchResult gameStatusAfterPlacing = boardSearcher.getGameStatus();
        if (gameStatusAfterPlacing.isGameOver()) {
            game.setRoundOver(gameStatusAfterPlacing, board);
        }

        game.finishMyTurn();
        return game;
    }

    public void killGame(final String gameName) {
        gameRepository.killGame(gameName);
    }

    public List<FourInARowbotGame> getFinishedGames() {
        return gameRepository.getFinishedGames();
    }
}
