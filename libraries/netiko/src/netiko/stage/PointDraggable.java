package netiko.stage;


import static processing.core.PConstants.*;


public class PointDraggable extends AbstractDraggable {

    public Point point;

    PointDraggable(float x, float y, float z, float r) {
        super(new Point(x, y, z, r));
        point = (Point)drawable;
        setOffsetPointReference(point);
        setFigurePoints(new Point[]{point});
    }

    @Override
    public boolean isPointInFigure(float mx, float my) {
        float pxrange[] = new float[]{point.x - point.r, point.x + point.r};
        float pyrange[] = new float[]{point.y - point.r, point.y + point.r};
        return pxrange[0] < mx && mx < pxrange[1] && pyrange[0] < my && my < pyrange[1] ;
    }

    @Override
    public String toString() {
        return point.toString();
    }
}
