package fourinarowbot.server;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class GameRepository {

    private final ConcurrentHashMap<String, Game> games         = new ConcurrentHashMap<>();
    private final List<Game>                      finishedGames = new ArrayList<>();

    public Game getOrCreateGame(final String gameName, final String playerName) throws InterruptedException {
        synchronized (games) {
            final Game foundGame = games.get(gameName);
            if (foundGame != null) {
                return foundGame;
            }
            else {
                final UUID id   = UUID.randomUUID();
                final Game game = new Game(id, gameName, playerName);
                games.put(game.getName(), game);
                return game;
            }
        }
    }

    public Game getGame(final String gameName) {
        return games.get(gameName);
    }

    public void killGame(final String gameName) {
        synchronized (games) {
            final Game game = games.get(gameName);
            finishedGames.add(game);
            games.remove(gameName);
        }
    }

    public List<Game> getFinishedGames() {
        return finishedGames;
    }
}
