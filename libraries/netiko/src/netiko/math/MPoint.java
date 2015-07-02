package netiko.math;

public class MPoint implements IPoint {

    private float x;
    private float y;

    public MPoint(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public void x(float x) { this.x = x; }

    public void y(float y) { this.y = y; }

    public void xy(float x, float y) { this.x = x; this.y = y; }

    public float x() { return x; }

    public float y() { return y; }

    public float[] xy() { return new float[]{x, y}; }

    @Override
    public String toString() {
        return String.format("MPoint: %f %f", x, y);
    }

}
