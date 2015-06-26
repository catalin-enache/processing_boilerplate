package netiko.stage;


import java.io.Console;
import java.util.ArrayList;

public class ShapeDraggable extends AbstractDraggable {

    private Shape shape;

    ShapeDraggable(Shape _shape) {
        super(_shape);
        shape = (Shape)drawable; // composition pattern
        setOffsetPointReference((IPoint)(shape.points.toArray()[0]));
        setFigurePoints(shape.points.toArray(new IPoint[]{}));
    }

    // proxy/shortcut to shape
    public ArrayList<AbstractShapeData> getShapeData() {
        return shape.shapeData;
    }

    // proxy/shortcut to shape
    public <T extends AbstractShapeData> T getData(int idx) {
        return (T)(shape.shapeData.get(idx));
    }

    @Override
    public boolean isPointInFigure(float mx, float my) {
        // allow draggable points inside shape to modify its data without dragging entire shape
        for (IPoint pt: shape.points) {
            if (pt instanceof PointDraggable) {
                if (((PointDraggable)pt).isPointInFigure(mx, my)) {
                    return false;
                }
            }
        }
        IPoint[] boundingRect = boundingRect();
        IPoint tl = boundingRect[0];
        IPoint br = boundingRect[2];

        return mx > tl.x() && mx < br.x() && my < tl.y() && my > br.y();
    }

    @Override
    public String toString() {
        String str = "ShapeDraggable " + shape + "\n";
        return str;
    }
}
