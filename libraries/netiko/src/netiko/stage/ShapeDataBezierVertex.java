package netiko.stage;


public class ShapeDataBezierVertex extends  AbstractShapeDataVertex{

    public ShapeDataBezierVertex(IPoint p2, IPoint p3, IPoint p4) {
        points = new IPoint[]{p2, p3, p4};
    }
}
