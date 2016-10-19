package fourinarowbot.gameengine;

import commons.gameengine.board.Coordinate;
import commons.gameengine.board.PlayerColor;
import fourinarowbot.FourInARowApplication;
import fourinarowbot.board.FourInARowbotBoard;

public class MyN00bGameEngine extends FourInARowbotGameEngine {

    @Override
    public Coordinate getCoordinatesForNextMakerToPlace(final FourInARowbotBoard board, final PlayerColor myColor) {
        // TODO This is where your bad-ass SkyNet 2.0 logic goes.

        for (int i = 0; i < 7; i++) {
            for (int j = 5; j >= 0; j--) {
                if (!board.isAnyMarkerAt(i, j)) {
                    return new Coordinate(i, j);
                }
            }
        }

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
