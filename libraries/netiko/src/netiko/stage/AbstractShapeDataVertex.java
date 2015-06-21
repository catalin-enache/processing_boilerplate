package netiko.stage;


public abstract class AbstractShapeDataVertex extends AbstractShapeData {
    protected IPoint[] points;

    public IPoint p(int i) {
        return points[i];
    }

    public IPoint[] points() {
        return points;
    }

    @Override
    public String toString() {
        String str = this.getClass().getSimpleName() + ": \n";
        int c = 0;
        for (IPoint p : points) {
            str += "\t" + "p" + c + ": " + p.x() + ", " + p.y() + "\n";
            c++;
        }
        return str;
    }
}
