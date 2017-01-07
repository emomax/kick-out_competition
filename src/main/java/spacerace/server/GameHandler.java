package spacerace.server;

public class GameHandler {

    private final GameRepository gameRepository = new GameRepository();

    public SpaceRaceGame getOrCreateGame(final String gameName, final String playerName, final int levelNumber) {
        return gameRepository.getOrCreateGame(gameName, playerName, levelNumber);
    }

    public SpaceRaceGame getGame(final String gameName) {
        return gameRepository.getGame(gameName);
    }
}
