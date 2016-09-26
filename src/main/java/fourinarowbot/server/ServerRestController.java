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
import fourinarowbot.server.response.GetBoardResponse;
import fourinarowbot.server.response.PlaceMarkerResponse;

@RestController
public class ServerRestController {

    private final GameHandler gameHandler = new GameHandler();

    @RequestMapping("/getBoard")
    public GetBoardResponse getBoardState(
            @RequestParam(value = "playerName") final String playerName,
            @RequestParam(value = "gameName") final String gameName) {
        try {
            final BoardImpl  board      = gameHandler.getBoard(gameName, playerName);
            final BoardState boardState = new BoardState(board.getBoard());

            final BoardSearcher boardSearcher = new BoardSearcher(board);
            final SearchResult  searchResult  = boardSearcher.searchForWinner();
            if (searchResult.isGameOver()) {
                final GetBoardResponse loserResponse = createLoserResponse(searchResult, gameName, boardState);
                gameHandler.killGame(gameName);
                return loserResponse;
            }

            return GetBoardResponse.responseWithBoardState(boardState);
        }
        catch (final Exception e) {
            gameHandler.killGame(gameName);
            return GetBoardResponse.responseWithMessage("Error! " + e.getMessage() + e.getStackTrace());
        }
    }

    private GetBoardResponse createLoserResponse(final SearchResult searchResult, final String gameName, final BoardState boardState) {
        if (searchResult.isDraw()) {
            return GetBoardResponse.responseWithBoardStateAndMessage(boardState, "It's a draw!");
        }
        final Game   game = gameHandler.getGame(gameName);
        final String otherPlayerName;
        if (searchResult.getWinnerMarkerColor().equals(MarkerColor.RED)) {
            otherPlayerName = game.getRedPlayerName();
        }
        else {
            otherPlayerName = game.getYellowPlayerName();
        }
        return GetBoardResponse.responseWithBoardStateAndMessage(boardState, otherPlayerName + " (" + searchResult.getWinnerMarkerColor() + ")" + " wins!");
    }

    @RequestMapping("/placeMarker")
    public PlaceMarkerResponse placeMarker(
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
                return PlaceMarkerResponse.emptyResponse();
            }
        }
        catch (final Exception e) {
            final Game       game       = gameHandler.getGame(gameName);
            final BoardState boardState = new BoardState(game.getBoard().getBoard());
            gameHandler.killGame(gameName);
            return PlaceMarkerResponse.responseWithMessage("Error! " + e.getMessage() + e.getStackTrace(), boardState);
        }
    }

    private PlaceMarkerResponse createWinnerResponse(final String playerName, final SearchResult searchResult, final String gameName) {
        final Game       game       = gameHandler.getGame(gameName);
        final BoardState boardState = new BoardState(game.getBoard().getBoard());
        if (searchResult.isDraw()) {
            return PlaceMarkerResponse.responseWithMessage("It's a draw!", boardState);
        }
        return PlaceMarkerResponse.responseWithMessage(playerName + " (" + searchResult.getWinnerMarkerColor() + ")" + " wins!", boardState);
    }

    @RequestMapping("/test")
    public String test() {
        return "Hej";
    }
}
