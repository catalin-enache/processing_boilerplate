package netiko.stage;

import processing.core.*;
import static processing.core.PConstants.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

// TODO: create input text, why Stage.textUserInfo is rendered twice ? 
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
    protected static int pointDraggableLimitsStrokeColor;
    protected static int pointDraggableLimitsBgColor;
    protected static int boundingRectColor;
    protected static int textColor;
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
    protected static ArrayList<IDrawable> stageWidgets = new ArrayList<>();
    public static TextUserInfo textUserInfo;

    protected static PFont fA12;
    protected static PFont fA8;
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
        fillColor = p.color(0, 100, 0, 70);
        pointColor = 0X99ffffff; // point bgColor
        pointStrokeColor = p.color(100, 100, 100, 100);
        pointDraggableHoverColor = p.color(0, 0, 255, 100);
        pointDraggableLimitsStrokeColor = p.color(0, 100, 0, 100);
        pointDraggableLimitsBgColor = p.color(0, 100, 0, 30);
        boundingRectColor = p.color(0, 0, 255, 255);
        textColor = p.color(0, 0, 0, 255);

        isCartezian = _isCartezian;

        fA12 = p.createFont("Arial", 12, true);
        fA8 = p.createFont("Arial", 8, true);

        addStageWidget(new TextStageInfo("stageInfo", 2, height - 4, 12));
        // default user info (user can update its text data)
        textUserInfo = new TextUserInfo("userInfo", 2, 10, 12);
        addStageWidget(textUserInfo);

        p.size(width, height, renderer);
        p.textFont(fA12, 12);

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

    /*
    usage:
    sl1 = Stage.slider(0, 0, 8, -100, 100, 100, -100, true, -10, 10, -5, 5, 1, 1);
    sl2 = Stage.slider(0, -200, 8, -100, -200, 100, -200, true, -10, 10, 0, 0, 1, 1);
    */
    public static Slider slider(float x, float y, float r, int tlx, int tly, int brx, int bry, boolean drawLimits, int startRangeX, int endRangeX, int startRangeY, int endRangeY, float stepX, float stepY) {
        Slider newSlider =  new Slider(x, y, r, tlx, tly, brx, bry, drawLimits, startRangeX, endRangeX, startRangeY, endRangeY, stepX, stepY);
        addDrawable(newSlider);
        return newSlider;
    }

    /*
    usage:
      ArrayList<AbstractShapeData> s1_data = new ArrayList();
      s1_data.add(new ShapeDataVertex(Stage.point(0, 0)));
      s1_data.add(new ShapeDataVertex(Stage.pointVirtual(50, 50)));
      s1_data.add(new ShapeDataVertex(Stage.pointDraggable(0, 100)));
      s1_data.add(new ShapeDataBezierVertex(Stage.pointDraggable(-50, 100), Stage.pointDraggable(-50, 150), Stage.pointDraggable(0, 150)));
      s1_data.add(new ShapeDataQuadraticVertex(Stage.pointDraggable(50, 200), Stage.pointDraggable(0, 250)));
      s1 = Stage.shape("myShape", color(150, 100, 0 , 50), color(100, 0, 0, 70), null, null, s1_data);
    */
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

    protected static void addStageWidget(IDrawable client) {
        addEventClient(client);
        stageWidgets.add(client);
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
        ((AbstractShapeDataVertex)s1.getData(4)).p(0).x(p1.x());
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

    public static int getPointDraggableHoverColor() { return pointDraggableHoverColor; }

    public static int getPointDraggableLimitsStrokeColor() { return pointDraggableLimitsStrokeColor; }

    public static int getPointDraggableLimitsBgColor() { return pointDraggableLimitsBgColor; }

    public static int getBoundingRectColor() {return boundingRectColor; }

    public static int getTextColor() {return textColor; }

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

        if (isCartezian) { // reset cartesian translation before drawing stage wigets
            p.translate(-width/2, -height/2);
        }

        drawStageWidgets();
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

    private static void drawStageWidgets() {
        for (IDrawable d : stageWidgets) {
            d.draw();
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
