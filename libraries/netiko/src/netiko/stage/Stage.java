package netiko.stage;

import processing.core.*;
import static processing.core.PConstants.*;
import java.util.ArrayList;

public class Stage  {

    protected static PApplet p;
    protected static String renderer;
    protected static int width;
    protected static int height;
    protected static int depth;
    protected static int bgColor;
    protected static int pColor;
    protected static int sColor;
    protected static boolean isCartezian;

    protected static ArrayList<Drawable> drawables = new ArrayList<>();

    // usage:  Stage.startSetup(this, P3D, 400, 400, 400, color(255), color(0), color(100), true);
    public static void startSetup(PApplet _p, String _renderer, int _width, int _height, int _depth, int _bgColor, int _pColor, int _sColor, boolean _isCartezian) {
        p = _p; // PApplet
        renderer = _renderer; // what renderer to use
        width = _width; // stage width
        height = _height; // stage height
        depth = _depth;
        bgColor = _bgColor; // background color
        pColor = _pColor; // point color
        sColor = _sColor; // stroke color
        isCartezian = _isCartezian;

        if (renderer != "") {
            p.size(width, height, renderer);
        }
        else {
            p.size(width, height);
        }

        p.smooth(8);
        p.stroke(bgColor);
        p.strokeWeight(1);

        if (renderer.equals(P3D)) {
            p.lights();
        }

        start();
    }

    public static void endSetup() {
        end();
    }

    public static void startDraw() {
        start();
    }

    public static void endDraw() {
        end();
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

    public static int getPointColor() {
        return pColor;
    }

    public static int getStrokeColor() {
        return sColor;
    }

    private static void start() {
        p.background(bgColor);
        if (isCartezian) {
            if (renderer == P3D) {
                p.translate(width/2, height/2, 0);
            }
            else {
                p.translate(width/2, height/2);
            }

        }
    }

    private static void end() {
        if (isCartezian) {
            drawCoords();
        }
        for (Drawable d : drawables) {
            d.draw();
        }
        if (isCartezian) {
            reverseSceneY();
        }
    }

    private static void reverseSceneY() {
        p.loadPixels();
        for (int i = 0; i < height/2; i++) {
            int[] tmp = new int[width];
            int startRowPosition = i * width;
            int endRowPosition = width * (height - i - 1);

            System.arraycopy(p.pixels, endRowPosition, tmp, 0, width); // save last row into tmp
            System.arraycopy(p.pixels, startRowPosition, p.pixels, endRowPosition, width); // put first row into  last row
            System.arraycopy(tmp, 0, p.pixels, startRowPosition, width); // put tmp row into  first row
        }
        p.updatePixels();
    }

    private static void drawCoords() {
        /*
        p.pushStyle();
        p.stroke(100);

        p.pushMatrix();
//        if (renderer == P3D) {
//            p.translate(0, 0, -1);
//        }
        p.line(-width / 2, 0, width / 2, 0);
        p.line(0, -height/2, 0, height/2);
        if (renderer == P3D) {
            p.line(0, 0, -depth / 2, 0, 0, depth / 2);
            p.camera(70.0F, 35.0F, 120.0F, 50.0F, 50.0F, 0.0F, 0.0F, 1.0F, 0.0F);
        }
        p.popMatrix();
        p.popStyle();
        */
    }
}
