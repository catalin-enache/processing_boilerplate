package netiko.stage;


import processing.core.PApplet;

import static processing.core.PConstants.*;

public abstract class AbstractDraggable implements IPointInFigure, IDrawable {

    PApplet p = Stage.getPApplet();
    protected boolean dragStarted = false;


    IDrawable drawable;
    protected IPoint[] figurePoints;

    // registers for stage events
    protected Event.Name[] eventNamesRegisteredFor = new Event.Name[]{ Event.Name.mousePressed, Event.Name.mouseReleased, Event.Name.mouseMove };

    AbstractDraggable(IDrawable _drawable) {
        drawable = _drawable;
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
        if (evt.name == Event.Name.mousePressed && p.mouseButton == LEFT) {
            if (isPointInFigure(Stage.mouseX, Stage.mouseY)) {
                dragStarted = true;
                if (Stage.getSelectedDraggable() != this) {
                    Stage.setSelectedDraggable(this);
                    Stage.emitEvent(new Event(Event.Name.draggableSelected, null), this);
                }
            }
            else {
                if (Stage.getSelectedDraggable() == this) {
                    Stage.setSelectedDraggable(null);
                    Stage.emitEvent(new Event(Event.Name.draggableSelected, null), null);
                }
            }
        } else if (evt.name == Event.Name.mouseReleased) {
            dragStarted = false;
        }
        else if (evt.name == Event.Name.mouseMove) {
            checkMouseHover();
        }
        checkWasSelected();
    }

    protected void checkMouseHover() {
        if (isPointInFigure(Stage.mouseX, Stage.mouseY)) {
            Stage.setHoverState(true, this);
            if (Stage.getSelectedDraggable() != this) {
                setFocusState(true);
            }
        } else {
            Stage.setHoverState(false, this);
            if (Stage.getSelectedDraggable() != this) {
                setFocusState(false);
            }
        }
    }

    protected void checkWasSelected() {
        if (Stage.getSelectedDraggable() == this) {
            setFocusState(true);
        }
    }

    // most general & simple calculation - a rect around all shape points
    protected IPoint[] boundingRect () {
        if (figurePoints.length == 1) {
            return null; // not interesting in having a rect around a point
        }

        float[] tl = new float[]{Float.POSITIVE_INFINITY, Float.NEGATIVE_INFINITY};
        float[] tr = new float[]{Float.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY};
        float[] br = new float[]{Float.NEGATIVE_INFINITY, Float.POSITIVE_INFINITY};
        float[] bl = new float[]{Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY};

        for (IPoint p : figurePoints) {
            tl[0] = (Math.min(tl[0], p.x()));
            tl[1] = (Math.max(tl[1], p.y()));
            tr[0] = (Math.max(tr[0], p.x()));
            tr[1] = (Math.max(tr[1], p.y()));
            br[0] = (Math.max(br[0], p.x()));
            br[1] = (Math.min(br[1], p.y()));
            bl[0] = (Math.min(bl[0], p.x()));
            bl[1] = (Math.min(bl[1], p.y()));
        }

        return new IPoint[]{new PointVirtual(tl[0], tl[1]), new PointVirtual(tr[0], tr[1]), new PointVirtual(br[0], br[1]), new PointVirtual(bl[0], bl[1])};
    }

    public void setFocusState(boolean focused) {
        if (!focused) { return; }
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
            setNewPosition();
            Stage.emitEvent(new Event(Event.Name.draggableDragged, null), this);
        }
    }

    protected void setNewPosition() {
        for (IPoint p : figurePoints) {
            // simplest approach using mouse distance between previous frame and current frame
            float[] newCoords = getNewPosition(p);
            // (just a clarification)
            // in case of Slider/PointDraggable being dragged: p is the wrapped Point (that was set in setFigurePoints())
            // in case of ShapeDraggable being dragged: p might be PointVirtual, Point, PointDraggable, Slider (whatever was set in setFigurePoints())
            p.xy(newCoords[0], newCoords[1]);
        }
    }

    protected float[] getNewPosition(IPoint point) {
        float newX = point.x() +  Stage.distMouseX;
        float newY = point.y() + Stage.distMouseY;
        return new float[] {newX, newY};
    }

}
