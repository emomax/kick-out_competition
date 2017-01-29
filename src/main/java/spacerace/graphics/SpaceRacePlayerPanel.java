package spacerace.graphics;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;

import spacerace.domain.Detector;
import spacerace.domain.GameState;
import spacerace.domain.GameStatus;
import spacerace.domain.ShipState;
import spacerace.domain.Statistics;
import spacerace.level.Level;

public class SpaceRacePlayerPanel extends SpaceRaceGraphicsPanel {

    private volatile List<Detector> detectors;
    private final    String         playerName;

    SpaceRacePlayerPanel(final Level level, final List<KeyListener> keyListeners, final GameState gameState, final Statistics gameCycleStatistics, final Statistics responseTimeStatistics, final String playerName) throws IOException {
        super(level, gameState, gameCycleStatistics, responseTimeStatistics);
        this.playerName = playerName;
        keyListeners.forEach(this::addKeyListener);
    }

    public void setDetectors(final List<Detector> detectors) {
        this.detectors = detectors;
    }

    @Override
    public void paint(final Graphics graphics) {
        updatePaintTimeStatistics();
        super.paint(graphics);

        paintLevelBaseLayer(graphics);

        gameState.getShipStates()
                .forEach(shipState -> drawShip(shipState, (Graphics2D) graphics));

        paintLevelTopLayer(graphics);

        final GameStatus gameStatus = GameStatus.valueOf(gameState.getGameStatus());
        if (gameStatus == GameStatus.JOINABLE) {
            printWaitingText(graphics);
            drawInfoPanel(graphics);
            drawShipInfo(graphics);
        }
        else if (gameStatus == GameStatus.RUNNING) {
            drawDetectors(graphics);
            drawInfoPanel(graphics);
            drawShipInfo(graphics);
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

    private void drawDetectors(final Graphics graphics) {
        if (!getMyShip().isPassedGoal()) {
            detectors.stream()
                    .filter(detector -> detector.getBeam() != null)
                    .forEach(beam -> GraphicsUtils.drawLine(beam.getBeam(), Color.RED, graphics));
        }
    }

    private void drawShipInfo(final Graphics graphics) {
        final ShipState     myShip        = getMyShip();
        final DecimalFormat decimalFormat = new DecimalFormat("#0.0");
        final Font          font          = new Font("Helvetica", Font.BOLD, 14);

        // Draw ship info
        final String speedXText = decimalFormat.format(myShip.getSpeed().getX() * 1000);
        final String speedYText = decimalFormat.format(myShip.getSpeed().getY() * 1000);
        final String speedText  = "Speed: [" + speedXText + ", " + speedYText + "]";

        final String accelerationXText = String.valueOf((int) myShip.getAccelerationDirection().getX());
        final String accelerationYText = String.valueOf((int) myShip.getAccelerationDirection().getY());
        final String accelerationText  = "Acceleration: [" + accelerationXText + ", " + accelerationYText + "]";

        final String text = speedText + "   " + accelerationText;
        graphics.setColor(Color.YELLOW);
        graphics.setFont(font);
        graphics.drawString(text, 5, 20);
    }

    private ShipState getMyShip() {
        return gameState.getShipStates().stream()
                .filter(shipState -> shipState.getName().equals(playerName))
                .findAny()
                .get();
    }
}
