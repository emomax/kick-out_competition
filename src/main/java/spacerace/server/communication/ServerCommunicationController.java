package spacerace.server.communication;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import spacerace.domain.Acceleration;
import spacerace.domain.GameState;
import spacerace.domain.GameStatus;
import spacerace.server.GameHandler;
import spacerace.server.SpaceRaceGame;
import spacerace.server.communication.response.GameStateConverter;
import spacerace.server.communication.response.ServerResponse;

public class ServerCommunicationController {

    private final GameHandler gameHandler = new GameHandler();

    public ServerResponse registerPlayer(final String gameName, final String playerName, final Integer levelNumber) {
        try {
            return registerNewPlayer(gameName, playerName, levelNumber);
        }
        catch (final Exception e) {
            return createErrorResponse(e);
        }
    }

    private ServerResponse registerNewPlayer(final String gameName, final String playerName, final int levelNumber) throws IOException {
        final SpaceRaceGame game = gameHandler.getOrCreateGame(gameName, levelNumber);

        final ServerResponse response = new ServerResponse();
        if (game.getGameStatus() != GameStatus.JOINABLE) {
            response.setErrorMessage("Game not joinable state");
            return response;
        }
        else {
            game.addShip(playerName);
        }
        return response;
    }

    private ServerResponse createErrorResponse(final Exception e) {
        final StringWriter stringWriter = new StringWriter();
        final PrintWriter  printWriter  = new PrintWriter(stringWriter);
        e.printStackTrace(printWriter);
        final String errorMessage = e.getMessage() + "\n" + stringWriter.toString();

        final ServerResponse response = new ServerResponse();
        response.setErrorMessage(errorMessage);
        return response;
    }

    public ServerResponse getGameState(final String gameName) {
        final SpaceRaceGame  game = gameHandler.getGame(gameName);
        final ServerResponse response;
        if (game == null) {
            response = createErrorResponse(new IllegalStateException("No game found. Maybe you did not start game in time?"));
        }
        else {
            final GameState gameState = GameStateConverter.convertGameToGameState(game);
            response = new ServerResponse();
            response.setGameState(gameState);
        }
        return response;
    }

    public ServerResponse action(
            final String gameName,
            final String playerName,
            final Acceleration accelerationX,
            final Acceleration accelerationY,
            final boolean stabilize) {
        final SpaceRaceGame game = gameHandler.getGame(gameName);
        game.updateShipParameters(playerName, accelerationX, accelerationY, stabilize);
        return new ServerResponse();
    }

    public ServerResponse startGame(final String gameName) {
        try {
            final SpaceRaceGame game = gameHandler.getGame(gameName);
            game.startGame();
        }
        catch (final Exception e) {
            return createErrorResponse(e);
        }
        return new ServerResponse();
    }

    public ServerResponse getGameResult(final String gameName) {
        try {
            final SpaceRaceGame  game     = gameHandler.getGame(gameName);
            final ServerResponse response = new ServerResponse();
            response.setPlayerResults(game.getPlayerPositions());
            return response;
        }
        catch (final Exception e) {
            return createErrorResponse(e);
        }
    }
}
