package spacerace.client;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class GameKeyAdapter extends KeyAdapter {

    private final RemoteGame remoteGame;
    private boolean startGameCommandSent = false;

    public GameKeyAdapter(final RemoteGame remoteGame) {
        this.remoteGame = remoteGame;
    }

    public void keyPressed(final KeyEvent event) {
        final int key = event.getKeyCode();

        if (key == KeyEvent.VK_SPACE && !startGameCommandSent) {
            remoteGame.sendStartCommand();
            startGameCommandSent = true;
        }
    }
}
