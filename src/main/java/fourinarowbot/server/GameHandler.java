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
        if (game.getYellowPlayerName() == null) {
            // It's the first time yellow plays, set name...
            game.setYellowPlayerName(playerName);
        }
        game.waitForMyTurn(playerName);
        return game.getBoard();
    }

    public SearchResult placeMarker(final String gameName, final String playerName, final Coordinates coordinates) {
        final Game          game          = gameRepository.getGame(gameName);
        final BoardImpl     board         = game.getBoard();
        final BoardSearcher boardSearcher = new BoardSearcher(board);
        final SearchResult  gameStatus    = boardSearcher.getGameStatus();

        if (gameStatus.isGameOver()) {
            game.finishMyTurn();
            return gameStatus;
        }
        else {
            final MarkerColor playerColor = game.getPlayerColor(playerName);
            final Marker      marker      = new Marker(playerColor, coordinates);
            board.placeMarker(marker);
            final SearchResult gameStatusAfterPlacing = boardSearcher.getGameStatus();
            game.finishMyTurn();
            return gameStatusAfterPlacing;
        }
    }
}
