package spacerace.level;

import java.awt.Graphics2D;

import spacerace.domain.ShipState;

public interface RocketFireGraphics {

    void paint(ShipState shipState, ShipGraphics shipGraphics, Graphics2D graphics);
}
