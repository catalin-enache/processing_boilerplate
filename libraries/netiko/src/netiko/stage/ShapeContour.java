package netiko.stage;

public class ShapeContour extends AbstractShapeData {

    protected String contour;
    public static final String BEGIN = "begin";
    public static final String END = "end";
    public ShapeContour(String contour) {
        if (!(contour.contentEquals(BEGIN) || contour.contentEquals(END))) {
            throw new RuntimeException(this.getClass().getSimpleName() + " only accepts begin | end as param");
        }
        this.contour = contour;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + ": " + contour;
    }
}
