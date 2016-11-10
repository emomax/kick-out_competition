package treasurehunter.server;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import treasurehunter.GameResult;

public class GameRepository {

    private final ConcurrentHashMap<String, TreasureHunterGame> games = new ConcurrentHashMap<>();
    private final List<TreasureHunterGame> finishedGames;
    private final GameDao gameDao = new GameDao();

    public GameRepository() {
        finishedGames = gameDao.readGames();
    }

    public TreasureHunterGame getOrCreateGame(final String gameName, final String playerName) throws InterruptedException {
        synchronized (games) {
            final TreasureHunterGame foundGame = games.get(gameName);
            if (foundGame != null) {
                return foundGame;
            }
            else {
                final UUID              id   = UUID.randomUUID();
                final TreasureHunterGame game = new TreasureHunterGame(id, gameName, playerName);
                games.put(game.getName(), game);

                GameResult.resetTurns();
                return game;
            }
        }
    }

    public TreasureHunterGame getGame(final String gameName) {
        return games.get(gameName);
    }

    public void killGame(final String gameName) {
        synchronized (games) {
            final TreasureHunterGame game = games.get(gameName);
            finishedGames.add(game);
            games.remove(gameName);
            gameDao.writeGames(finishedGames);
        }
    }

    public List<TreasureHunterGame> getFinishedGames() {
        return finishedGames;
    }
}
