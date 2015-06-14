package netiko.stage;

import processing.core.*;
import static processing.core.PConstants.*;
import java.util.ArrayList;
import java.util.HashMap;

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

    protected static PImage cArrow;

    protected static boolean mousePressed = false;
    protected static boolean mouseHoverOn = false;
    protected static Object mouseHoverSetter = null;

    protected static ArrayList<IDrawable> drawables = new ArrayList<>();
    /*
    usage:  Stage.startSetup(this, P3D, 800, 800, 800, 0XFFFFFFFF, 0XFF444444, color(255, 102, 0), true);
    hint(DISABLE_OPTIMIZED_STROKE); // might be used as default
    hint(DISABLE_DEPTH_TEST); // optional where needed
    */
    public static void startSetup(PApplet _p, String _renderer, int _width, int _height, int _depth, int _bgColor, int _pColor, int _sColor, boolean _isCartezian) {
        p = _p; // PApplet
        renderer = _renderer; // what renderer to use

        cArrow = p.loadImage(Stage.class.getResource("use32.gif").toString());

        width = _width; // stage width
        height = _height; // stage height
        depth = _depth;

        bgColor = _bgColor; // background color
        pColor = _pColor; // point color
        sColor = _sColor; // stroke color

        isCartezian = _isCartezian;

        if (!renderer.equals("")) {
            p.size(width, height, renderer);
        }
        else {
            p.size(width, height);
        }

        start();
    }

    public static void endSetup() { end(); }

    public static void startDraw() {
        start();
    }

    public static void endDraw() {
        end();
    }

    // SECTION create and return objects

    public static Point point(float x, float y, float z, float r) {
        Point newPoint =  new Point(x, y, z, r);
        drawables.add(newPoint);
        return newPoint;
    }

    public static PointDraggable pointDraggable(float x, float y, float z, float r) {
        PointDraggable newPointDraggable =  new PointDraggable(x, y, z, r);
        drawables.add(newPointDraggable);
        return newPointDraggable;
    }

    public static Shape shape(int bgColor, int sColor, Integer beginShape, Integer endShape, boolean showPoints, ArrayList<ShapeData> shapeData) {
        Shape newShape =  new Shape(bgColor, sColor, beginShape, endShape, showPoints, shapeData);
        drawables.add(newShape);
        return newShape;
    }

    // SECTION get stuff

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

    // to be called after stage transformation was applied
    public static float[] getTranslatedMouse() {
        float[] coords;
        if (isCartezian) {
            coords = new float[]{p.mouseX - width/2, -(p.mouseY - height/2)};
        } else {
            coords = new float[]{p.mouseX, p.mouseY};
        }
        //System.out.println(coords[0] + " | " + coords[1]);
        //System.out.println((p.modelX(p.mouseX - width/2, p.mouseY - height/2, 0) - width/2) + " || " + (-(p.modelY(p.mouseX - width/2, p.mouseY - height/2, 0) - height/2)));
        return coords;
    }

    // SECTION set stuff

    public static void setHoverState(boolean on, Object setter) {
        if (on && setter != mouseHoverSetter) {
            mouseHoverOn = true;
            mouseHoverSetter = setter;
            p.cursor(HAND);
        } else if (!on && setter == mouseHoverSetter) {
            mouseHoverOn = false;
            mouseHoverSetter = null;
            p.cursor(ARROW);
        }
    }

    // SECTION internal stage stuff

    private static void resetView () {
        cPosZ = 30;
        rotX = 0;
        rotY = 0;
    }

    private static void start() {
        p.smooth(8);
        p.stroke(bgColor); // seems to help with antialiasing
        p.strokeWeight(1);
        // DISABLE_OPTIMIZED_STROKE helps in transparency issues (when transparency seems to be ignored)
        // though not necessarily needed if DISABLE_DEPTH_TEST is on
        // yet it is still  needed because without it the strokes behind a transparent fill will not respect the order
        // and will be seen as they would have been drown on top of the fill (even if beyond it)
        //p.hint(DISABLE_OPTIMIZED_STROKE);
        // DISABLE_DEPTH_TEST helps in transparency issues (when transparency seems to be ignored)
        // better results in combination with DISABLE_OPTIMIZED_STROKE
        // though overlapping fills do not respect z buffer
        // http://processingjs.org/reference/hint_/
        //p.hint(DISABLE_DEPTH_TEST);
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
                    } else if (p.key == 'r') {
                        resetView();
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

        if (isCartezian) {
            drawCoords(); //  put here in case of DISABLE_OPTIMIZED_STROKE is on and DISABLE_DEPTH_TEST is off so we still can see the coords as they are drown before anything
        }

        emitStageEvent();
        // let the rest of objects draw their stuff in this new environment
    }

    private static void end() {
        for (IDrawable d : drawables) {
            d.draw();
        }

        drawCursor();

        if (isCartezian) {
            reverseSceneY();
        }
    }

    private static void drawCursor() {
        float[] xyz = getTranslatedMouse();
        //p.ellipse(xyz[0], xyz[1], 5, 5);
        p.hint(DISABLE_DEPTH_TEST);
        p.image(cArrow, xyz[0], xyz[1]);
        p.hint(ENABLE_DEPTH_TEST);

        //p.printMatrix();
        PMatrix3D pMatrix = (PMatrix3D)p.getMatrix();
        pMatrix.print();
    }

    private static void drawCoords() {
        p.pushStyle();

        p.stroke(255, 0, 0, 120); // x
        p.line(-width / 2, 0, width / 2, 0);
        p.stroke(0, 255, 0, 120); // y
        p.line(0, -height/2, 0, height/2);
        if (renderer == P3D) {
            p.stroke(0, 0, 255, 120); // z
            p.line(0, 0, -depth / 2, 0, 0, depth / 2);
            p.noFill();
            p.stroke(100); // cube
            p.box(width, height, depth);
        }

        p.popStyle();
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

    // section emit events

    protected static void emitStageEvent() {
        Event evt = null;
        HashMap<String, Object> evtData = null;

        if (!mousePressed && p.mousePressed) {
            mousePressed = true;
            evt = new Event(Event.Name.mousePressed, evtData);
        } else if (mousePressed && !p.mousePressed) {
            mousePressed = false;
            evt = new Event(Event.Name.mouseReleased, evtData);
        } else {
            evt = new Event(Event.Name.mouseMove, evtData);
        }

        emitEvent(evt);
    }

    public static void emitEvent(Event evt) {
        if (evt == null) {
            return;
        }
        for (IStageEventClient d : drawables) {
            Event.Name[] eventNameRegistered = d.registerForEvents();
            for (int i = 0; i < eventNameRegistered.length; i++) {
                if (eventNameRegistered[i] == evt.name) {
                    d.onEvent(evt);
                }
            }
        }
    }




}
