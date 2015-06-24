package netiko.stage;


import processing.core.PApplet;

import static processing.core.PConstants.*;

public abstract class AbstractDraggable implements IPointInFigure, IDrawable {

    PApplet p = Stage.getPApplet();
    protected boolean dragStarted = false;
    protected float[] offset = new float[]{0, 0};

    IDrawable drawable;
    protected IPoint offsetPointReference;
    protected IPoint[] figurePoints;

    // registers for stage events
    protected final Event.Name[] eventNamesRegisteredFor = new Event.Name[]{ Event.Name.mousePressed, Event.Name.mouseReleased, Event.Name.mouseMove };

    AbstractDraggable(IDrawable _drawable) {
        drawable = _drawable;
    }

    protected void setOffsetPointReference(IPoint point) {
        offsetPointReference = point;
    }

    protected void setFigurePoints(IPoint[] points) {
        figurePoints = points;
    }

    @Override
    public void draw() {
        drag();
        drawable.draw();
    }

    @Override
    public Event.Name[] registerForEvents() {
        return eventNamesRegisteredFor;
    }

    @Override
    public void onEvent(Event evt, Object emitter) {
        if (evt.name == Event.Name.mousePressed) {
            if (p.mouseButton == LEFT && isPointInFigure(Stage.mouseX, Stage.mouseY)) {
                dragStarted = true;
                offset = getOffset(Stage.mouseX, Stage.mouseY);
            }
        } else if (evt.name == Event.Name.mouseReleased) {
            dragStarted = false;
            offset[0] = 0;
            offset[1] = 0;
        }
        else if (evt.name == Event.Name.mouseMove) {
            checkMouseHover();
        }
    }

    protected void checkMouseHover() {
        if (isPointInFigure(Stage.mouseX, Stage.mouseY)) {
            Stage.setHoverState(true, this);
            drawBoundingRect();
        } else {
            Stage.setHoverState(false, this);
        }
    }

    // most general & simple calculation - a rect around all shape points
    protected IPoint[] boundingRect () {
        if (figurePoints.length == 1) {
            return null; // not interesting in having a rect around a point
        }
        PointVirtual tl = new PointVirtual(Float.POSITIVE_INFINITY, Float.NEGATIVE_INFINITY);
        PointVirtual tr = new PointVirtual(Float.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY);
        PointVirtual br = new PointVirtual(Float.NEGATIVE_INFINITY, Float.POSITIVE_INFINITY);
        PointVirtual bl = new PointVirtual(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY);

        for (IPoint p : figurePoints) {
            tl.x(Math.min(tl.x(), p.x()));
            tl.y(Math.max(tl.y(), p.y()));
            tr.x(Math.max(tr.x(), p.x()));
            tr.y(Math.max(tr.y(), p.y()));
            br.x(Math.max(br.x(), p.x()));
            br.y(Math.min(br.y(), p.y()));
            bl.x(Math.min(bl.x(), p.x()));
            bl.y(Math.min(bl.y(), p.y()));
        }

        return new IPoint[]{tl, tr, br, bl};
    }

    public void drawBoundingRect() {
        IPoint[] boundingRect = boundingRect();
        if (boundingRect == null) { return; }
        IPoint tl = boundingRect[0];
        IPoint tr = boundingRect[1];
        IPoint br = boundingRect[2];
        IPoint bl = boundingRect[3];
        p.pushStyle();
        p.stroke(Stage.getBoundingRectColor());
        p.line(tl.x(), tl.y(), tr.x(), tr.y());
        p.line(tr.x(), tr.y(), br.x(), br.y());
        p.line(br.x(), br.y(), bl.x(), bl.y());
        p.line(bl.x(), bl.y(), tl.x(), tl.y());
        p.pushStyle();
    }

    protected void drag() {
        if (dragStarted) {
            followNewDirection();
            Stage.emitEvent(new Event(Event.Name.draggableDragged, null), this);
        }
    }

    protected float[] getOffset(float mx, float my) {
        return new float[]{offsetPointReference.x() - mx, offsetPointReference.y() - my};
    }

    protected void followNewDirection() {
        for (IPoint p : figurePoints) {
            // simplest approach using mouse distance between previous frame and current frame
            p.xy(p.x() + Stage.distMouseX, p.y() + Stage.distMouseY);
        }
    }

}
