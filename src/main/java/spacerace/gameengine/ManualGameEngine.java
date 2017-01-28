package spacerace.gameengine;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;

import spacerace.client.RemoteGame;
import spacerace.client.communication.LocalServerAdapter;
import spacerace.domain.Acceleration;
import spacerace.domain.Action;
import spacerace.domain.ShipState;

public class ManualGameEngine extends KeyAdapter implements SpaceRaceGameEngine {

    private volatile Acceleration accelerationX = Acceleration.NONE;
    private volatile Acceleration accelerationY = Acceleration.NONE;
    private volatile boolean      stabilize     = false;

    @Override
    public Action getAction(final ShipState shipState) {
        if (stabilize) {
            return Action.withStabilize(true);
        }
        else {
            return Action.withAcceleration(accelerationX, accelerationY);
        }
    }

    public void keyPressed(final KeyEvent event) {
        final int key = event.getKeyCode();

        if (key == KeyEvent.VK_SPACE) {
            stabilize = true;
        }
        else if (key == KeyEvent.VK_LEFT) {
            accelerationX = Acceleration.NEGATIVE;
        }
        else if (key == KeyEvent.VK_RIGHT) {
            accelerationX = Acceleration.POSITIVE;
        }
        else if (key == KeyEvent.VK_UP) {
            accelerationY = Acceleration.NEGATIVE;
        }
        else if (key == KeyEvent.VK_DOWN) {
            accelerationY = Acceleration.POSITIVE;
        }
    }

    public void keyReleased(final KeyEvent event) {
        final int key = event.getKeyCode();

        if (key == KeyEvent.VK_SPACE) {
            stabilize = false;
        }
        else if (key == KeyEvent.VK_LEFT || key == KeyEvent.VK_RIGHT) {
            accelerationX = Acceleration.NONE;
        }
        else if (key == KeyEvent.VK_UP || key == KeyEvent.VK_DOWN) {
            accelerationY = Acceleration.NONE;
        }
    }

    public static void main(final String[] args) throws IOException, InterruptedException {
        final String             playerName       = "Robocop";
        final String             gameName         = "Battle of Trustly";
        final int                levelNumber      = 2;
        final LocalServerAdapter server           = new LocalServerAdapter(playerName, gameName, levelNumber);
        final RemoteGame         remoteGame       = new RemoteGame(server, playerName, gameName, levelNumber);
        final ManualGameEngine   manualGameEngine = new ManualGameEngine();
        remoteGame.runGame(manualGameEngine, manualGameEngine);
    }
}
