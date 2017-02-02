package spacerace.graphics.panel;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javax.swing.JPanel;

import spacerace.domain.GameState;
import spacerace.domain.PlayerResult;
import spacerace.domain.Rectangle2D;
import spacerace.domain.ShipState;
import spacerace.domain.Statistics;
import spacerace.graphics.GraphicsUtils;
import spacerace.level.Level;

public abstract class GraphicsPanel extends JPanel {

    private static final int GRAPHICS_UPDATE_INTERVAL = 17;

    protected          Level              level;
    protected volatile GameState          gameState;
    private final      Statistics         gameCycleStatistics;
    private final      Statistics         responseTimeStatistics;
    private final      Statistics         graphicsPaintStatistics;
    private volatile   List<PlayerResult> playerResults;
    private volatile   long               lastPaintTime;

    GraphicsPanel(final Level level, final GameState gameState, final Statistics gameCycleStatistics, final Statistics responseTimeStatistics) throws IOException {
        this.level = level;
        this.gameState = gameState;
        this.gameCycleStatistics = gameCycleStatistics;
        this.responseTimeStatistics = responseTimeStatistics;
        this.graphicsPaintStatistics = new Statistics();

        setFocusable(true);
        setBackground(Color.black);
        setDoubleBuffered(true);

        startGraphicsUpdateLoop();
    }

    private void startGraphicsUpdateLoop() {
        final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(this::repaint, 0, GRAPHICS_UPDATE_INTERVAL, TimeUnit.MILLISECONDS);
    }

    public void setPlayerResults(final List<PlayerResult> playerResults) {
        this.playerResults = playerResults;
    }

    //    // ToDo: Must this be here?
    //    public void addNotify() {
    //        super.addNotify();
    //    }
    //
    //    // ToDo: Must this be here?
    //    @Override
    //    public void update(final Graphics g) {
    //        paint(g);
    //    }

    public void setState(final GameState gameState) {
        this.gameState = gameState;
    }

    void updatePaintTimeStatistics() {
        if (lastPaintTime != 0) {
            final Long timeSinceLastPaint = System.currentTimeMillis() - lastPaintTime;
            graphicsPaintStatistics.add(timeSinceLastPaint.intValue());
        }
        lastPaintTime = System.currentTimeMillis();
    }

    void paintLevelBaseLayer(final Graphics graphics) {
        level.paintBaseLayer((Graphics2D) graphics);
        level.getTrackBorders().forEach(line -> GraphicsUtils.drawLine(line, Color.WHITE, graphics));
        GraphicsUtils.drawLine(level.getGoalLine(), Color.YELLOW, graphics);
    }

    void paintLevelTopLayer(final Graphics graphics) {
        level.paintTopLayer((Graphics2D) graphics);
    }

    void drawShip(final ShipState shipState, final Graphics2D graphics) {
        level.getShipGraphics().paint(shipState, graphics);
        level.getRocketFireGraphics().paint(shipState, level.getShipGraphics(), graphics);
    }

    void drawInfoPanel(final Graphics graphics) {
        final DecimalFormat decimalFormat = new DecimalFormat("#0.0");
        final Font          font          = new Font("Helvetica", Font.BOLD, 14);

        // Draw statistics
        final IntSummaryStatistics gameCycleSummaryStatistics    = gameCycleStatistics.getStatistics();
        final IntSummaryStatistics responseTimeSummaryStatistics = responseTimeStatistics.getStatistics();
        final IntSummaryStatistics graphicsSummaryStatistics     = graphicsPaintStatistics.getStatistics();


        final String responseTimesText = "Server response time"
                                         + "  min: " + responseTimeSummaryStatistics.getMin()
                                         + "  max: " + responseTimeSummaryStatistics.getMax()
                                         + "  average: " + decimalFormat.format(responseTimeSummaryStatistics.getAverage());
        final String gameCycleTimesText = "Game cycle time"
                                          + "  min: " + gameCycleSummaryStatistics.getMin()
                                          + "  max: " + gameCycleSummaryStatistics.getMax()
                                          + "  average: " + decimalFormat.format(gameCycleSummaryStatistics.getAverage());
        final String fpsText = "FPS    "
                               + "  min: " + (1000 / gameCycleSummaryStatistics.getMax())
                               + "  max: " + (1000 / gameCycleSummaryStatistics.getMin())
                               + "  average: " + decimalFormat.format(1000 / gameCycleSummaryStatistics.getAverage());
        final String graphicsPaintTimesText = "Graphics FPS    "
                                              + "  min: " + (1000 / graphicsSummaryStatistics.getMax())
                                              + "  max: " + (1000 / graphicsSummaryStatistics.getMin())
                                              + "  average: " + decimalFormat.format(1000 / graphicsSummaryStatistics.getAverage());


        graphics.setColor(Color.YELLOW);
        graphics.setFont(font);
        graphics.drawString(responseTimesText, 400, 20);
        graphics.drawString(gameCycleTimesText, 400, 40);
        graphics.drawString(fpsText, 400, 60);
        graphics.drawString(graphicsPaintTimesText, 400, 80);

        // Draw time left
        String timeLeftText = "Time left:  ";
        if (gameState.getStartTime() == 0) {
            // Game not started yet
            timeLeftText += "-";
        }
        else {
            final long timeSinceGameStart = System.currentTimeMillis() - gameState.getStartTime();
            timeLeftText += ((int) (level.getTimeLimit() - timeSinceGameStart) / 1000);
        }
        graphics.setColor(Color.GREEN);
        graphics.setFont(font);
        graphics.drawString(timeLeftText, 800, 20);
    }

    void printWaitingText(final Graphics graphics) {
        printJoinableMessage(graphics);
        printJoinedPlayers(graphics);
    }

    private void printJoinableMessage(final Graphics graphics) {
        graphics.setColor(Color.YELLOW);

        final Font bigFont = new Font("Helvetica", Font.BOLD, 50);
        graphics.setFont(bigFont);
        graphics.drawString("Game is now joinable", (level.getWidth() / 2) - 250, ((level.getHeight() / 2) - 75));

        final Font smallFont = new Font("Helvetica", Font.BOLD, 30);
        graphics.setFont(smallFont);
        graphics.drawString("Press space to start game", (level.getWidth() / 2) - 180, (level.getHeight() / 2) - 35);
    }


    void printJoinTimeIsUp(final Graphics graphics) {
        graphics.setColor(Color.YELLOW);
        final Font bigFont = new Font("Helvetica", Font.BOLD, 50);
        graphics.setFont(bigFont);
        graphics.drawString("Join time timeout", (level.getWidth() / 2) - 250, (level.getHeight() / 2));
    }

    private void printJoinedPlayers(final Graphics graphics) {
        final Rectangle2D background      = new Rectangle2D((level.getWidth() / 2) - 300, (level.getHeight() / 2), 600, 250);
        final Rectangle2D backgroundFrame = new Rectangle2D(background.getX() - 10, background.getY() - 10, background.getWidth() + 20, background.getHeight() + 20);
        GraphicsUtils.drawRectangle(backgroundFrame, Color.CYAN, graphics);
        GraphicsUtils.drawRectangle(background, Color.BLACK, graphics);

        final Font font = new Font("Helvetica", Font.BOLD, 20);
        graphics.setFont(font);

        int x                  = background.getX() + 20;
        int y                  = background.getY() + 20;
        int numberOfShipsInRow = 0;
        for (final ShipState ship : gameState.getShipStates()) {
            final Rectangle2D shipColorRectangle = new Rectangle2D(x, y, 20, 20);
            GraphicsUtils.drawRectangle(shipColorRectangle, new Color(ship.getColorRGB()), graphics);
            graphics.drawString(ship.getName(), (shipColorRectangle.getX() + shipColorRectangle.getWidth()) + 5, shipColorRectangle.getY() + 15);

            numberOfShipsInRow++;
            if (numberOfShipsInRow == 3) {
                y = y + 30;
                x = background.getX() + 20;
                numberOfShipsInRow = 0;
            }
            else {
                x = x + 200;
            }
        }
    }

    void printGameResult(final Graphics graphics) {
        final Rectangle2D background      = new Rectangle2D((level.getWidth() / 2) - 200, (level.getHeight() / 2 - 250), 400, 500);
        final Rectangle2D backgroundFrame = new Rectangle2D(background.getX() - 10, background.getY() - 10, background.getWidth() + 20, background.getHeight() + 20);
        GraphicsUtils.drawRectangle(backgroundFrame, Color.GREEN, graphics);
        GraphicsUtils.drawRectangle(background, Color.BLACK, graphics);

        final Font font = new Font("Helvetica", Font.BOLD, 20);
        graphics.setFont(font);

        playerResults.sort((o1, o2) -> {
            if (o1.getBestFinishTime() == null) {
                return 1;
            }
            else if (o2.getBestFinishTime() == null) {
                return -1;
            }
            return o1.getBestFinishTime().compareTo(o2.getBestFinishTime());
        });

        final int x        = background.getX() + 60;
        int       y        = background.getY() + 40;
        int       position = 1;
        for (final PlayerResult playerResult : playerResults) {
            printPosition(graphics, x, y, position, playerResult);
            y = y + 30;
            position++;
        }
    }

    private void printPosition(final Graphics graphics, final int x, final int y, final int position, final PlayerResult playerResult) {
        // Print position and name
        final ShipState ship = getShipByName(playerResult.getName());
        graphics.setColor(new Color(ship.getColorRGB()));
        graphics.drawString(position + ".  " + ship.getName(), x, y);

        // Print time
        final String timeText;
        if (playerResult.getBestFinishTime() == null) {
            timeText = "-";
        }
        else {
            final DecimalFormat formatter = new DecimalFormat("#0.0000");
            timeText = formatter.format(playerResult.getBestFinishTime() / 1000d) + " s";
        }
        graphics.drawString(timeText, x + 180, y);
    }

    private ShipState getShipByName(final String name) {
        return gameState.getShipStates().stream()
                .filter(shipState -> shipState.getName().equals(name))
                .findAny()
                .get();
    }
}
