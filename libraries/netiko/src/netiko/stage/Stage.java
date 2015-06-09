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
    protected static int bgColor = -1;
    protected static int pColor = -1;
    protected static int sColor = -1;
    protected static boolean isCartezian;
    protected static float rotX = 0;
    protected static float rotY = 0;
    protected static float cPosZ = 30;

    protected static ArrayList<Drawable> drawables = new ArrayList<>();

    // usage:  Stage.startSetup(this, P3D, 800, 800, 800, 0XFFFFFFFF, 0XFF444444, color(255, 102, 0), true);
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
        p.stroke(bgColor); // seems to help with antialiasing
        p.strokeWeight(1);

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
        if (renderer.equals(P3D)) {
            p.lights();
        }

        p.background(bgColor);

        if (isCartezian) {
            if (renderer.equals(P3D)) {
                if (p.keyPressed) {
                    if (p.key == 'w') {
                        cPosZ += 0.2;
                    } else if (p.key == 's') {
                        cPosZ -= 0.2;
                    }
                    // set the camera
                    p.camera(width/2F, height/2F, (height/2F) / p.tan(PI/180F * cPosZ), width/2F, height/2F, 0F, 0F, 1F, 0F); // PI*?
                }
                // move to the middle of stage
                p.translate(width/2, height/2, 0);
            }
            else {
                p.translate(width/2, height/2);
            }
        }

        if (renderer.equals(P3D)) {
            if (p.mousePressed && p.mouseButton == RIGHT) {
                rotX = PI/180 * (p.mouseX - width/2) / 5;
                rotY = PI/180 * (p.mouseY - height/2) / 5;
            }
            // apply any rotation
            p.rotateY(rotX);
            if (isCartezian) {
                p.rotateX(rotY);
            } else {
                p.rotateX(-rotY);
            }
        }
        // let the rest of objects draw their stuff in this new environment
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

            System.arraycopy(p.pixels, endRowPosition, tmp, 0, width); // save end row into tmp
            System.arraycopy(p.pixels, startRowPosition, p.pixels, endRowPosition, width); // put start row into  end row
            System.arraycopy(tmp, 0, p.pixels, startRowPosition, width); // put tmp row into  start row
        }
        p.updatePixels();
    }

    private static void drawCoords() {
        p.pushStyle();

        p.stroke(255, 0, 0); // x
        p.line(-width / 2, 0, width / 2, 0);
        p.stroke(0, 255, 0); // y
        p.line(0, -height/2, 0, height/2);
        if (renderer == P3D) {
            p.stroke(0, 0, 255); // z
            p.line(0, 0, -depth / 2, 0, 0, depth / 2);
            p.noFill();
            p.stroke(100); // cube
            p.box(width, height, depth);
        }

        p.popStyle();
    }
}
