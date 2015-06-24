package netiko.stage;

import java.util.ArrayList;
import java.util.HashMap;

import static processing.core.PConstants.*;

// this class is a wrapper/proxy over a Point
public final class PointDraggable extends AbstractDraggable implements IPoint, IRadius {

    private Point point;
    private float[] limits = new float[0];
    private boolean drawLimits = false;
    private float startRangeX = -1;
    private float endRangeX = 1;
    private float startRangeY = -1;
    private float endRangeY = 1;
    private float step = 0.1F;

    private float rangeValueX = 0;
    private float rangeValueY = 0;

    PointDraggable(float x, float y, float r) {
        super(new Point(x, y, r));
        point = (Point)drawable; // composition pattern
        setOffsetPointReference(point);
        setFigurePoints(new Point[]{point});
    }

    PointDraggable(float x, float y) {
        this(x, y, Stage.POINT_RADIUS);
    }

    PointDraggable(float x, float y, float r, float tlx, float tly, float brx, float bry, boolean drawLimits, float startRangeX, float endRangeX, float startRangeY, float endRangeY, float step) {
        this(x, y, r);
        limits = new float[]{tlx, tly, brx, bry};
        this.drawLimits = drawLimits;
        this.startRangeX = startRangeX;
        this.endRangeX = endRangeX;
        this.startRangeY = startRangeY;
        this.endRangeY = endRangeY;
        this.step = step;
        if (drawLimits) {
            drawLimits();
        }
    }

    @Override
    public boolean isPointInFigure(float mx, float my) {
        float pxrange[] = new float[]{point.x - point.r, point.x + point.r};
        float pyrange[] = new float[]{point.y - point.r, point.y + point.r};
        boolean yes = pxrange[0] < mx && mx < pxrange[1] && pyrange[0] < my && my < pyrange[1] ;
        if (yes) {
            point.setBgColor(Stage.getPointDraggableHoverColor());
        } else {
            point.resetBgColor();
        }
        return yes;
    }

    @Override
    public Event.Name[] registerForEvents() {
        // add pointUpdated (emitted by our point) to the events super registered for
        Event.Name[] parentEvents =  super.registerForEvents();
        Event.Name[] eventNamesRegisteredFor = new Event.Name[parentEvents.length + 1];
        System.arraycopy(parentEvents, 0, eventNamesRegisteredFor, 0, parentEvents.length);
        eventNamesRegisteredFor[parentEvents.length] = Event.Name.pointUpdated;
        return eventNamesRegisteredFor;
    }

    @Override
    public void onEvent(Event evt, Object emitter) {
        super.onEvent(evt, emitter); // allow parent to intercept events it is interested in, eventually handle the drag and emit draggableDragged. In parent process our point will be updated and thus will make it emit pointUpdated event.
        // if evt is pointUpdated and emitter is our point, ?intercept? it, wrap it and broadcast it as own event. Shape class needs us to emit pointUpdated as it has been our event.
        // actually we are not really intercept pointUpdated event which was emitted by our point. But we are listening to it and emit another duplicate event, only it is ours.
        if (evt.name == Event.Name.pointUpdated) {
            if (emitter == point) { // ! important or else stack overflow due to most of events are emitted by Stage (anyways we are only interested just in events emitted by our point)
                rangeValueX = p.map(point.x(), limits[0], limits[2], startRangeX, endRangeX);
                rangeValueY = p.map(point.y(), limits[1], limits[3], startRangeY, endRangeY);
                if (Float.isNaN(rangeValueX)) { rangeValueX = 0; }
                if (Float.isNaN(rangeValueY)) { rangeValueY = 0; }
                System.out.println(rangeValueX + " | " + rangeValueY);
                HashMap<String, Object> eventData = new HashMap<>();
                Event selfEvent = new Event(Event.Name.pointUpdated, eventData);
                Stage.emitEvent(selfEvent, this);
            }
        }
        point.onEvent(evt, emitter); // forward all events | actually Point listens for its event to update its text info
    }


    @Override
    protected void followNewDirection() { // based on mouseX, mouseY unlike in super which prefers distMouseX, distMouseY
        float mx = Stage.mouseX;
        float my = Stage.mouseY;
        if (limits.length == 4) {
            float tlx = limits[0];
            float tly = limits[1];
            float brx = limits[2];
            float bry = limits[3];

            // do some overrides eventually
            if (mx <= tlx) {
                mx = tlx;
            }
            else if  (mx >= brx) {
                mx = brx;
            }
            if (Stage.isCartezian) {
                if (my >= tly) {
                    my = tly;
                }
                else if  (my <= bry) {
                    my = bry;
                }
            } else {
                if (my <= tly) {
                    my = tly;
                }
                else if  (my >= bry) {
                    my = bry;
                }
            }
        }
        mx = applySteppingX(mx);
        my = applySteppingY(my);
        point.xy(mx, my);
    }

    protected float applySteppingX(float mx) {
        return mx;
    }
    protected float applySteppingY(float my) {
        return my;
    }

    protected void drawLimits() {
        p.pushStyle();
        // tlx, tly, brx, bry
        ArrayList<AbstractShapeData> shapeData = new ArrayList<>();
        shapeData.add(new ShapeDataVertex(Stage.pointVirtual(limits[0], limits[1])));
        shapeData.add(new ShapeDataVertex(Stage.pointVirtual(limits[2], limits[1])));
        shapeData.add(new ShapeDataVertex(Stage.pointVirtual(limits[2], limits[3])));
        shapeData.add(new ShapeDataVertex(Stage.pointVirtual(limits[0], limits[3])));
        Stage.shape("", Stage.getPointDraggableLimitsBgColor(), Stage.getPointDraggableLimitsStrokeColor(), null, p.CLOSE, shapeData);
        p.popStyle();
    }

    @Override
    public String toString() { return String.format("PointDraggable: %.2f %.2f", x(), y()); }

    @Override
    public float x() { return point.x(); }

    @Override
    public void x(float x) { point.x(x); }

    @Override
    public float y() { return point.y(); }

    @Override
    public void y(float y) { point.y(y); }

    @Override
    public float[] xy() { return new float[]{point.x(), point.y()}; }

    @Override
    public void xy(float x, float y) { point.x(x);point.y(y); }

    @Override
    public float r() { return point.r(); }

    @Override
    public void r(float r) { point.r(r); }
}
