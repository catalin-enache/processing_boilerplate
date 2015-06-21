package netiko.stage;

import processing.core.*;
import static processing.core.PConstants.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;


/*
TODO: be able to see coords of points, of mouse, constrain drag h|v|rect, make some high level controls (slider)
*/
public class Stage  {

    protected final static float POINT_RADIUS = 3;

    protected static PApplet p;
    protected static String renderer;
    protected static int width;
    protected static int height;
    protected static int bgColor;
    protected static int strokeColor;
    protected static int fillColor;
    protected static int pointColor;
    protected static int pointStrokeColor;
    protected static int pointDraggableHoverColor;
    protected static int boundingRectColor;
    protected static boolean isCartezian;

    protected static Map<Event.Name, HashSet<IStageEventClient>> eventsRegister = new HashMap<>();

    public static float mouseX = 0;
    public static float mouseY = 0;
    public static float prevMouseX = 0;
    public static float prevMouseY = 0;
    public static float distMouseX = 0;
    public static float distMouseY = 0;
    protected static boolean mousePressed = false;
    protected static boolean mouseHoverOn = false;
    protected static Object mouseHoverSetter = null;

    protected static ArrayList<IDrawable> drawables = new ArrayList<>();
    /*
    usage:  Stage.startSetup(this, 600, 600, true);
    */
    public static void startSetup(PApplet _p, int _width, int _height, boolean _isCartezian) {
        p = _p; // PApplet
        renderer = P2D; // what renderer to use

        width = _width; // stage width
        height = _height; // stage height

        bgColor = 0XFFFFFFFF;
        strokeColor = p.color(255, 102, 0);
        fillColor = p.color(0, 100, 0, 100);
        pointColor = 0X99ffffff; // point bgColor
        pointStrokeColor = p.color(100, 100, 100, 100);
        pointDraggableHoverColor = p.color(0, 0, 255, 255);
        boundingRectColor = p.color(0, 0, 255, 255);

        isCartezian = _isCartezian;




        p.size(width, height, renderer);

        start();
    }

    public static void endSetup() { end(); }

    public static void startDraw() { start(); }

    public static void endDraw() { end(); }

    // SECTION create and return objects

    public static PointVirtual pointVirtual(float x, float y) {
        PointVirtual newPoint =  new PointVirtual(x, y);
        return newPoint;
    }

    public static Point point(float x, float y, float r) {
        Point newPoint =  new Point(x, y, r);
        addDrawable(newPoint);
        return newPoint;
    }

    public static Point point(float x, float y) {
        return point(x, y, POINT_RADIUS);
    }

    public static PointDraggable pointDraggable(float x, float y, float r) {
        PointDraggable newPointDraggable =  new PointDraggable(x, y, r);
        addDrawable(newPointDraggable);
        return newPointDraggable;
    }

    public static PointDraggable pointDraggable(float x, float y) {
        return pointDraggable(x, y, POINT_RADIUS);
    }

    // s1 = Stage.shape("myShape", bgColor(150, 100), bgColor(100, 0, 0), null, null, s1_data);
    public static Shape shape(String name, int bgColor, int sColor, Integer beginShape, Integer endShape, ArrayList<AbstractShapeData> shapeData) {
        Shape newShape =  new Shape(name, bgColor, sColor, beginShape, endShape, shapeData);
        addDrawable(newShape);
        return newShape;
    }

    public static ShapeDraggable shapeDraggable(Shape _shape) {
        ShapeDraggable newShapeDraggable =  new ShapeDraggable(_shape);
        addDrawable(newShapeDraggable);
        return newShapeDraggable;
    }

    protected static void addDrawable(IDrawable client) {
        addEventClient(client);
        drawables.add(client);
    }
    /*
    usage:
    Stage.addEventClient(new IStageEventClient(){
    public Event.Name[] registerForEvents() {
      return new Event.Name[]{Event.Name.draggableDragged};
    }
    public void onEvent(Event evt, Object emitter) {
      if (emitter == p1) {
        ((ShapeDataVertex)s1.getData(0)).x(p1.x());
      }
    }
  });
    */
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

    public static int getStrokeColor() { return strokeColor; }

    public static int getFillColor() { return fillColor; }

    public static int getPointColor() { return pointColor; }

    public static int getPointStrokeColor() {return pointStrokeColor; }

    public static int getPointDraggableHoverColor() {return pointDraggableHoverColor; }

    public static int getBoundingRectColor() {return boundingRectColor; }
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
        p.strokeWeight(1);
        p.stroke(strokeColor); // p.stroke(bgColor) seems to help with antialiasing ?
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
        distMouseX = mouseX - prevMouseX;
        distMouseY = mouseY - prevMouseY;
        prevMouseX = mouseX;
        prevMouseY = mouseY;
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
        evtData.put("prevX", prevMouseX);
        evtData.put("prevY", prevMouseY);
        evtData.put("distX", distMouseX);
        evtData.put("distY", distMouseY);

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
