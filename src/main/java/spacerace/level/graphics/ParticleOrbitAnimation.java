package spacerace.level.graphics;

import java.awt.Graphics2D;
import java.util.Random;

public class ParticleOrbitAnimation {

    private static final Random RANDOM = new Random();
    private       double  sphereRelativeCenterX;
    private       double  angle;
    private final Ellipse ellipse;
    private       int     particleX;
    private       int     particleY;
    private final double  speed;
    private final int     particleWidth;

    private ParticleOrbitAnimation(final int ellipseCenterX, final int ellipseCenterY, final int a, final int b, final int particleWidth) {
        this.ellipse = new Ellipse(a, b, ellipseCenterX, ellipseCenterY);
        if (RANDOM.nextInt(100) < 50) {
            this.particleX = ellipseCenterX + a; // Start on right side of ellipse
            angle = 0;
        }
        else {
            this.particleX = ellipseCenterX - a; // Start on left side of ellipse
            angle = Math.PI;
        }
        this.particleY = ellipseCenterY; // Start on right side of ellipse
        this.sphereRelativeCenterX = 1;
        this.speed = 0.01 / (a / 200d);
        this.particleWidth = particleWidth;
    }

    public static OrbitAnimationBuilder aParticleOrbitAnimation() {
        return ellipseCenterX -> ellipseCenterY -> a -> b -> particleWidth -> () ->
                new ParticleOrbitAnimation(ellipseCenterX, ellipseCenterY, a, b, particleWidth);
    }

    public interface OrbitAnimationBuilder {
        EllipseCenterXBuilder withCenterX(int ellipseCenterX);
    }

    public interface EllipseCenterXBuilder {
        EllipseCenterYBuilder withCenterY(int ellipseCenterY);
    }

    public interface EllipseCenterYBuilder {
        ABuilder withA(int a);
    }

    public interface ABuilder {
        BBuilder withB(int b);
    }

    public interface BBuilder {
        ParticleWidthBuilder withParticleRadius(int particleRadius);
    }

    public interface ParticleWidthBuilder {
        ParticleOrbitAnimation build();
    }

    public Ellipse getEllipse() {
        return ellipse;
    }

    void paintUpperPart(final Graphics2D graphics) {
        if (isSphereInUpperQuadrants()) {
            paintPlanet(graphics);
        }
    }

    void paintLowerPart(final Graphics2D graphics) {
        if (!isSphereInUpperQuadrants()) {
            paintPlanet(graphics);
        }
    }

    private boolean isSphereInUpperQuadrants() {
        return particleY <= ellipse.getCenterY();
    }

    private void paintPlanet(final Graphics2D graphics) {
        graphics.fillRect(particleX, particleY, particleWidth, particleWidth);
    }

    void tickAnimation() {
        if ((angle >= (2 * Math.PI))) {
            angle = 0;
        }
        angle += speed * Math.PI;
        sphereRelativeCenterX = Math.cos(angle) * ellipse.getA();

        final double sphereRelativeCenterY = Math.copySign(ellipse.calculateY(sphereRelativeCenterX), -Math.sin(angle));
        final double sphereAbsoluteCenterX = sphereRelativeCenterX + ellipse.getCenterX();
        final double sphereAbsoluteCenterY = sphereRelativeCenterY + ellipse.getCenterY();
        particleX = (int) sphereAbsoluteCenterX;
        particleY = (int) sphereAbsoluteCenterY;
    }
}
