package fourinarowbot.gameengine;

import fourinarowbot.FourInARowApplication;
import fourinarowbot.board.Board;
import fourinarowbot.domain.Coordinates;

public class MyN00bGameEngine implements GameEngine {

    @Override
    public Coordinates getCoordinatesForNextMakerToPlace(final Board board) {
        // TODO This is where your bad-ass SkyNet 2.0 logic goes.

        return null;
    }


    // Run this main to start the game
    public static void main(final String[] args) {
        final FourInARowApplication fourInARowApplication = new FourInARowApplication(new MyN00bGameEngine(), true);

        // Run game once
        fourInARowApplication.runGameOnce();

        // Run game multiple times
        //        fourInARowApplication.runGameMultipleGames(100);
    }
}
