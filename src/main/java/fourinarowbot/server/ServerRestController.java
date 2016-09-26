package fourinarowbot.server;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import fourinarowbot.SearchResult;
import fourinarowbot.board.BoardImpl;
import fourinarowbot.board.BoardState;
import fourinarowbot.domain.Coordinates;
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
            return GetBoardResponse.responseWithBoardState(boardState);
        }
        catch (final Exception e) {
            return GetBoardResponse.responseWithMessage("Error! " + e.getMessage() + e.getStackTrace());
        }
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
                return createGameOverResponse(searchResult);
            }
            else {
                return PlaceMarkerResponse.emptyResponse();
            }
        }
        catch (final Exception e) {
            return PlaceMarkerResponse.responseWithMessage("Error! " + e.getMessage() + e.getStackTrace());
        }
    }

    private PlaceMarkerResponse createGameOverResponse(final SearchResult searchResult) {
        if (searchResult.isDraw()) {
            System.out.println("DRAW!");
            return PlaceMarkerResponse.responseWithMessage("It's a draw!");
        }
        System.out.println("WINNER: " + searchResult.getWinnerMarkerColor());
        return PlaceMarkerResponse.responseWithMessage(searchResult.getWinnerMarkerColor() + " wins!");
    }

    @RequestMapping("/test")
    public String test() {
        return "Hej";
    }
}
