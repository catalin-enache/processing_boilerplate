package netiko.stage;

import processing.core.*;
import static processing.core.PConstants.*;

public class Point extends PointVirtual implements IDrawable, IRadius {

    public int bgColor = Stage.getPointColor();
    public int strokeColor = Stage.getPointStrokeColor();
    protected PApplet p = Stage.getPApplet();

    protected float r;

    protected final Event.Name[] eventNamesRegisteredFor = new Event.Name[]{};

    Point(float x, float y, float r) {
        super(x, y);
        this.r = r;
    }

    Point(float x, float y) {
        this(x, y, Stage.POINT_RADIUS);
    }

    @Override
    public float r() {
        return r;
    }

    @Override
    public void r(float r) {
        this.r = r;
    }

    public void draw() {
        p.pushStyle();
        p.pushMatrix();

        p.stroke(strokeColor);
        p.fill(bgColor);

        p.translate(x, y);

        p.ellipseMode(RADIUS);
        p.ellipse(0, 0, r, r);

        p.popMatrix();
        p.popStyle();
    }

    public void setBgColor(int color) {
        bgColor = color;
    }

    public void resetBgColor() {
        bgColor = Stage.getPointColor();
    }

    @Override
    public Event.Name[] registerForEvents() {
        return eventNamesRegisteredFor;
    }

    @Override
    public void onEvent(Event evt, Object emitter) {}

    @Override
    public String toString() {
        return String.format("Point: %.2f %.2f %.2f", x, y, r);
    }

}
