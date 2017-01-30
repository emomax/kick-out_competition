package spacerace.level.graphics;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.List;

import spacerace.graphics.GraphicsUtils;

public class OrbitAnimation {

    private double sphereRelativeCenterX;
    private double angle = 0;
    private final Ellipse                      ellipse;
    private final Sphere                       sphere;
    private final double                       speed;
    private       List<OrbitAnimation>         moonOrbitAnimations;
    private       List<ParticleOrbitAnimation> particleOrbitAnimations;

    private OrbitAnimation(final int ellipseCenterX, final int ellipseCenterY, final int a, final int b, final Color ellipseColor, final int sphereRadius, final Color sphereColor, final Color sphereShadowColor) {
        this.ellipse = new Ellipse(a, b, ellipseCenterX, ellipseCenterY, ellipseColor);
        this.sphere = new Sphere(sphereColor, sphereShadowColor, sphereRadius, ellipseCenterX + a, ellipseCenterY); // Start on right side of ellipse
        this.sphereRelativeCenterX = 1;
        this.speed = 0.01 / (a / 200d);
    }

    public static OrbitAnimationBuilder anOrbitAnimation() {
        return ellipseCenterX -> ellipseCenterY -> a -> b -> ellipseColor -> sphereRadius -> sphereColor -> sphereShadowColor -> () ->
                new OrbitAnimation(ellipseCenterX, ellipseCenterY, a, b, ellipseColor, sphereRadius, sphereColor, sphereShadowColor);
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
        EllipseColorBuilder withEllipseColor(Color ellipseColor);
    }

    public interface EllipseColorBuilder {
        SphereRadiusBuilder withSphereRadius(int sphereRadius);
    }

    public interface SphereRadiusBuilder {
        SphereColorBuilder withSphereColor(Color sphereColor);
    }

    public interface SphereColorBuilder {
        SphereShadowColorBuilder withSphereShadowColor(Color sphereShadowColor);
    }

    public interface SphereShadowColorBuilder {
        OrbitAnimation build();
    }

    public Sphere getSphere() {
        return sphere;
    }

    public Ellipse getEllipse() {
        return ellipse;
    }

    public void setMoonOrbitAnimations(final List<OrbitAnimation> moonOrbitAnimations) {
        this.moonOrbitAnimations = moonOrbitAnimations;
    }

    public void setParticleOrbitAnimations(final List<ParticleOrbitAnimation> particleOrbitAnimations) {
        this.particleOrbitAnimations = particleOrbitAnimations;
    }

    void paintUpperPart(final Graphics2D graphics) {
        ellipse.paintUpperPart(graphics);
        if (isSphereInUpperQuadrants()) {
            paintPlanet(graphics);
        }
    }

    void paintLowerPart(final Graphics2D graphics) {
        ellipse.paintLowerPart(graphics);
        if (!isSphereInUpperQuadrants()) {
            paintPlanet(graphics);
        }
    }

    private boolean isSphereInUpperQuadrants() {
        return sphere.getCenterY() <= ellipse.getCenterY();
    }

    private void paintPlanet(final Graphics2D graphics) {
        if (moonOrbitAnimations != null) {
            paintPlanetAndMoons(graphics);
        }
        else if (particleOrbitAnimations != null) {
            paintPlanetAndParticles(graphics);
        }
        else {
            sphere.paint(graphics);
        }
    }

    private void paintPlanetAndMoons(final Graphics2D graphics) {
        for (final OrbitAnimation moonOrbitAnimation : moonOrbitAnimations) {
            moonOrbitAnimation.getEllipse().setCenterX(sphere.getCenterX());
            moonOrbitAnimation.getEllipse().setCenterY(sphere.getCenterY());
            moonOrbitAnimation.tickAnimation();
        }

        GraphicsUtils.paintWithRotation(30, sphere.getCenterX(), sphere.getCenterY(), graphics, graphics2D -> {
            for (int i = moonOrbitAnimations.size() - 1; i >= 0; i--) {
                final OrbitAnimation moonOrbitAnimation = moonOrbitAnimations.get(i);
                moonOrbitAnimation.paintUpperPart(graphics);
            }
        });

        sphere.paint(graphics);

        GraphicsUtils.paintWithRotation(30, sphere.getCenterX(), sphere.getCenterY(), graphics, graphics2D -> {
            for (final OrbitAnimation moonOrbitAnimation : moonOrbitAnimations) {
                moonOrbitAnimation.paintLowerPart(graphics);
            }
        });
    }

    private void paintPlanetAndParticles(final Graphics2D graphics) {
        for (final ParticleOrbitAnimation particleOrbitAnimation : particleOrbitAnimations) {
            particleOrbitAnimation.getEllipse().setCenterX(sphere.getCenterX());
            particleOrbitAnimation.getEllipse().setCenterY(sphere.getCenterY());
            particleOrbitAnimation.tickAnimation();
        }

        graphics.setColor(Color.white);
        GraphicsUtils.paintWithRotation(30, sphere.getCenterX(), sphere.getCenterY(), graphics, graphics2D -> {
            for (int i = particleOrbitAnimations.size() - 1; i >= 0; i--) {
                final ParticleOrbitAnimation particleOrbitAnimations = this.particleOrbitAnimations.get(i);
                particleOrbitAnimations.paintUpperPart(graphics);
            }
        });

        sphere.paint(graphics);

        graphics.setColor(Color.white);
        GraphicsUtils.paintWithRotation(30, sphere.getCenterX(), sphere.getCenterY(), graphics, graphics2D -> {
            for (final ParticleOrbitAnimation particleOrbitAnimation : particleOrbitAnimations) {
                particleOrbitAnimation.paintLowerPart(graphics);
            }
        });
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
        sphere.setCenterX((int) sphereAbsoluteCenterX);
        sphere.setCenterY((int) sphereAbsoluteCenterY);
    }
}
