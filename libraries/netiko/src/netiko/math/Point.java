package netiko.math;

public class Point implements IPoint {

    private float x;
    private float y;

    public Point (float x, float  y) {
        this.x = x;
        this.y = y;
    }

    public void x(float x) { this.x = x; }

    public void y(float y) { this.y = y; }

    public void xy(float x, float y) { this.x = x; this.y = y; }

    public float x() { return x; }

    public float y() { return y; }

    public float[] xy() { return new float[]{x, y}; }

}
