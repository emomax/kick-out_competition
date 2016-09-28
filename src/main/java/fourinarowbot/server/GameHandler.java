package fourinarowbot.server;

import fourinarowbot.BoardSearcher;
import fourinarowbot.SearchResult;
import fourinarowbot.board.BoardImpl;
import fourinarowbot.domain.Coordinates;
import fourinarowbot.domain.Marker;
import fourinarowbot.domain.MarkerColor;

public class GameHandler {

    private final GameRepository gameRepository = new GameRepository();

    // Returns the board when it's your turn
    public BoardImpl getBoard(final String gameName, final String playerName) throws InterruptedException {
        final Game game = gameRepository.getOrCreateGame(gameName, playerName);
        if (!game.getRedPlayerName().equals(playerName) && game.getYellowPlayerName() == null) {
            // It's the first time yellow plays, set name...
            game.setYellowPlayerName(playerName);
        }
        game.waitForMyTurn(playerName);
        return game.getBoard();
    }

    public Game getGame(final String gameName) {
        return gameRepository.getGame(gameName);
    }

    public Game placeMarker(final String gameName, final String playerName, final Coordinates coordinates) {
        final Game          game          = gameRepository.getGame(gameName);
        final BoardImpl     board         = game.getBoard();
        final BoardSearcher boardSearcher = new BoardSearcher(board);

        final MarkerColor playerColor = game.getPlayerColor(playerName);
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
}
