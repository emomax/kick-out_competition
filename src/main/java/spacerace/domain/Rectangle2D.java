package spacerace.domain;

import java.awt.Rectangle;
import java.io.Serializable;

public class Rectangle2D implements Serializable {

    private int x;
    private int y;
    private int width;
    private int height;

    public Rectangle2D() {
        // For JSON conversion
    }

    public Rectangle2D(final int x, final int y, final int width, final int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public int getX() {
        return x;
    }

    public void setX(final int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(final int y) {
        this.y = y;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(final int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(final int height) {
        this.height = height;
    }

    public Rectangle toAWTRectangle() {
        return new Rectangle(x, y, width, height);
    }
}
