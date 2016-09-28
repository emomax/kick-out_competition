package fourinarowbot.server.response;

import java.util.concurrent.atomic.AtomicInteger;

public class GameStatistics {

    private final AtomicInteger draws          = new AtomicInteger();
    private final AtomicInteger redWins        = new AtomicInteger();
    private final AtomicInteger yellowWins     = new AtomicInteger();
    private final AtomicInteger yellowTimeouts = new AtomicInteger();
    private final AtomicInteger redTimeouts    = new AtomicInteger();

    public int getDraws() {
        return draws.get();
    }

    public void incrementDraws() {
        draws.incrementAndGet();
    }

    public int getRedWins() {
        return redWins.get();
    }

    public void incrementRedWins() {
        redWins.incrementAndGet();
    }

    public int getYellowWins() {
        return yellowWins.get();
    }

    public void incrementYellowWins() {
        yellowWins.incrementAndGet();
    }

    public int getYellowTimeouts() {
        return yellowTimeouts.get();
    }

    public void incrementYellowTimeouts() {
        yellowTimeouts.incrementAndGet();
    }

    public int getRedTimeouts() {
        return redTimeouts.get();
    }

    public void incrementRedTimeouts() {
        redTimeouts.incrementAndGet();
    }
}
