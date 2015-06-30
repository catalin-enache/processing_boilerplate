package netiko.stage;

import processing.core.*;
import java.util.ArrayList;
import java.util.HashMap;

public class Shape implements IDrawable {

    public String name;
    protected ArrayList<AbstractShapeData> shapeData;
    protected PApplet p = Stage.getPApplet();
    protected int sColor;
    protected int bgColor;
    protected Integer beginShape = null;
    protected Integer endShape = null;
    protected ArrayList<ActionStore> shapesActionsStore = new ArrayList<>();
    protected ArrayList<IPoint> points = new ArrayList<>();
    // registers for self points events
    protected final Event.Name[] eventNamesRegisteredFor = new Event.Name[]{Event.Name.pointUpdated};

    /*
    usage:
    Shape s1;
    final ArrayList<AbstractShapeData> s1_data = new ArrayList();
    s1_data.add(new ShapeDataVertex(Stage.point(0, 0)));
    s1_data.add(new ShapeDataVertex(Stage.pointVirtual(50, 50)));
    s1_data.add(new ShapeDataVertex(Stage.pointDraggable(0, 100)));
    s1_data.add(new ShapeDataBezierVertex(Stage.pointDraggable(-50, 100), Stage.pointDraggable(-50, 150), Stage.pointDraggable(0, 150)));
    s1_data.add(new ShapeDataQuadraticVertex(Stage.pointDraggable(50, 200), Stage.pointDraggable(0, 250)));
    s1 = Stage.shape("myShape", bgColor(150, 100), bgColor(100, 0, 0), null, null, s1_data);
    */
    Shape(String name, int bgColor, int sColor, Integer beginShape, Integer endShape, ArrayList<AbstractShapeData> shapeData) {
        this.name = name;
        this.sColor = sColor;
        this.bgColor = bgColor;
        this.beginShape = beginShape;
        this.endShape =  endShape;
        this.shapeData = shapeData;
        prepare();
    }

    public ArrayList<AbstractShapeData> getShapeData() {
        return shapeData;
    }

    public <T extends AbstractShapeData> T getData(int idx) {
        return (T)(shapeData.get(idx));
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
    public void onEvent(Event evt, Object emitter) {
        // broadcast shapeModified for pointUpdated if point in our points
        if (evt.name == Event.Name.pointUpdated) {
            int pointsNum = points.size();
            for  (int i = 0; i < pointsNum; i++) {
                if (points.get(i) == emitter) {
                    HashMap<String, Object> eventData = new HashMap<>();
                    eventData.put("IPoint", emitter);
                    Event event = new Event(Event.Name.shapeModified, eventData);
                    Stage.emitEvent(event, this);
                }
            }
        }
    }

    protected void prepare() {
        for (AbstractShapeData shapeData : this.shapeData) {
            if (shapeData instanceof ShapeDataVertex) {
                registerVertexAction((ShapeDataVertex) shapeData);
            } else if (shapeData instanceof ShapeDataBezierVertex) {
                registerBezierVertexAction((ShapeDataBezierVertex) shapeData);
            } else if (shapeData instanceof ShapeDataQuadraticVertex) {
                registerQuadraticVertexAction((ShapeDataQuadraticVertex) shapeData);
            } else if (shapeData instanceof ShapeDataLine) {
                registerLineAction((ShapeDataLine) shapeData);
            }else if (shapeData instanceof ShapeContour) {
                registerShapeContourAction((ShapeContour) shapeData);
            }
            if (shapeData instanceof AbstractShapeDataVertex) {
                for(IPoint p : ((AbstractShapeDataVertex) shapeData).points()){
                    points.add(p);
                }
            }
        }
    }

    protected void registerVertexAction(final ShapeDataVertex vertex) {
        int numPoints = vertex.points().length;
        if (numPoints == 1) {
            shapesActionsStore.add(new ActionStore() {void run() {p.vertex(vertex.p(0).x(), vertex.p(0).y());}});
        } else if (numPoints == 2) {
            shapesActionsStore.add(new ActionStore() {void run() {p.vertex(vertex.p(0).x(), vertex.p(0).y(), vertex.p(1).x(), vertex.p(1).y());}});
        }
    }

    protected void registerBezierVertexAction(final ShapeDataBezierVertex vertex) {
        shapesActionsStore.add(new ActionStore() {void run() {p.bezierVertex(vertex.p(0).x(), vertex.p(0).y(), vertex.p(1).x(), vertex.p(1).y(), vertex.p(2).x(), vertex.p(2).y());}});
    }

    protected void registerQuadraticVertexAction(final ShapeDataQuadraticVertex vertex) {
        shapesActionsStore.add(new ActionStore() {void run() {p.quadraticVertex(vertex.p(0).x(), vertex.p(0).y(), vertex.p(1).x(), vertex.p(1).y());}});
    }

    protected void registerLineAction(final ShapeDataLine line) {
        shapesActionsStore.add(new ActionStore() {void run() {
            p.vertex(line.p(0).x(), line.p(0).y());
            p.vertex(line.p(1).x(), line.p(1).y());
            p.vertex(line.p(0).x(), line.p(0).y());
        }});
    }

    protected void registerShapeContourAction(final ShapeContour shapeContour) {
        if (shapeContour.contour.equals(ShapeContour.BEGIN)) {
            shapesActionsStore.add(new ActionStore() {void run() {p.beginContour();}});
        } else if (shapeContour.contour.equals(ShapeContour.END)) {
            shapesActionsStore.add(new ActionStore() {void run() {p.endContour();}});
        }
    }

    @Override
    public String toString() {
        String str = "Shape " + name + ": \n";
        for (IPoint point: points) {
            str += "\t" + point + "\n";
        }
        return str;
    }

}
