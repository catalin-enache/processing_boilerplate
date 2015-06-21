package netiko.stage;

public class PointVirtual implements IPoint {

    protected float x, y;

    public PointVirtual(float x, float y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public float x() {
        return x;
    }

    @Override
    public void x(float x) {
        this.x = x;
        Stage.emitEvent(new Event(Event.Name.pointUpdated, null), this);
    }

    @Override
    public float y() {
        return y;
    }

    @Override
    public void y(float y) {
        this.y = y;
        Stage.emitEvent(new Event(Event.Name.pointUpdated, null), this);
    }

    @Override
    public float[] xy() {
        return new float[]{x, y};
    }

    @Override
    public void xy(float x, float y) {
        this.x = x;
        this.y = y;
        Stage.emitEvent(new Event(Event.Name.pointUpdated, null), this);
    }


    @Override
    public String toString() {
        return String.format("PointVirtual: %.2f %.2f", x, y);
    }

}
