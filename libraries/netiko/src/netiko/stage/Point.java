package netiko.stage;

import processing.core.*;
import static processing.core.PConstants.*;

public class Point implements IDrawable {

    public float x, y, z, r;
    public int color = Stage.getPointColor();
    protected PApplet p = Stage.getPApplet();
    protected String renderer = Stage.getRenderer();

    protected final Event.Name[] eventNamesRegisteredFor = new Event.Name[]{};

    Point(float x, float y, float z, float r) {
        this.x = x; this.y = y; this.z = z; this.r = r;
    }

    public void draw() {
        p.pushStyle();
        p.pushMatrix();

        p.fill(color);

        if (renderer == P3D) {
            p.translate(x, y, z);
        }
        else {
            p.translate(x, y);
        }

        if (renderer == P3D) {
            p.noStroke();
            p.sphere(r);
        }
        else {
            p.ellipseMode(RADIUS);
            p.ellipse(0, 0, r, r);
        }

        p.popMatrix();
        p.popStyle();
    }

    @Override
    public Event.Name[] registerForEvents() {
        return eventNamesRegisteredFor;
    }

    @Override
    public void onEvent(Event evt) {

    }

    @Override
    public String toString() {
        return String.format("Point: %.2f %.2f, %.2f, %.2f", x, y, z, r);
    }

}
