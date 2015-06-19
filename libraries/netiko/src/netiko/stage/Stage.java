package netiko.stage;

import processing.core.*;
import static processing.core.PConstants.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class Stage  {

    protected static PApplet p;
    protected static String renderer;
    protected static int width;
    protected static int height;
    protected static int bgColor = -1;
    protected static int pColor = -1;
    protected static int sColor = -1;
    protected static boolean isCartezian;

    protected static Map<Event.Name, HashSet<IStageEventClient>> eventsRegister = new HashMap<>();

    public static float mouseX = 0;
    public static float mouseY = 0;
    protected static boolean mousePressed = false;
    protected static boolean mouseHoverOn = false;
    protected static Object mouseHoverSetter = null;

    protected static ArrayList<IDrawable> drawables = new ArrayList<>();
    /*
    usage:  Stage.startSetup(this, 800, 800, 0XFFFFFFFF, 0XFF444444, color(255, 102, 0), true);
    */
    public static void startSetup(PApplet _p, int _width, int _height, int _bgColor, int _pColor, int _sColor, boolean _isCartezian) {
        p = _p; // PApplet
        renderer = P2D; // what renderer to use

        width = _width; // stage width
        height = _height; // stage height

        bgColor = _bgColor; // background color
        pColor = _pColor; // point color
        sColor = _sColor; // stroke color

        isCartezian = _isCartezian;

        p.size(width, height, renderer);

        start();
    }

    public static void endSetup() { end(); }

    public static void startDraw() { start(); }

    public static void endDraw() { end(); }

    // SECTION create and return objects

    public static Point point(float x, float y, float r) {
        Point newPoint =  new Point(x, y, r);
        addDrawable(newPoint);
        return newPoint;
    }

    public static PointDraggable pointDraggable(float x, float y, float r) {
        PointDraggable newPointDraggable =  new PointDraggable(x, y, r);
        addDrawable(newPointDraggable);
        return newPointDraggable;
    }

    public static Shape shape(int bgColor, int sColor, Integer beginShape, Integer endShape, boolean showPoints, ArrayList<ShapeData> shapeData) {
        Shape newShape =  new Shape(bgColor, sColor, beginShape, endShape, showPoints, shapeData);
        addDrawable(newShape);
        return newShape;
    }

    protected static void addDrawable(IDrawable client) {
        addEventClient(client);
        drawables.add(client);
    }

    public static void addEventClient(IStageEventClient client) {
        for (Event.Name evtName: client.registerForEvents()) {
            if (eventsRegister.containsKey(evtName)) {
                eventsRegister.get(evtName).add(client);
            } else {
                HashSet<IStageEventClient> newSet = new HashSet<>();
                newSet.add(client);
                eventsRegister.put(evtName, newSet);
            }
        }
    }

    // SECTION get stuff

    public static PApplet getPApplet() { return p; }

    public static int getBgColor() { return bgColor; }

    public static int getPointColor() { return pColor; }

    public static int getStrokeColor() { return sColor; }

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

    private static void start() {
        p.smooth(8);
        p.stroke(bgColor); // seems to help with antialiasing
        p.strokeWeight(1);
        p.background(bgColor);

        if (isCartezian) {
            p.translate(width/2, height/2);
            drawCoords();
        }
        setMouseCoords();
        emitStageEvent();
        // let the rest of objects draw their stuff in this new environment
    }

    private static void end() {
        for (IDrawable d : drawables) {
            d.draw();
        }

        if (isCartezian) {
            reverseSceneY();
        }
    }

    private static void setMouseCoords() {
        if (isCartezian) {
            mouseX = p.mouseX - width/2;
            mouseY = -(p.mouseY - height/2);
        } else {
            mouseX = p.mouseX;
            mouseY = p.mouseY;
        }
    }

    private static void drawCoords() {
        p.pushStyle();

        p.stroke(255, 0, 0, 120); // x
        p.line(-width / 2, 0, width / 2, 0);
        p.stroke(0, 255, 0, 120); // y
        p.line(0, -height/2, 0, height/2);

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
        HashMap<String, Object> evtData = new HashMap<>();
        evtData.put("x", mouseX);
        evtData.put("y", mouseY);

        if (!mousePressed && p.mousePressed) {
            mousePressed = true;
            evt = new Event(Event.Name.mousePressed, evtData);
        } else if (mousePressed && !p.mousePressed) {
            mousePressed = false;
            evt = new Event(Event.Name.mouseReleased, evtData);
        } else {
            evt = new Event(Event.Name.mouseMove, evtData);
        }

        emitEvent(evt, Stage.class);
    }

    public static void emitEvent(Event evt, Object emitter) {
        if (evt == null) {
            return;
        }

        for (Map.Entry<Event.Name, HashSet<IStageEventClient>> entry : eventsRegister.entrySet()) {
            Event.Name evtName = entry.getKey();
            HashSet<IStageEventClient> clients = entry.getValue();
            if (evt.name == evtName) {
                for (IStageEventClient client: clients) {
                    client.onEvent(evt, emitter);
                }
            }
        }

    }




}
