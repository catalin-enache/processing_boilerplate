package netiko.stage;


public class ShapeDataLine extends  AbstractShapeDataVertex {
    public ShapeDataLine(IPoint p1, IPoint p2) {
        points = new IPoint[]{p1, p2};
    }
}



