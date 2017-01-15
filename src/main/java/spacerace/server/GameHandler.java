package spacerace.server;

public class GameHandler {

    private final GameRepository gameRepository = new GameRepository();

    public SpaceRaceGame getOrCreateGame(final String gameName, final int levelNumber) {
        return gameRepository.getOrCreateGame(gameName, levelNumber);
    }

    public SpaceRaceGame getGame(final String gameName) {
        return gameRepository.getGame(gameName);
    }
}
