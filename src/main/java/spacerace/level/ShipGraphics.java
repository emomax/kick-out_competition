package spacerace.level;

import java.awt.Graphics2D;

import spacerace.domain.ShipState;

public interface ShipGraphics {

    int getWidth();

    int getHeight();

    void paint(ShipState shipState, Graphics2D graphics);
}
