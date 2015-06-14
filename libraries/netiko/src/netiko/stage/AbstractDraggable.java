package netiko.stage;


import processing.core.PApplet;

import java.util.HashMap;

import static processing.core.PConstants.*;

public abstract class AbstractDraggable implements IPointInFigure, IDrawable {

    PApplet p = Stage.getPApplet();
    protected boolean dragStarted = false;
    protected float[] offset = new float[]{0, 0};

    IDrawable drawable;
    protected Point offsetPointReference;
    protected Point[] figurePoints;

    protected final Event.Name[] eventNamesRegisteredFor = new Event.Name[]{Event.Name.mousePressed, Event.Name.mouseReleased, Event.Name.mouseMove };

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
    public void onEvent(Event evt) {
        if (evt.name == Event.Name.mousePressed) {
            float mxy[] = Stage.getTranslatedMouse();
            if (p.mouseButton == LEFT && isPointInFigure(mxy[0], mxy[1])) {
                dragStarted = true;
                offset = getOffset(mxy[0], mxy[1]);
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
        float mxy[] = Stage.getTranslatedMouse();
        if (isPointInFigure(mxy[0], mxy[1])) {
            Stage.setHoverState(true, this);
        } else {
            Stage.setHoverState(false, this);
        }
    }

    protected void drag() {
        if (dragStarted) {
            float mxy[] = Stage.getTranslatedMouse();
            followNewDirection(mxy[0], mxy[1]);
        }
    }

    protected float[] getOffset(float mx, float my) {
        return new float[]{offsetPointReference.x - mx, offsetPointReference.y - my};
    }

    protected void followNewDirection(float x, float y) {
        for (Point p : figurePoints) {
            p.x = x + offset[0];
            p.y = y + offset[1];
            HashMap<String, Object> data = new HashMap<>();
            data.put("point", this);
            Stage.emitEvent(new Event(Event.Name.pointUpdated, data));
        }
    }

}
