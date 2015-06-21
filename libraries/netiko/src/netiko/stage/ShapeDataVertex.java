package netiko.stage;

public class ShapeDataVertex extends AbstractShapeDataVertex {

    public ShapeDataVertex(IPoint pointCoord, IPoint pointTexture) {
        if (pointTexture != null) {
            points = new IPoint[]{pointCoord, pointTexture};
        } else {
            points = new IPoint[]{pointCoord};
        }
    }

    public ShapeDataVertex(IPoint pointCoord) {
        this(pointCoord, null);
    }


}
