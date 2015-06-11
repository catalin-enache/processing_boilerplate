package netiko.stage;

import processing.core.*;
import static processing.core.PConstants.*;

public class Point implements IDrawable {

    public float x, y, z, r;
    public int color;
    protected PApplet p = Stage.getPApplet();
    protected String renderer;

    Point(int x, int y, int z, int r) {
        this.x = x; this.y = y; this.z = z; this.r = r;
        this.color = Stage.getPointColor();
        this.renderer = Stage.getRenderer();
    }

    public void draw() {
        p.pushStyle();

        p.fill(color);

        p.pushMatrix();
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

}
