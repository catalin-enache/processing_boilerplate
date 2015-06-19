package netiko.stage;


import processing.core.PApplet;

import static processing.core.PConstants.*;

public abstract class AbstractDraggable implements IPointInFigure, IDrawable {

    PApplet p = Stage.getPApplet();
    protected boolean dragStarted = false;
    protected float[] offset = new float[]{0, 0};

    IDrawable drawable;
    protected Point offsetPointReference;
    protected Point[] figurePoints;

    // registers for stage events
    protected final Event.Name[] eventNamesRegisteredFor = new Event.Name[]{ Event.Name.mousePressed, Event.Name.mouseReleased, Event.Name.mouseMove };

    AbstractDraggable(IDrawable _drawable) {
        drawable = _drawable;
    }

    protected void setOffsetPointReference(Point point) {
        offsetPointReference = point;
    }

    protected void setFigurePoints(Point[] points) {
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
        } else {
            Stage.setHoverState(false, this);
        }
    }

    protected void drag() {
        if (dragStarted) {
            followNewDirection(Stage.mouseX, Stage.mouseY);
        }
    }

    protected float[] getOffset(float mx, float my) {
        return new float[]{offsetPointReference.x - mx, offsetPointReference.y - my};
    }

    protected void followNewDirection(float x, float y) {
        for (Point p : figurePoints) {
            p.x = x + offset[0];
            p.y = y + offset[1];
            Stage.emitEvent(new Event(Event.Name.pointUpdated, null), this);
        }
    }

}
