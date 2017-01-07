package spacerace.server;

public class GameCycle implements Runnable {

    private static final int GAME_CYCLE_SLEEP_TIME = 17;

    private final SpaceRaceGame game;

    public GameCycle(final SpaceRaceGame game) {
        this.game = game;
    }

    public void run() {
        long beforeTime         = System.currentTimeMillis();
        long lastSpriteMovement = System.currentTimeMillis();

        while (true) {
            final long timeSinceLastSpriteMovement = System.currentTimeMillis() - lastSpriteMovement;
            animationCycle(timeSinceLastSpriteMovement);
            lastSpriteMovement = System.currentTimeMillis();

            final long timeDiff  = System.currentTimeMillis() - beforeTime;
            final long sleepTime = GAME_CYCLE_SLEEP_TIME - timeDiff;

            if (sleepTime > 0) {
                sleep(sleepTime);
            }

            beforeTime = System.currentTimeMillis();
        }
    }

    private void sleep(final long sleepTime) {
        try {
            Thread.sleep(sleepTime);
        }
        catch (final InterruptedException e) {
            System.out.println("Interrupted!");
        }
    }


    private void animationCycle(final long timeSinceLastCycle) {
        game.getShips().stream().forEach(ship -> ship.move(timeSinceLastCycle));
    }
}
