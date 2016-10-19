package fourinarowbot.server;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class GameRepository {

    private final ConcurrentHashMap<String, FourInARowbotGame> games = new ConcurrentHashMap<>();
    private final List<FourInARowbotGame> finishedGames;
    private final GameDao gameDao = new GameDao();

    public GameRepository() {
        finishedGames = gameDao.readGames();
    }

    public FourInARowbotGame getOrCreateGame(final String gameName, final String playerName) throws InterruptedException {
        synchronized (games) {
            final FourInARowbotGame foundGame = games.get(gameName);
            if (foundGame != null) {
                return foundGame;
            }
            else {
                final UUID              id   = UUID.randomUUID();
                final FourInARowbotGame game = new FourInARowbotGame(id, gameName, playerName);
                games.put(game.getName(), game);
                return game;
            }
        }
    }

    public FourInARowbotGame getGame(final String gameName) {
        return games.get(gameName);
    }

    public void killGame(final String gameName) {
        synchronized (games) {
            final FourInARowbotGame game = games.get(gameName);
            finishedGames.add(game);
            games.remove(gameName);
            gameDao.writeGames(finishedGames);
        }
    }

    public List<FourInARowbotGame> getFinishedGames() {
        return finishedGames;
    }
}
