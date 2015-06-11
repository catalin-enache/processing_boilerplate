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

    protected void drag() {
        float mxy[] = Stage.getTranslatedMouse();
        if (p.mousePressed && p.mouseButton == LEFT && isPointInFigure(mxy[0], mxy[1]) && !dragStarted) {

            dragStarted = true;
            offset = getOffset(mxy[0], mxy[1]);
            System.out.println(offset[0] + " | " + offset[1]);
        } else if (!p.mousePressed && dragStarted) {
            System.out.println("drag stopped");
            dragStarted = false;
            offset[0] = 0;
            offset[1] = 0;
        }

        if (dragStarted) {
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
        }
    }

}
