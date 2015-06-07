package netiko.stage;

import processing.core.*;
import static processing.core.PConstants.*;
import java.util.ArrayList;

public class Stage  {

    protected static PApplet p;
    protected static String renderer;
    protected static int width;
    protected static int height;
    protected static int bgColor;
    protected static int pColor;
    protected static int sColor;

    protected static ArrayList<Drawable> drawables = new ArrayList<>();

    public static void setup(PApplet _p, String _renderer, int _width, int _height, int _bgColor, int _pColor, int _sColor) {
        p = _p; // PApplet
        renderer = _renderer; // what renderer to use
        width = _width; // stage width
        height = _height; // stage height
        bgColor = _bgColor; // background color
        pColor = _pColor; // point color
        sColor = _sColor; // stroke color

        p.smooth(8);
        p.background(bgColor);
        p.stroke(bgColor);
        p.strokeWeight(1);

        if (renderer != "") {
            p.size(width, height, renderer);
        }
        else {
            p.size(width, height);
        }

        if (renderer.equals(P3D)) {
            p.lights();
        }
    }

    public static void draw() {
        p.background(bgColor);
        for (Drawable d : drawables) {
            d.draw();
        }
        p.pushStyle();
        p.noFill();
        p.stroke(255, 102, 0);
        p.line(30, 20, 80, 5);
        p.line(80, 75, 30, 75);
        p.stroke(sColor);
        p.bezier(30, 20,  80, 5,  80, 75,  30, 75);
        p.popStyle();
    }

    public static Point point(int x, int y, int z, int r) {
        Point newPoint =  new Point(x, y, z, r);
        drawables.add(newPoint);
        return newPoint;
    }

    public static PApplet getPApplet() {
        return p;
    }
    public static String getRenderer() {
        return renderer;
    }
    public static int getBgColor() {
        return bgColor;
    }
    public static int getPColor() {
        return pColor;
    }
    public static int getSColor() {
        return sColor;
    }
}
