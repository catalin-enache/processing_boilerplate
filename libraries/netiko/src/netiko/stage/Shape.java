package netiko.stage;

import processing.core.*;
import java.util.ArrayList;

public class Shape implements IDrawable {

    protected ArrayList<ShapeData> instructions;
    protected PApplet p = Stage.getPApplet();
    protected String renderer = Stage.getRenderer();
    protected int sColor;
    protected int bgColor;
    protected Integer beginShape = null;
    protected Integer endShape = null;
    protected boolean showPoints = false;
    protected final float POINT_RADIUS = 3;
    protected ArrayList<ActionStore> shapesActionsStore = new ArrayList<>();
    protected ArrayList<PointDraggable> points = new ArrayList<>();
    protected final Event.Name[] eventNamesRegisteredFor = new Event.Name[]{ Event.Name.pointUpdated };

    /*
    usage:
    Shape s1;
    ArrayList<ShapeData> s1_data = new ArrayList();
    s1_data.add(new ShapeDataVertex(0, 0, 0));
    s1_data.add(new ShapeDataVertex(50, 50, 0));
    s1_data.add(new ShapeDataVertex(0, 100, 0));
    s1 = Stage.shape(color(150), color(100, 0, 0), null, null, true,  s1_data);
    */
    Shape(int bgColor, int sColor, Integer beginShape, Integer endShape, boolean showPoints, ArrayList<ShapeData> instructions) {
        this.sColor = sColor;
        this.bgColor = bgColor;
        this.beginShape = beginShape;
        this.endShape =  endShape;
        this.showPoints = showPoints;
        this.instructions = instructions;
        prepare();
    }

    @Override
    public void draw() {
        p.pushStyle();
        p.pushMatrix();

        p.stroke(sColor);
        p.fill(bgColor);

        if (beginShape != null) {
            p.beginShape(beginShape);
        } else {
            p.beginShape();
        }

        for (ActionStore action : shapesActionsStore) {
            action.run();
        }

        if (endShape != null) {
            p.endShape(endShape);
        } else {
            p.endShape();
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
        if (evt.name == Event.Name.pointUpdated) {
            PointDraggable whichPoint = (PointDraggable)evt.data.get("point");
            int pointsNum = points.size();
            for  (int i = 0; i < pointsNum; i++) {
                if (points.get(i) == whichPoint) {
                    ShapeData instruction = instructions.get(i);
                    if (instruction instanceof ShapeDataVertex) {
                        float[] coords = ((ShapeDataVertex)instruction).coords;
                        updateVertexCoords(whichPoint, coords);
                    }
                }
            }
        }
    }

    protected void prepare() {
        for (ShapeData shapeData : instructions) {
            if (shapeData instanceof ShapeDataVertex) {
                registerVertexAction(((ShapeDataVertex) shapeData).coords);
            }
        }
    }

    protected void updateVertexCoords(PointDraggable point, float[] coords) {
        switch (coords.length) {
            case 2:
            case 3:
            case 4:
            case 5:
                coords[0] = point.point.x;
                coords[1] = point.point.y;
                break;
            default:
                break;
        }
    }

    protected void registerVertexAction(final float[] coords) {
        switch (coords.length) {
            case 1:
                shapesActionsStore.add(new ActionStore() {void run() {p.vertex(coords);}});
                // ?
                break;
            case 2:
                shapesActionsStore.add(new ActionStore() {void run() {p.vertex(coords[0], coords[1]);}});
                if (showPoints) { points.add(Stage.pointDraggable(coords[0], coords[1], 0, POINT_RADIUS)); }
                break;
            case 3:
                shapesActionsStore.add(new ActionStore() {void run() {p.vertex(coords[0], coords[1], coords[2]);}});
                if (showPoints) { points.add(Stage.pointDraggable(coords[0], coords[1], coords[2], POINT_RADIUS)); }
                break;
            case 4:
                shapesActionsStore.add(new ActionStore() {void run() {p.vertex(coords[0], coords[1], coords[2], coords[3]);}});
                if (showPoints) { points.add(Stage.pointDraggable(coords[0], coords[1], 0, POINT_RADIUS)); }
                break;
            case 5:
                shapesActionsStore.add(new ActionStore() {void run() {p.vertex(coords[0], coords[1], coords[2], coords[3], coords[4]);}});
                if (showPoints) { points.add(Stage.pointDraggable(coords[0], coords[1], coords[2], POINT_RADIUS)); }
                break;
        }
    }

}
