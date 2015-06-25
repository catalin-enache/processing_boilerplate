package netiko.stage;


import java.util.ArrayList;
import java.util.HashMap;

public class Slider extends PointDraggable {

    private float[] limits = new float[4];
    private boolean drawLimits = false;
    private float startRangeX = -1;
    private float endRangeX = 1;
    private float startRangeY = -1;
    private float endRangeY = 1;
    private float stepX = 0F;
    private float stepY = 0F;

    private float rangeValueX = 0;
    private float rangeValueY = 0;

    Slider(float x, float y, float r, float tlx, float tly, float brx, float bry, boolean drawLimits, float startRangeX, float endRangeX, float startRangeY, float endRangeY, float stepX, float stepY) {
        super(x, y, r);
        limits[0] = tlx; limits[1] = tly; limits[2] = brx; limits[3] = bry;
        this.drawLimits = drawLimits;
        this.startRangeX = startRangeX;
        this.endRangeX = endRangeX;
        this.startRangeY = startRangeY;
        this.endRangeY = endRangeY;
        this.stepX = stepX;
        this.stepY = stepY;
        if (drawLimits) {
            drawLimits();
        }
    }

    protected void drawLimits() {
        p.pushStyle();
        // tlx, tly, brx, bry
        ArrayList<AbstractShapeData> shapeData = new ArrayList<>();
        shapeData.add(new ShapeDataVertex(Stage.pointVirtual(limits[0], limits[1])));
        shapeData.add(new ShapeDataVertex(Stage.pointVirtual(limits[2], limits[1])));
        shapeData.add(new ShapeDataVertex(Stage.pointVirtual(limits[2], limits[3])));
        shapeData.add(new ShapeDataVertex(Stage.pointVirtual(limits[0], limits[3])));
        Stage.shape("", Stage.getPointDraggableLimitsBgColor(), Stage.getPointDraggableLimitsStrokeColor(), null, p.CLOSE, shapeData);
        p.popStyle();
    }

    @Override
    protected void processPointUpdatedEvent(Event evt, Object emitter) {
        rangeValueX = p.map(point.x(), limits[0], limits[2], startRangeX, endRangeX);
        rangeValueY = p.map(point.y(), limits[1], limits[3], startRangeY, endRangeY);
        if (Float.isNaN(rangeValueX)) { rangeValueX = 0; }
        if (Float.isNaN(rangeValueY)) { rangeValueY = 0; }
        System.out.println(rangeValueX + " | " + rangeValueY);
        super.processPointUpdatedEvent(evt, emitter);
    }

    @Override
    protected float[] getNewPosition(IPoint point) {
        float mx = Stage.mouseX;
        float my = Stage.mouseY;

        if (limits.length == 4) {
            float tlx = limits[0];
            float tly = limits[1];
            float brx = limits[2];
            float bry = limits[3];

            // do some overrides eventually
            if (mx <= tlx) {
                mx = tlx;
            }
            else if  (mx >= brx) {
                mx = brx;
            }
            if (Stage.isCartezian) {
                if (my >= tly) {
                    my = tly;
                }
                else if  (my <= bry) {
                    my = bry;
                }
            } else {
                if (my <= tly) {
                    my = tly;
                }
                else if  (my >= bry) {
                    my = bry;
                }
            }
        }
        if (stepX > 0) {
            mx = applySteppingX(mx);
        }
        if (stepY > 0) {
            my = applySteppingY(my);
        }
        return new float[] {mx, my};
    }

    protected float applySteppingX(float mx) {
        return mx;
    }
    protected float applySteppingY(float my) {
        return my;
    }

    @Override
    public String toString() { return String.format("Slider: %.2f %.2f", x(), y()); }
}
