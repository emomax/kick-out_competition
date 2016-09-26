package fourinarowbot.server;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class GameRepository {

    private final ConcurrentHashMap<String, Game> games = new ConcurrentHashMap<>();

    public Game getOrCreateGame(final String gameName, final String playerName) throws InterruptedException {
        synchronized (games) {
            final Game foundGame = games.get(gameName);
            if (foundGame != null) {
                if (!foundGame.isItMyTurn(playerName)) {
                    foundGame.waitForMyTurn(playerName);
                }
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
}
