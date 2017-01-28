package spacerace.level;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

import spacerace.domain.Line2D;
import spacerace.domain.Rectangle2D;
import spacerace.domain.Vector2D;
import spacerace.graphics.GraphicsUtils;
import spacerace.level.graphics.OrbitAnimation;
import spacerace.level.graphics.SolarSystem;
import spacerace.level.graphics.Sun;

class Level2Builder {

    private Level2Builder() {
        // Intentionally empty
    }

    static Level build() {
        final int levelNumber = 2;
        final int width       = 900;
        final int height      = 600;

        final List<Line2D> trackBorders = createTrackBoarders(width, height);

        final Vector2D startPosition = new Vector2D((width / 2) - 25, height - 80);
        final Line2D   goalLine      = new Line2D((width / 2) - 375, height - 275, (width / 2) - 375, height - 400);

        final SolarSystem solarSystem = createSolarSystem(width, height);

        final Level2Builder        builder          = new Level2Builder();
        final Consumer<Graphics2D> baseLayerPainter = graphics2D -> builder.paintBaseLayer(graphics2D, height, width, solarSystem);

        return Level.Builder.aLevel()
                .withNumber(levelNumber)
                .withWidth(width)
                .withHeight(height)
                .withStartPosition(startPosition)
                .withGoalLine(goalLine)
                .withTrackBoarders(trackBorders)
                .withBaseLayerPainter(baseLayerPainter)
                .build();
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

        final List<OrbitAnimation> orbitAnimations = Arrays.asList(orbitAnimation1, orbitAnimation2, orbitAnimation3);
        final Sun                  sun             = new Sun(80, width / 2, height / 2);
        return new SolarSystem(sun, orbitAnimations);
    }

    private static List<Line2D> createTrackBoarders(final int width, final int height) {
        final Line2D start = new Line2D((width / 2) - 75, height - 25, (width / 2) + 75, height - 25);

        // Transport
        final Line2D leftVertical1  = new Line2D((width / 2) - 75, height - 25, (width / 2) - 75, height - 125);
        final Line2D rightVertical1 = new Line2D((width / 2) + 75, height - 25, (width / 2) + 75, height - 125);

        // First side path
        final Line2D leftHorizontal1  = new Line2D((width / 2) - 75, height - 125, (width / 2) - 200, height - 125);
        final Line2D rightHorizontal1 = new Line2D((width / 2) + 75, height - 125, (width / 2) + 200, height - 125);

        final Line2D leftVertical2  = new Line2D((width / 2) - 200, height - 125, (width / 2) - 200, height - 225);
        final Line2D rightVertical2 = new Line2D((width / 2) + 200, height - 125, (width / 2) + 200, height - 225);

        final Line2D leftHorizontal2  = new Line2D((width / 2) - 200, height - 225, (width / 2) - 75, height - 225);
        final Line2D rightHorizontal2 = new Line2D((width / 2) + 75, height - 225, (width / 2) + 200, height - 225);

        // Transport
        final Line2D leftVertical3  = new Line2D((width / 2) - 75, height - 225, (width / 2) - 75, height - 275);
        final Line2D rightVertical3 = new Line2D((width / 2) + 75, height - 225, (width / 2) + 75, height - 275);

        // Second side path
        final Line2D leftHorizontal3  = new Line2D((width / 2) - 400, height - 275, (width / 2) - 75, height - 275);
        final Line2D rightHorizontal3 = new Line2D((width / 2) + 75, height - 275, (width / 2) + 400, height - 275);

        final Line2D leftVertical4  = new Line2D((width / 2) - 400, height - 275, (width / 2) - 400, height - 400);
        final Line2D rightVertical4 = new Line2D((width / 2) + 400, height - 275, (width / 2) + 400, height - 400);

        final Line2D leftHorizontal4  = new Line2D((width / 2) - 400, height - 400, (width / 2) - 75, height - 400);
        final Line2D rightHorizontal4 = new Line2D((width / 2) + 75, height - 400, (width / 2) + 400, height - 400);


        // End part
        final Line2D leftVertical5  = new Line2D((width / 2) - 75, height - 400, (width / 2) - 75, height - 500);
        final Line2D rightVertical5 = new Line2D((width / 2) + 75, height - 400, (width / 2) + 75, height - 500);
        final Line2D end            = new Line2D((width / 2) - 75, height - 500, (width / 2) + 75, height - 500);

        return Arrays.asList(
                start,
                leftVertical1,
                rightVertical1,
                leftHorizontal1,
                rightHorizontal1,
                leftVertical2,
                rightVertical2,
                leftHorizontal2,
                rightHorizontal2,
                leftVertical3,
                rightVertical3,
                leftHorizontal3,
                rightHorizontal3,
                leftVertical4,
                rightVertical4,
                leftHorizontal4,
                rightHorizontal4,
                leftVertical5,
                rightVertical5,
                end);
    }

    private void paintBaseLayer(final Graphics2D graphics, final int height, final int width, final SolarSystem solarSystem) {
        drawFinishLine(graphics, height, width);
        solarSystem.paint(graphics);
    }

    private void drawFinishLine(final Graphics graphics, final int height, final int width) {
        final Rectangle2D   finish        = new Rectangle2D((width / 2) - 385, height - 400, 10, 125);
        final GradientPaint gradientPaint = new GradientPaint(25, 25, Color.WHITE, 25, 15, Color.BLACK, true);
        GraphicsUtils.drawRectangle(finish, gradientPaint, graphics);
    }
}
