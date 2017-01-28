package spacerace.gameengine;

import java.io.IOException;

import spacerace.client.RemoteGame;
import spacerace.client.communication.LocalServerAdapter;
import spacerace.domain.Acceleration;
import spacerace.domain.Action;
import spacerace.domain.ShipState;

public class MyN00bGameEngine implements SpaceRaceGameEngine {

    @Override
    public Action getAction(final ShipState shipState) {
        // Insert awesome code here
        return Action.withAcceleration(Acceleration.NONE, Acceleration.NONE);
    }

    public static void main(final String[] args) throws IOException, InterruptedException {
        final String              playerName   = "Robocop";
        final String              gameName     = "Battle of Trustly";
        final int                 levelNumber  = 2;
        final LocalServerAdapter  server       = new LocalServerAdapter(playerName, gameName, levelNumber);
        final RemoteGame          remoteGame   = new RemoteGame(server, playerName, gameName, levelNumber);
        final SpaceRaceGameEngine myGameEngine = new MyN00bGameEngine();
        remoteGame.runGame(myGameEngine);
    }
}
