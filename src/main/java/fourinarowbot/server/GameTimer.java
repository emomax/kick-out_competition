package fourinarowbot.server;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class GameTimer implements Serializable {

    private final Map<String, Long> totalGameTimes = new HashMap<>();
    private final Map<String, Long> lastStartTimes = new HashMap<>();

    public void start(final String playerName) {
        lastStartTimes.put(playerName, System.currentTimeMillis());
    }

    public void stop(final String playerName) {
        final long duration      = System.currentTimeMillis() - lastStartTimes.get(playerName);
        final Long totalGameTime = duration + totalGameTimes.getOrDefault(playerName, 0l);
        totalGameTimes.put(playerName, totalGameTime);
    }

    public long getGameTime(final String playerName) {
        return totalGameTimes.getOrDefault(playerName, 0l);
    }
}
