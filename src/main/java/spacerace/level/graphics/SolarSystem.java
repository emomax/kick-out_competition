package spacerace.level.graphics;

import java.awt.Graphics2D;
import java.util.List;

public class SolarSystem {
    private final Sun                  sun;
    private final List<OrbitAnimation> orbitAnimations;

    /**
     * The orbits must be provided in order with the shortest orbit first
     */
    public SolarSystem(final Sun sun, final List<OrbitAnimation> orbitAnimations) {
        this.sun = sun;
        this.orbitAnimations = orbitAnimations;
    }

    public void paint(final Graphics2D graphics) {
        orbitAnimations.forEach(OrbitAnimation::tickAnimation);

        for (int i = orbitAnimations.size() - 1; i >= 0; i--) {
            orbitAnimations.get(i).paintUpperPart(graphics);
        }

        sun.paint(graphics);

        for (final OrbitAnimation orbitAnimation : orbitAnimations) {
            orbitAnimation.paintLowerPart(graphics);
        }
    }
}
