package fourinarowbot.server;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import fourinarowbot.BoardSearcher;
import fourinarowbot.SearchResult;
import fourinarowbot.board.BoardImpl;
import fourinarowbot.board.BoardState;
import fourinarowbot.domain.Coordinates;
import fourinarowbot.domain.MarkerColor;
import fourinarowbot.server.response.ServerResponse;

@RestController
public class ServerRestController {

    private final GameHandler gameHandler = new GameHandler();

    @RequestMapping("/getBoard")
    public ServerResponse getBoardState(
            @RequestParam(value = "playerName") final String playerName,
            @RequestParam(value = "gameName") final String gameName) {
        try {
            final BoardImpl  board      = gameHandler.getBoard(gameName, playerName);
            final BoardState boardState = new BoardState(board.getBoard());

            final BoardSearcher boardSearcher = new BoardSearcher(board);
            final SearchResult  searchResult  = boardSearcher.searchForWinner();
            if (searchResult.isGameOver()) {
                final ServerResponse loserResponse = createLoserResponse(searchResult, gameName, boardState);
                gameHandler.killGame(gameName);
                return loserResponse;
            }
            else {
                final ServerResponse response = new ServerResponse();
                response.setBoardState(boardState);
                return response;
            }
        }
        catch (final Exception e) {
            gameHandler.killGame(gameName);
            final ServerResponse response = new ServerResponse();
            response.setMessage("Error! " + e.getMessage());
            return response;
        }
    }

    private ServerResponse createLoserResponse(final SearchResult searchResult, final String gameName, final BoardState boardState) {
        final ServerResponse response = new ServerResponse();
        response.setBoardState(boardState);

        if (searchResult.isDraw()) {
            response.setMessage("It's a draw!");
        }
        else {
            final Game   game = gameHandler.getGame(gameName);
            final String otherPlayerName;
            if (searchResult.getWinnerMarkerColor().equals(MarkerColor.RED)) {
                otherPlayerName = game.getRedPlayerName();
            }
            else {
                otherPlayerName = game.getYellowPlayerName();
            }
            response.setMessage(otherPlayerName + " (" + searchResult.getWinnerMarkerColor() + ")" + " wins!");
        }
        return response;
    }

    @RequestMapping("/placeMarker")
    public ServerResponse placeMarker(
            @RequestParam(value = "playerName") final String playerName,
            @RequestParam(value = "gameName") final String gameName,
            @RequestParam(value = "x") final Integer x,
            @RequestParam(value = "y") final Integer y) {
        final Coordinates coordinates = new Coordinates(x, y);
        try {
            final SearchResult searchResult = gameHandler.placeMarker(gameName, playerName, coordinates);
            if (searchResult.isGameOver()) {
                return createWinnerResponse(playerName, searchResult, gameName);
            }
            else {
                return new ServerResponse();
            }
        }
        catch (final Exception e) {
            final Game       game       = gameHandler.getGame(gameName);
            final BoardState boardState = new BoardState(game.getBoard().getBoard());
            gameHandler.killGame(gameName);
            final ServerResponse response = new ServerResponse();
            response.setBoardState(boardState);
            response.setMessage("Error! " + e.getMessage());
            return response;
        }
    }

    private ServerResponse createWinnerResponse(final String playerName, final SearchResult searchResult, final String gameName) {
        final Game           game       = gameHandler.getGame(gameName);
        final BoardState     boardState = new BoardState(game.getBoard().getBoard());
        final ServerResponse response   = new ServerResponse();
        response.setBoardState(boardState);
        if (searchResult.isDraw()) {
            response.setMessage("It's a draw!");
        }
        else {
            response.setMessage(playerName + " (" + searchResult.getWinnerMarkerColor() + ")" + " wins!");
        }
        return response;
    }

    @RequestMapping("/test")
    public String test() {
        return "Hej";
    }
}
