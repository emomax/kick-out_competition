package spacerace.server;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import spacerace.level.Level;

public class GameRepository {

    private final ConcurrentHashMap<String, SpaceRaceGame> games           = new ConcurrentHashMap<>();
    private final LevelRepository                          levelRepository = new LevelRepository();
    //    private final List<SpaceRaceGame> finishedGames;
    //    private final FinishedGamesDao                         finishedGamesDao = new FinishedGamesDao();

    //    public GameRepository() {
    //        finishedGames = finishedGamesDao.readGames();
    //    }

    public SpaceRaceGame getOrCreateGame(final String gameName, final String playerName, final int levelNumber) {
        synchronized (games) {
            final SpaceRaceGame foundGame = games.get(gameName);
            if (foundGame != null) {
                return foundGame;
            }
            else {
                final UUID          id    = UUID.randomUUID();
                final Level         level = levelRepository.getLevel(levelNumber);
                final SpaceRaceGame game  = new SpaceRaceGame(id, gameName, level);
                games.put(gameName, game);
                return game;
            }
        }
    }

    public SpaceRaceGame getGame(final String gameName) {
        return games.get(gameName);
    }
}
