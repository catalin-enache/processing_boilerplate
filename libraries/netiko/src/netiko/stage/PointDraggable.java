package netiko.stage;

import java.util.HashMap;

import static processing.core.PConstants.*;

// this class is a wrapper/proxy over a Point
public final class PointDraggable extends AbstractDraggable implements IPoint, IRadius {

    private Point point;

    PointDraggable(float x, float y, float r) {
        super(new Point(x, y, r));
        point = (Point)drawable; // composition pattern
        setOffsetPointReference(point);
        setFigurePoints(new Point[]{point});
    }

    PointDraggable(Point _point) {
        super(_point);
        point = (Point)drawable; // composition pattern
        setOffsetPointReference(point);
        setFigurePoints(new Point[]{point});
    }

    PointDraggable(float x, float y) {
        this(x, y, Stage.POINT_RADIUS);
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
        Event.Name[] parentEvents =  super.registerForEvents();
        Event.Name[] eventNamesRegisteredFor = new Event.Name[parentEvents.length + 1];
        System.arraycopy(parentEvents, 0, eventNamesRegisteredFor, 0, parentEvents.length);
        eventNamesRegisteredFor[parentEvents.length] = Event.Name.pointUpdated;
        return eventNamesRegisteredFor;
    }

    @Override
    public void onEvent(Event evt, Object emitter) {
        super.onEvent(evt, emitter); // allow parent to intercept events it is interested in, eventually handle the drag and emit draggableDragged. In parent process our point will be updated and thus will make it emit pointUpdated event.
        // if evt is pointUpdated and emitter is our point, ?intercept? it, wrap it and forward it as own event. Shape class needs us to emit pointUpdated as it has been our event.
        // actually we are not really intercept pointUpdated event which was emitted by our point. But we are listening to it and emit another duplicate event, only it is ours.
        if (evt.name == Event.Name.pointUpdated) {
            if (emitter == point) { // ! important or else stack overflow due to most of events are emitted by Stage (anyways we are only interested just in events emitted by our point)
                HashMap<String, Object> eventData = new HashMap<>();
                Event selfEvent = new Event(Event.Name.pointUpdated, eventData);
                Stage.emitEvent(selfEvent, this);
            }
        }
    }

    @Override
    public String toString() {
        return String.format("PointDraggable: %.2f %.2f", x(), y());
    }

    @Override
    public float x() {
        return point.x();
    }

    @Override
    public void x(float x) {
        point.x(x);
    }

    @Override
    public float y() {
        return point.y();
    }

    @Override
    public void y(float y) {
        point.y(y);
    }

    @Override
    public float[] xy() {
        return new float[]{point.x(), point.y()};
    }

    @Override
    public void xy(float x, float y) {
        point.x(x);
        point.y(y);
    }

    @Override
    public float r() {
        return point.r();
    }

    @Override
    public void r(float r) {
        point.r(r);
    }
}
