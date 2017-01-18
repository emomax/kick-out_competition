package spacerace.graphics;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.event.KeyListener;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

import spacerace.domain.Detector;
import spacerace.domain.GameState;
import spacerace.domain.GameStatistics;
import spacerace.domain.GameStatus;
import spacerace.domain.Line2D;
import spacerace.domain.PlayerResult;
import spacerace.domain.Rectangle2D;
import spacerace.domain.ShipState;
import spacerace.level.Level;

import static spacerace.server.SpaceRaceGame.GAME_TIME_LIMIT;

class SpaceRaceGraphicsPanel extends JPanel implements SpaceRaceGraphics {

    private static final String SHIP_IMAGE_DIR           = "../../spacerace/ship2.png";
    private static final String FIRE_IMAGE_DIR           = "../../spacerace/fire_50px.png";
    public static final  int    GRAPHICS_UPDATE_INTERVAL = 17;

    private final Level              level;
    private final BufferedImage      shipImage;
    private final BufferedImage      fireImage;
    private       GameState          gameState;
    private       List<Detector>     detectors;
    private final GameStatistics     gameStatistics;
    private final String             playerName;
    private       List<PlayerResult> playerResults;

    SpaceRaceGraphicsPanel(final Level level, final List<KeyListener> keyListeners, final GameState gameState, final GameStatistics gameStatistics, final String playerName) throws IOException {
        this.level = level;
        this.gameState = gameState;
        this.gameStatistics = gameStatistics;
        this.playerName = playerName;

        keyListeners.forEach(this::addKeyListener);
        setFocusable(true);
        setBackground(Color.black);
        setDoubleBuffered(true);

        this.shipImage = ImageIO.read(new File(getClass().getResource(SHIP_IMAGE_DIR).getFile()));
        this.fireImage = ImageIO.read(new File(getClass().getResource(FIRE_IMAGE_DIR).getFile()));

        startGraphicsUpdateLoop();
    }

    private void startGraphicsUpdateLoop() {
        final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(this::repaint, 0, GRAPHICS_UPDATE_INTERVAL, TimeUnit.MILLISECONDS);
    }

    public Dimension getShipImageDimension() {
        return new Dimension(shipImage.getWidth(), shipImage.getHeight());
    }

    @Override
    public void setPlayerResults(final List<PlayerResult> playerResults) {
        this.playerResults = playerResults;
    }

    // ToDo: Must this be here?
    public void addNotify() {
        super.addNotify();
    }

    // ToDo: Must this be here?
    @Override
    public void update(final Graphics g) {
        paint(g);
    }

    @Override
    public void setState(final GameState gameState, final List<Detector> detectors) {
        this.gameState = gameState;
        this.detectors = detectors;
    }

    @Override
    public void paint(final Graphics graphics) {
        super.paint(graphics);

        drawLevel(graphics);

        for (final ShipState shipState : gameState.getShipStates()) {
            drawShip(shipState, graphics);
        }

        final GameStatus gameStatus = GameStatus.valueOf(gameState.getGameStatus());
        if (gameStatus == GameStatus.JOINABLE) {
            printWaitingText(graphics);
            drawInfoPanel(graphics);
        }
        else if (gameStatus == GameStatus.RUNNING) {
            drawDetectors(graphics);
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

    private void drawLevel(final Graphics graphics) {
        final GradientPaint gradientPaint = new GradientPaint(25, 25, Color.BLUE, 15, 25, Color.BLACK, true);
        level.getRectangles().forEach(rectangle -> drawRectangle(rectangle, gradientPaint, graphics));
        drawGoal(graphics);
    }

    private void drawGoal(final Graphics graphics) {
        final GradientPaint gradientPaint = new GradientPaint(25, 25, Color.WHITE, 15, 25, Color.BLACK, true);
        drawRectangle(level.getGoal(), gradientPaint, graphics);
    }

    private void drawShip(final ShipState shipState, final Graphics graphics) {
        drawRectangle(new Rectangle2D((int) shipState.getPosition().getX(), (int) shipState.getPosition().getY(), shipImage.getWidth(), shipImage.getHeight()), new Color(shipState.getColorRGB()), graphics);
        graphics.drawImage(shipImage, (int) shipState.getPosition().getX(), (int) shipState.getPosition().getY(), this);
        drawRocketFire(shipState, graphics);
    }

    private void drawRocketFire(final ShipState shipState, final Graphics graphics) {
        if (shipState.getAccelerationDirection().getY() < 0 || shipState.isStabilize()) {
            graphics.drawImage(fireImage, (int) shipState.getPosition().getX(), (int) shipState.getPosition().getY() + shipImage.getHeight(), this);
        }
        if (shipState.getAccelerationDirection().getY() > 0 || shipState.isStabilize()) {
            final BufferedImage rotatedImage = rotateImage(fireImage, 180);
            graphics.drawImage(rotatedImage, (int) shipState.getPosition().getX(), (int) shipState.getPosition().getY() - fireImage.getHeight(), this);
        }
        if (shipState.getAccelerationDirection().getX() < 0 || shipState.isStabilize()) {
            final BufferedImage rotatedImage = rotateImage(fireImage, 270);
            graphics.drawImage(rotatedImage, (int) shipState.getPosition().getX() + shipImage.getWidth(), (int) shipState.getPosition().getY(), this);
        }
        if (shipState.getAccelerationDirection().getX() > 0 || shipState.isStabilize()) {
            final BufferedImage rotatedImage = rotateImage(fireImage, 90);
            graphics.drawImage(rotatedImage, (int) shipState.getPosition().getX() - rotatedImage.getWidth(), (int) shipState.getPosition().getY(), this);
        }
    }

    private BufferedImage rotateImage(final BufferedImage image, final int angle) {
        final double            rotationRequired = Math.toRadians(angle);
        final double            locationX        = image.getWidth() / 2;
        final double            locationY        = image.getHeight() / 2;
        final AffineTransform   transform        = AffineTransform.getRotateInstance(rotationRequired, locationX, locationY);
        final AffineTransformOp transformOp      = new AffineTransformOp(transform, AffineTransformOp.TYPE_BILINEAR);
        return transformOp.filter(image, null);
    }

    private void drawDetectors(final Graphics graphics) {
        detectors.stream()
                .filter(detector -> detector.getBeam() != null)
                .forEach(beam -> drawLine(beam.getBeam(), Color.RED, graphics));
    }

    private void drawLine(final Line2D line, final Color color, final Graphics graphics) {
        graphics.setColor(color);
        graphics.drawLine(line.getX1(), line.getY1(), line.getX2(), line.getY2());
    }

    private void drawRectangle(final Rectangle2D rectangle, final Color color, final Graphics graphics) {
        graphics.setColor(color);
        graphics.fillRect(rectangle.getX(), rectangle.getY(), rectangle.getWidth(), rectangle.getHeight());
    }

    private void drawRectangle(final Rectangle2D rectangle, final GradientPaint gradientPaint, final Graphics graphics) {
        ((Graphics2D) graphics).setPaint(gradientPaint);
        graphics.fillRect(rectangle.getX(), rectangle.getY(), rectangle.getWidth(), rectangle.getHeight());
    }

    private void drawInfoPanel(final Graphics graphics) {
        final ShipState     myShip    = getMyShip();
        final DecimalFormat formatter = new DecimalFormat("#0.0000");
        final Font          font      = new Font("Helvetica", Font.BOLD, 14);

        // Draw ship speed and acceleration
        //        drawShipDataForAllShips(graphics, formatter, font);
        final String speedXText = formatter.format(myShip.getSpeed().getX());
        final String speedYText = formatter.format(myShip.getSpeed().getY());
        final String speedText  = "Speed: [" + speedXText + ", " + speedYText + "]";

        final String accelerationXText = String.valueOf((int) myShip.getAccelerationDirection().getX());
        final String accelerationYText = String.valueOf((int) myShip.getAccelerationDirection().getY());
        final String accelerationText  = "Acceleration: [" + accelerationXText + ", " + accelerationYText + "]";

        final String text = speedText + "   " + accelerationText;
        graphics.setColor(Color.YELLOW);
        graphics.setFont(font);
        graphics.drawString(text, 5, 20);

        // Draw update times and FPS statistics
        final IntSummaryStatistics updateTimeStatistics = gameStatistics.getGameCycleStatistics();

        final String updateTimeText = "Update time"
                                      + "  min: " + updateTimeStatistics.getMin()
                                      + "  max: " + updateTimeStatistics.getMax()
                                      + "  average: " + ((int) updateTimeStatistics.getAverage());
        final String fpsText = "FPS3    "
                               + "  min: " + (1000 / updateTimeStatistics.getMax())
                               + "  max: " + (1000 / updateTimeStatistics.getMin())
                               + "  average: " + ((int) (1000 / updateTimeStatistics.getAverage()));
        graphics.setColor(Color.YELLOW);
        graphics.setFont(font);
        graphics.drawString(updateTimeText, 400, 20);
        graphics.drawString(fpsText, 400, 40);

        // Draw time left
        String timeLeftText = "Time left:  ";
        if (gameState.getStartTime() == 0) {
            // Game not started yet
            timeLeftText += "-";
        }
        else {
            final long timeSinceGameStart = System.currentTimeMillis() - gameState.getStartTime();
            timeLeftText += ((int) (GAME_TIME_LIMIT - timeSinceGameStart) / 1000);
        }
        graphics.setColor(Color.GREEN);
        graphics.setFont(font);
        graphics.drawString(timeLeftText, 800, 20);
    }

    private ShipState getMyShip() {
        return gameState.getShipStates().stream()
                .filter(shipState -> shipState.getName().equals(playerName))
                .findAny()
                .get();
    }

    //    private void drawShipDataForAllShips(final Graphics graphics, final DecimalFormat formatter, final Font font) {
    //        int linePosY = 20;
    //        for (final ShipState shipState : gameState.getShipStates()) {
    //            final String speedXText = formatter.format(shipState.getSpeed().getX());
    //            final String speedYText = formatter.format(shipState.getSpeed().getY());
    //            final String speedText  = "Speed: [" + speedXText + ", " + speedYText + "]";
    //
    //            final String accelerationXText = formatter.format(shipState.getAccelerationDirection().getX());
    //            final String accelerationYText = formatter.format(shipState.getAccelerationDirection().getY());
    //            final String accelerationText  = "Acceleration: [" + accelerationXText + ", " + accelerationYText + "]";
    //
    //            final String text = speedText + "   " + accelerationText;
    //            graphics.setColor(Color.YELLOW);
    //            graphics.setFont(font);
    //            graphics.drawString(text, 5, linePosY);
    //
    //            linePosY = linePosY + 10;
    //        }
    //    }

    private void printWaitingText(final Graphics graphics) {
        printJoinableMessage(graphics);
        printJoinedPlayers(graphics);
    }

    private void printJoinableMessage(final Graphics graphics) {
        graphics.setColor(Color.YELLOW);

        final Font bigFont = new Font("Helvetica", Font.BOLD, 50);
        graphics.setFont(bigFont);
        graphics.drawString("Game is now joinable", (Level.WIDTH / 2) - 250, (Level.HEIGHT / 2));

        final Font smallFont = new Font("Helvetica", Font.BOLD, 30);
        graphics.setFont(smallFont);
        graphics.drawString("Press space to start game", (Level.WIDTH / 2) - 180, (Level.HEIGHT / 2) + 50);
    }


    private void printJoinTimeIsUp(final Graphics graphics) {
        graphics.setColor(Color.YELLOW);
        final Font bigFont = new Font("Helvetica", Font.BOLD, 50);
        graphics.setFont(bigFont);
        graphics.drawString("Join-time timeout", (Level.WIDTH / 2) - 250, (Level.HEIGHT / 2));
    }

    private void printJoinedPlayers(final Graphics graphics) {
        final Rectangle2D background      = new Rectangle2D((Level.WIDTH / 2) - 300, (Level.HEIGHT / 2 + 100), 600, 150);
        final Rectangle2D backgroundFrame = new Rectangle2D(background.getX() - 10, background.getY() - 10, background.getWidth() + 20, background.getHeight() + 20);
        drawRectangle(backgroundFrame, Color.CYAN, graphics);
        drawRectangle(background, Color.BLACK, graphics);

        final Font font = new Font("Helvetica", Font.BOLD, 20);
        graphics.setFont(font);

        int x                  = background.getX() + 20;
        int y                  = background.getY() + 20;
        int numberOfShipsInRow = 0;
        for (final ShipState ship : gameState.getShipStates()) {
            final Rectangle2D shipColorRectangle = new Rectangle2D(x, y, 20, 20);
            drawRectangle(shipColorRectangle, new Color(ship.getColorRGB()), graphics);
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

    private void printGameResult(final Graphics graphics) {
        final Rectangle2D background      = new Rectangle2D((Level.WIDTH / 2) - 200, (Level.HEIGHT / 2 - 150), 400, 300);
        final Rectangle2D backgroundFrame = new Rectangle2D(background.getX() - 10, background.getY() - 10, background.getWidth() + 20, background.getHeight() + 20);
        drawRectangle(backgroundFrame, Color.GREEN, graphics);
        drawRectangle(background, Color.BLACK, graphics);

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
