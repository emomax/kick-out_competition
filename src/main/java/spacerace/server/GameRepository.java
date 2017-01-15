package spacerace.server;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import spacerace.domain.GameStatus;
import spacerace.level.Level;

class GameRepository {

    public static final int MOVE_OLD_GAMES_PERIOD = 5;

    private final ConcurrentHashMap<String, SpaceRaceGame> games           = new ConcurrentHashMap<>();
    private final LevelRepository                          levelRepository = new LevelRepository();
    private final List<SpaceRaceGame>                      finishedGames   = Collections.synchronizedList(new ArrayList<>());

    GameRepository() {
        startOldGamesCleaner();
    }

    private void startOldGamesCleaner() {
        final Runnable moveOldGames = () -> {
            games.values().stream()
                    .filter(spaceRaceGame -> spaceRaceGame.getGameStatus() == GameStatus.CLOSED)
                    .forEach(game -> {
                        finishedGames.add(game);
                        games.remove(game.getName());
                    });
        };
        final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(moveOldGames, 0, MOVE_OLD_GAMES_PERIOD, TimeUnit.SECONDS);
    }

    SpaceRaceGame getOrCreateGame(final String gameName, final int levelNumber) {
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

    SpaceRaceGame getGame(final String gameName) {
        return games.get(gameName);
    }
}
