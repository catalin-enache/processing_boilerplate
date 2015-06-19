package netiko.stage;

import processing.core.*;
import static processing.core.PConstants.*;

public class Point implements IDrawable {

    public float x, y, r;
    public int color = Stage.getPointColor();
    protected PApplet p = Stage.getPApplet();

    protected final Event.Name[] eventNamesRegisteredFor = new Event.Name[]{};

    Point(float x, float y, float r) {
        this.x = x; this.y = y; this.r = r;
    }

    public void draw() {
        p.pushStyle();
        p.pushMatrix();

        p.fill(color);

        p.translate(x, y);

        p.ellipseMode(RADIUS);
        p.ellipse(0, 0, r, r);

        p.popMatrix();
        p.popStyle();
    }

    @Override
    public Event.Name[] registerForEvents() {
        return eventNamesRegisteredFor;
    }

    @Override
    public void onEvent(Event evt, Object emitter) {}

    @Override
    public String toString() {
        return String.format("Point: %.2f %.2f, %.2f", x, y, r);
    }

}
