package spacerace.graphics;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import spacerace.domain.GameState;
import spacerace.domain.GameStatus;
import spacerace.domain.Statistics;
import spacerace.level.Level;
import spacerace.level.graphics.OrbitAnimation;
import spacerace.level.graphics.ParticleOrbitAnimation;
import spacerace.level.graphics.SolarSystem;
import spacerace.level.graphics.StarBackground;
import spacerace.level.graphics.Sun;

public class SpaceRaceViewerPanel extends SpaceRaceGraphicsPanel {


    private final String gameName;
    private final SolarSystem solarSystem = createSolarSystem(level.getWidth(), level.getHeight());
    private final StarBackground starBackground;

    SpaceRaceViewerPanel(final Level level, final GameState gameState, final Statistics gameCycleStatistics, final Statistics responseTimeStatistics, final String gameName) throws IOException {
        super(level, gameState, gameCycleStatistics, responseTimeStatistics);
        this.gameName = gameName;
        starBackground = new StarBackground(50, level.getWidth(), level.getHeight(), 200);
    }

    @Override
    public void paint(final Graphics g) {
        updatePaintTimeStatistics();
        super.paint(g);

        final Graphics2D graphics = (Graphics2D) g;
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                  RenderingHints.VALUE_ANTIALIAS_ON);
        if (gameState != null) {
            paintLevelBaseLayer(graphics);
            gameState.getShipStates()
                    .forEach(shipState -> drawShip(shipState, graphics));
            paintLevelTopLayer(graphics);
        }
        else {
            starBackground.paintStars(graphics);
            solarSystem.paint(graphics);
        }


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
        graphics.drawString("Searching for game:", (level.getWidth() / 2) - 250, ((level.getHeight() / 2) - 220));

        graphics.setColor(Color.RED);
        graphics.drawString(gameName, (level.getWidth() / 2) - 175, ((level.getHeight() / 2) - 160));
    }

    private static SolarSystem createSolarSystem(final int width, final int height) {
        final Color ellipseColor = new Color(114, 112, 112);

        final OrbitAnimation orbitAnimation1 = OrbitAnimation.anOrbitAnimation()
                .withCenterX(width / 2)
                .withCenterY(height / 2)
                .withA(200)
                .withB(25)
                .withEllipseColor(ellipseColor)
                .withSphereRadius(30)
                .withSphereColor(Color.BLUE)
                .withSphereShadowColor(Color.BLACK)
                .build();

        final OrbitAnimation orbitAnimation2 = OrbitAnimation.anOrbitAnimation()
                .withCenterX(width / 2)
                .withCenterY(height / 2)
                .withA(300)
                .withB(50)
                .withEllipseColor(ellipseColor)
                .withSphereRadius(50)
                .withSphereColor(Color.GREEN)
                .withSphereShadowColor(Color.BLACK)
                .build();
        final List<OrbitAnimation> moonOrbitAnimations = getMoonOrbitAnimations(orbitAnimation2);
        orbitAnimation2.setMoonOrbitAnimations(moonOrbitAnimations);

        final OrbitAnimation orbitAnimation3 = OrbitAnimation.anOrbitAnimation()
                .withCenterX(width / 2)
                .withCenterY(height / 2)
                .withA(400)
                .withB(75)
                .withEllipseColor(ellipseColor)
                .withSphereRadius(60)
                .withSphereColor(Color.RED)
                .withSphereShadowColor(Color.BLACK)
                .build();
        final List<ParticleOrbitAnimation> ringOrbitAnimations = getRingOrbitAnimations(orbitAnimation3);
        orbitAnimation3.setParticleOrbitAnimations(ringOrbitAnimations);

        final List<OrbitAnimation> orbitAnimations = Arrays.asList(orbitAnimation1, orbitAnimation2, orbitAnimation3);
        final Sun                  sun             = new Sun(80, width / 2, height / 2);
        return new SolarSystem(sun, orbitAnimations);
    }

    private static List<OrbitAnimation> getMoonOrbitAnimations(final OrbitAnimation planetOrbitAnimation) {
        final Color transparentColor = GraphicsUtils.createColorWithAlpha(Color.BLACK, 0);
        final OrbitAnimation moonOrbitAnimation1 = OrbitAnimation.anOrbitAnimation()
                .withCenterX(planetOrbitAnimation.getSphere().getCenterX())
                .withCenterY(planetOrbitAnimation.getSphere().getCenterY())
                .withA((int) (planetOrbitAnimation.getSphere().getRadius() * 1.3))
                .withB(planetOrbitAnimation.getSphere().getRadius() / 4)
                .withEllipseColor(transparentColor)
                .withSphereRadius(15)
                .withSphereColor(Color.PINK)
                .withSphereShadowColor(Color.BLACK)
                .build();

        final OrbitAnimation moonOrbitAnimation2 = OrbitAnimation.anOrbitAnimation()
                .withCenterX(planetOrbitAnimation.getSphere().getCenterX())
                .withCenterY(planetOrbitAnimation.getSphere().getCenterY())
                .withA((int) (planetOrbitAnimation.getSphere().getRadius() * 1.5))
                .withB(planetOrbitAnimation.getSphere().getRadius() / 3)
                .withEllipseColor(transparentColor)
                .withSphereRadius(10)
                .withSphereColor(Color.ORANGE)
                .withSphereShadowColor(Color.BLACK)
                .build();

        final OrbitAnimation moonOrbitAnimation3 = OrbitAnimation.anOrbitAnimation()
                .withCenterX(planetOrbitAnimation.getSphere().getCenterX())
                .withCenterY(planetOrbitAnimation.getSphere().getCenterY())
                .withA((int) (planetOrbitAnimation.getSphere().getRadius() * 1.7))
                .withB(planetOrbitAnimation.getSphere().getRadius() / 2)
                .withEllipseColor(transparentColor)
                .withSphereRadius(12)
                .withSphereColor(Color.LIGHT_GRAY)
                .withSphereShadowColor(Color.BLACK)
                .build();
        return new ArrayList<>(Arrays.asList(moonOrbitAnimation1, moonOrbitAnimation2, moonOrbitAnimation3));
    }

    private static List<ParticleOrbitAnimation> getRingOrbitAnimations(final OrbitAnimation planetOrbitAnimation) {
        final Color                        transparentColor   = GraphicsUtils.createColorWithAlpha(Color.BLACK, 0);
        final List<ParticleOrbitAnimation> particleAnimations = new ArrayList<>();

        for (int i = 0; i < 1_000; i++) {
            final ParticleOrbitAnimation particleAnimation = ParticleOrbitAnimation.aParticleOrbitAnimation()
                    .withCenterX(planetOrbitAnimation.getSphere().getCenterX())
                    .withCenterY(planetOrbitAnimation.getSphere().getCenterY())
                    .withA((int) (planetOrbitAnimation.getSphere().getRadius() * (1 + 0.002 * i)))
                    .withB(planetOrbitAnimation.getSphere().getRadius() / 2)
                    .withParticleRadius(3)
                    .build();
            particleAnimations.add(particleAnimation);
        }
        return particleAnimations;
    }
}
