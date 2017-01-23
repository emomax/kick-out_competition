package spacerace.graphics;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.io.IOException;

import spacerace.domain.GameState;
import spacerace.domain.GameStatus;
import spacerace.domain.Statistics;
import spacerace.level.Level;

public class SpaceRaceViewerPanel extends SpaceRaceGraphicsPanel {

    private final String gameName;

    SpaceRaceViewerPanel(final Level level, final GameState gameState, final Statistics gameCycleStatistics, final Statistics responseTimeStatistics, final String gameName) throws IOException {
        super(level, gameState, gameCycleStatistics, responseTimeStatistics);
        this.gameName = gameName;
    }

    @Override
    public void paint(final Graphics graphics) {
        updatePaintTimeStatistics();
        super.paint(graphics);

        paintLevelBaseLayer(graphics);

        if (gameState != null) {
            gameState.getShipStates()
                    .forEach(shipState -> drawShip(shipState, graphics));
        }

        paintLevelTopLayer(graphics);

        final GameStatus gameStatus = getGameStatus();
        if (gameStatus == null) {
            printSearchingForGameText(graphics);
        }
        else if (gameStatus == GameStatus.JOINABLE) {
            printWaitingText(graphics);
            drawInfoPanel(graphics);
        }
        else if (gameStatus == GameStatus.RUNNING) {
            drawInfoPanel(graphics);
        }
        else if (gameStatus == GameStatus.FINISHED) {
            printGameResult(graphics);
        }
        else if (gameStatus == GameStatus.CLOSED) {
            printJoinTimeIsUp(graphics);
        }

        Toolkit.getDefaultToolkit().sync();
        graphics.dispose();
    }

    private GameStatus getGameStatus() {
        if (gameState == null) {
            return null;
        }
        return GameStatus.valueOf(gameState.getGameStatus());
    }

    private void printSearchingForGameText(final Graphics graphics) {
        graphics.setColor(Color.YELLOW);

        final Font bigFont = new Font("Helvetica", Font.BOLD, 50);
        graphics.setFont(bigFont);
        graphics.drawString("Searching for game:", (level.getWidth() / 2) - 250, ((level.getHeight() / 2) - 75));

        graphics.setColor(Color.RED);
        graphics.drawString(gameName, (level.getWidth() / 2) - 175, ((level.getHeight() / 2) - 15));
    }
}
