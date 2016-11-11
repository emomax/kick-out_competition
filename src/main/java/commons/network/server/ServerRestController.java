package commons.network.server;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import commons.gameengine.board.Coordinate;
import commons.gameengine.board.BoardState;
import commons.network.server.Response.ServerResponseBase;
import fourinarowbot.board.FourInARowbotBoard;
import fourinarowbot.domain.Marker;
import fourinarowbot.server.FourInARowbotGame;
import fourinarowbot.server.GameHandler;
import fourinarowbot.server.response.GameSummaryResponse;
import fourinarowbot.server.response.ServerResponse;
import treasurehunter.board.Tile;
import treasurehunter.board.TreasureHunterBoard;
import treasurehunter.domain.Move;
import treasurehunter.server.TreasureHunterGame;

@RestController
public class ServerRestController {

    private final GameHandler gameHandler = new GameHandler();
    private final treasurehunter.server.GameHandler treasureHunterGameHandler = new treasurehunter.server.GameHandler();

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
        final FourInARowbotBoard board      = gameHandler.getBoard(gameName, playerName);
        final BoardState<Marker> boardState = new BoardState<>(board.getBoard());
        final FourInARowbotGame  game       = gameHandler.getGame(gameName);
        final ServerResponse     response   = new ServerResponse();
        response.setRedPlayerName(game.getRedPlayerName());
        response.setYellowPlayerName(game.getYellowPlayerName());

        if (game.isGameOver()) {
            response.setMessage("TreasureHunterGame over!");
            game.updateGameStatisticsWithGameTime();
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
        System.out.println("/placeMarker");
        final Coordinate coordinates = new Coordinate(x, y);
        try {
            final FourInARowbotGame game = gameHandler.placeMarker(gameName, playerName, coordinates);
            if (game.isGameOver()) {
                final ServerResponse response = new ServerResponse();
                response.setMessage("TreasureHunterGame over!");
                game.updateGameStatisticsWithGameTime();
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
            final FourInARowbotGame game       = gameHandler.getGame(gameName);
            final BoardState<Marker>        boardState = new BoardState<>(game.getBoard().getBoard());
            gameHandler.killGame(gameName);
            final ServerResponse response = new ServerResponse();
            response.setBoardState(boardState);
            response.setMessage("Error! " + e.getMessage());
            return response;
        }
    }

    @CrossOrigin
    @RequestMapping("/gameSummary")
    public List<GameSummaryResponse> getGameSummaries() {
        System.out.println("/gameSummary");
        final List<FourInARowbotGame>   games     = gameHandler.getFinishedGames();
        final List<GameSummaryResponse> summaries = new ArrayList<>();
        for (final FourInARowbotGame game : games) {
            final GameSummaryResponse response = new GameSummaryResponse();
            response.setUuid(game.getId());
            response.setGameName(game.getName());
            response.setRedPlayerName(game.getRedPlayerName());
            response.setYellowPlayerName(game.getYellowPlayerName());
            response.setDraws(game.getGameStatistics().getDraws());
            response.setRedWins(game.getGameStatistics().getRedWins());
            response.setYellowWins(game.getGameStatistics().getYellowWins());
            response.setBoardStates(game.getGameStatistics().getBoardStates());
            response.setRedPlayerGameTime(game.getGameStatistics().getRedPlayerGameTime());
            response.setYellowPlayerGameTime(game.getGameStatistics().getYellowPlayerGameTime());
            response.setGameStartDate(game.getGameStartTime().toString());
            summaries.add(response);
        }
        return summaries;
    }

    @RequestMapping("/getTreasureHunterBoard")
    public ServerResponseBase getTreasureHunterBoardState(
            @RequestParam(value = "playerName") final String playerName,
            @RequestParam(value = "gameName") final String gameName) {
        try {
            return getTreasureHunterBoardStateResponse(playerName, gameName);
        }
        catch (final Exception e) {
            treasureHunterGameHandler.killGame(gameName);
            final treasurehunter.server.response.ServerResponse response = new treasurehunter.server.response.ServerResponse();
            response.setMessage("Error! " + e.getMessage());
            return response;
        }
    }

    private ServerResponseBase getTreasureHunterBoardStateResponse(final @RequestParam(value = "playerName") String playerName, final @RequestParam(value = "gameName") String gameName) throws InterruptedException {
        final TreasureHunterBoard                           board      = treasureHunterGameHandler.getBoard(gameName, playerName);
        final BoardState<Tile>                              boardState = new BoardState<>(board.getCells());
        final TreasureHunterGame                            game       = treasureHunterGameHandler.getGame(gameName);
        final treasurehunter.server.response.ServerResponse response   = new treasurehunter.server.response.ServerResponse();
        response.setRedPlayerName(game.getRedPlayerName());
        response.setYellowPlayerName(game.getYellowPlayerName());

        if (game.isGameOver()) {
            response.setMessage("TreasureHunterGame over!");
            game.updateGameStatisticsWithGameTime();
            //response.setGameStatistics(game.getGameStatistics());
            treasureHunterGameHandler.killGame(gameName);
            return response;
        }
        else {
            response.setBoardState(boardState);
            return response;
        }
    }

    @RequestMapping("/move")
    public ServerResponseBase move(
            @RequestParam(value = "playerName") final String playerName,
            @RequestParam(value = "gameName") final String gameName,
            @RequestParam(value = "move") final String responseMove) {
        final Move move = Move.valueOf(responseMove);
        try {
            final TreasureHunterGame game = treasureHunterGameHandler.move(gameName, playerName, move);
            game.incrementTurns();

            if (game.isGameOver()) {
                final treasurehunter.server.response.ServerResponse response = new treasurehunter.server.response.ServerResponse();
                response.setMessage("TreasureHunterGame over!");
                game.updateGameStatisticsWithGameTime();
                //response.setGameStatistics(game.getGameStatistics());
                response.setRedPlayerName(game.getRedPlayerName());
                response.setYellowPlayerName(game.getYellowPlayerName());
                return response;
            }
            else {
                return new treasurehunter.server.response.ServerResponse();
            }
        }
        catch (final Exception e) {
            final TreasureHunterGame game       = treasureHunterGameHandler.getGame(gameName);
            final BoardState<Tile> boardState = new BoardState<>(game.getBoard().getCells());
            treasureHunterGameHandler.killGame(gameName);
            final treasurehunter.server.response.ServerResponse response = new treasurehunter.server.response.ServerResponse();
            response.setBoardState(boardState);
            response.setMessage("Error! " + e.getMessage());
            return response;
        }
    }

    @CrossOrigin
    @RequestMapping("/treasureHunterGameSummary")
    public List<treasurehunter.server.response.GameSummaryResponse> getTreasureHunterGameSummaries() {
        System.out.println("/treasureHunterGameSummary");
        final List<TreasureHunterGame>  games     = treasureHunterGameHandler.getFinishedGames();
        final List<treasurehunter.server.response.GameSummaryResponse> summaries = new ArrayList<>();
        for (final TreasureHunterGame game : games) {
            final treasurehunter.server.response.GameSummaryResponse response = new treasurehunter.server.response.GameSummaryResponse();
            response.setUuid(game.getId());
            response.setGameName(game.getName());
            response.setRedPlayerName(game.getRedPlayerName());
            response.setYellowPlayerName(game.getYellowPlayerName());

            response.setInitialBoardState(game.getInitialBoardStateAsString());
            response.setBoardStateUpdates(game.getBoardUpdatesAsStrings());

            response.setRedPlayerGameTime(game.getRedPlayerGameTime());
            response.setYellowPlayerGameTime(game.getYellowPlayerGameTime());

            response.setTotalTreasures(game.getNumberOfTreasures());
            response.setYellowPlayerTreasures(game.getYellowPlayerTreasures());
            response.setRedPlayerTreasures(game.getRedPlayerTreasures());

            //response.setPlayerMoves(game.getGameStatistics().getPlayerMoves());
            response.setRedPlayerGameTime(game.getRedPlayerGameTime());
            response.setYellowPlayerGameTime(game.getYellowPlayerGameTime());
            response.setGameStartDate(game.getGameStartTime().toString());
            summaries.add(response);
        }
        return summaries;
    }
}
