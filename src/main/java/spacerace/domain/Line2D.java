package spacerace.domain;

public class Line2D {

    private final int x1;
    private final int y1;
    private final int x2;
    private final int y2;

    public Line2D(final int x1, final int y1, final int x2, final int y2) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
    }

    public int getX1() {
        return x1;
    }

    public int getY1() {
        return y1;
    }

    public int getX2() {
        return x2;
    }

    public int getY2() {
        return y2;
    }

    public java.awt.geom.Line2D convertToAWTLine2D() {
        return new java.awt.geom.Line2D.Double(x1, y1, x2, y2);
    }

    @Override
    public String toString() {
        return "Line2D{" +
               "x1=" + x1 +
               ", y1=" + y1 +
               ", x2=" + x2 +
               ", y2=" + y2 +
               '}';
    }
}
