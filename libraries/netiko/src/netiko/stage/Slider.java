package netiko.stage;


import java.util.ArrayList;
import java.util.HashMap;

public class Slider extends PointDraggable {

    private float[] limits = new float[4];
    private boolean drawLimits;
    private float distanceX;
    private float distanceY;
    private float startRangeX;
    private float endRangeX;
    private float rangeX;
    private float startRangeY;
    private float endRangeY;
    private float rangeY;
    private float stepX;
    private float stepY;
    private int stepXCount;
    private int stepYCount;
    private float[] stepXPositions;
    private float[] stepYPositions;

    private float rangeValueX = 0;
    private float rangeValueY = 0;

    Slider(float x, float y, float r, float tlx, float tly, float brx, float bry, boolean drawLimits, float startRangeX, float endRangeX, float startRangeY, float endRangeY, float stepX, float stepY) {
        super(x, y, r);
        limits[0] = tlx; limits[1] = tly; limits[2] = brx; limits[3] = bry;
        this.drawLimits = drawLimits;
        this.distanceX = Math.abs(tlx - brx);
        this.distanceY = Math.abs(tly - bry);
        this.startRangeX = startRangeX;
        this.endRangeX = endRangeX;
        this.rangeX = Math.abs(startRangeX - endRangeX);
        this.startRangeY = startRangeY;
        this.endRangeY = endRangeY;
        this.rangeY = Math.abs(startRangeY - endRangeY);
        this.stepX = stepX;
        this.stepY = stepY;
        if (stepX > 0) {
            stepXCount = (int)(rangeX/stepX);
            stepXPositions = new float[stepXCount+1];
            for (int sx = 0; sx <= stepXCount; sx += 1) {
                stepXPositions[sx] = tlx + distanceX * ((sx*stepX)/rangeX);
            }
        }
        if (stepY > 0) {
            stepYCount = (int)(rangeY/stepY);
            stepYPositions = new float[stepYCount+1];
            for (int sy = 0; sy <= stepYCount; sy += 1) {
                stepYPositions[sy] = bry + distanceY * ((sy*stepY)/rangeY);
            }
        }

        System.out.println(stepXCount + "|" + stepYCount);
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
        float newX = Stage.mouseX;
        float newY = Stage.mouseY;

        if (limits.length == 4) {
            float tlx = limits[0];
            float tly = limits[1];
            float brx = limits[2];
            float bry = limits[3];

            // do some overrides eventually
            if (newX <= tlx) {
                newX = tlx;
            }
            else if  (newX >= brx) {
                newX = brx;
            }
            if (Stage.isCartezian) {
                if (newY >= tly) {
                    newY = tly;
                }
                else if  (newY <= bry) {
                    newY = bry;
                }
            } else {
                if (newY <= tly) {
                    newY = tly;
                }
                else if  (newY >= bry) {
                    newY = bry;
                }
            }
        }
        if (stepX > 0) {
            newX = applySteppingX(newX);
        }
        if (stepY > 0) {
            newY = applySteppingY(newY);
        }
        return new float[] {newX, newY};
    }

    protected float applySteppingX(float newX) {
        //return newX;
//        if (newX < stepXPositions[0]) {
//            System.out.println("<<<<<");
//            return stepXPositions[0];
//        }
//        if (newX > stepXPositions[stepXPositions.length-1]) {
//            System.out.println(">>>>>");
//            return stepXPositions[stepXPositions.length-1];
//        }
        for (int i = 0 ; i < stepXPositions.length; i++) {
            if (newX < stepXPositions[i]) {
                if (Math.abs(stepXPositions[i]-newX) < Math.abs(stepXPositions[i-1]-newX)) {
                    System.out.println(">");
                    newX = stepXPositions[i];
                }
                else {
                    System.out.println("<");
                    newX = stepXPositions[i-1];
                }
                break;
            }
        }
        return newX;
    }
    protected float applySteppingY(float newY) {
        for (int i = 0 ; i < stepYPositions.length; i++) {
            if (newY < stepYPositions[i]) {
                if (Math.abs(stepYPositions[i]-newY) < Math.abs(stepYPositions[i-1]-newY)) {
                    System.out.println(">");
                    newY = stepYPositions[i];
                }
                else {
                    System.out.println("<");
                    newY = stepYPositions[i-1];
                }
                break;
            }
        }
        return newY;
    }

    @Override
    public String toString() { return String.format("Slider: %.2f %.2f", x(), y()); }
}
