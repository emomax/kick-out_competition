package fourinarowbot.server;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import fourinarowbot.board.BoardImpl;
import fourinarowbot.board.BoardState;
import fourinarowbot.domain.Coordinates;
import fourinarowbot.server.response.GameStatistics;
import fourinarowbot.server.response.ServerResponse;

@RestController
public class ServerRestController {

    private final GameHandler gameHandler = new GameHandler();

    @RequestMapping("/getBoard")
    public ServerResponse getBoardState(
            @RequestParam(value = "playerName") final String playerName,
            @RequestParam(value = "gameName") final String gameName) {
        try {
            return getBoardStateResponse(playerName, gameName);
        }
        catch (final Exception e) {
            gameHandler.killGame(gameName);
            final ServerResponse response = new ServerResponse();
            response.setMessage("Error! " + e.getMessage());
            return response;
        }
    }

    private ServerResponse getBoardStateResponse(final @RequestParam(value = "playerName") String playerName, final @RequestParam(value = "gameName") String gameName) throws InterruptedException {
        final BoardImpl      board      = gameHandler.getBoard(gameName, playerName);
        final BoardState     boardState = new BoardState(board.getBoard());
        final Game           game       = gameHandler.getGame(gameName);
        final ServerResponse response   = new ServerResponse();
        response.setRedPlayerName(game.getRedPlayerName());
        response.setYellowPlayerName(game.getYellowPlayerName());

        if (game.isGameOver()) {
            response.setMessage("Game over!");
            response.setGameStatistics(game.getGameStatistics());
            gameHandler.killGame(gameName);
            return response;
        }
        else {
            response.setBoardState(boardState);
            return response;
        }
    }

    @RequestMapping("/placeMarker")
    public ServerResponse placeMarker(
            @RequestParam(value = "playerName") final String playerName,
            @RequestParam(value = "gameName") final String gameName,
            @RequestParam(value = "x") final Integer x,
            @RequestParam(value = "y") final Integer y) {
        final Coordinates coordinates = new Coordinates(x, y);
        try {
            final Game game = gameHandler.placeMarker(gameName, playerName, coordinates);
            if (game.isGameOver()) {
                final ServerResponse response = new ServerResponse();
                response.setMessage("Game over!");
                response.setGameStatistics(game.getGameStatistics());
                response.setRedPlayerName(game.getRedPlayerName());
                response.setYellowPlayerName(game.getYellowPlayerName());
                return response;
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

    @RequestMapping("/test")
    public GameStatistics test() {
        return new GameStatistics();
    }
}
