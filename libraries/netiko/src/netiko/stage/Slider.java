package netiko.stage;


public class Slider extends PointDraggable {

    private float[] limits = new float[4];
    private boolean drawLimits;
    private int distanceX;
    private int distanceY;
    private int startRangeX;
    private int endRangeX;
    private int rangeX;
    private int startRangeY;
    private int endRangeY;
    private int rangeY;
    private float stepX;
    private float stepY;
    private int stepXCount;
    private int stepYCount;
    private float[] stepXPositions;
    private float[] stepYPositions;

    private float rangeValueX = 0;
    private float rangeValueY = 0;

    Slider(float x, float y, float r, int tlx, int tly, int brx, int bry, boolean drawLimits, int startRangeX, int endRangeX, int startRangeY, int endRangeY, float stepX, float stepY) {
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
        setRangeValueXY();
        init();
    }

    protected void setRangeValueXY() {
        rangeValueX = p.map(point.x(), limits[0], limits[2], startRangeX, endRangeX);
        rangeValueY = p.map(point.y(), limits[1], limits[3], startRangeY, endRangeY);
        if (Float.isNaN(rangeValueX)) { rangeValueX = 0; }
        if (Float.isNaN(rangeValueY)) { rangeValueY = 0; }
    }

    // to be called whenever limits is updated (when Slider is part of a Shape controls and the Shape is dragged)
    protected void init() {
        if (stepX > 0) {
            stepXCount = (int)(rangeX/stepX);
            stepXPositions = new float[stepXCount+1];
            for (int sx = 0; sx <= stepXCount; sx += 1) {
                stepXPositions[sx] = limits[0] + distanceX * ((sx*stepX)/(float)rangeX); // tlx == limits[0]
            }
        }

        if (stepY > 0) {
            stepYCount = (int)(rangeY/stepY);
            stepYPositions = new float[stepYCount+1];
            for (int sy = 0; sy <= stepYCount; sy += 1) {
                if (Stage.isCartezian) {
                    stepYPositions[sy] = limits[3] + distanceY * ((sy*stepY)/(float)rangeY); // bry == limits[3]
                } else {
                    stepYPositions[sy] = limits[1] + distanceY * ((sy*stepY)/(float)rangeY); // tly == limits[1]
                }
            }
        }
    }

    @Override
    public void draw() {
        super.draw();
        if (drawLimits) {
            drawLimits();
        }
    }

    protected void drawLimits() {
        p.pushStyle();
        p.fill(Stage.getPointDraggableLimitsBgColor()); // Stage.getPointDraggableLimitsBgColor()
        p.stroke(Stage.getPointDraggableLimitsStrokeColor());
        p.beginShape();
        p.vertex(limits[0], limits[1], limits[2], limits[1]);
        p.vertex(limits[2], limits[1], limits[2], limits[3]);
        p.vertex(limits[2], limits[3], limits[0], limits[3]);
        p.vertex(limits[0], limits[3], limits[0], limits[1]);
        p.endShape(p.CLOSE);
        p.popStyle();
    }

    @Override
    protected void processPointUpdatedEvent(Event evt, Object emitter) {
        setRangeValueXY();
        super.processPointUpdatedEvent(evt, emitter); // Shape might be interested
    }

    @Override
    protected float[] getNewPosition(IPoint point) {
        float newX = Stage.mouseX;
        float newY = Stage.mouseY;

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

        if (stepX > 0) {
            newX = applySteppingX(newX);
        }
        if (stepY > 0) {
            newY = applySteppingY(newY);
        }
        return new float[] {newX, newY};
    }

    protected float applySteppingX(float newX) {
        for (int i = 0 ; i < stepXPositions.length; i++) {
            if (newX < stepXPositions[i]) {
                if (Math.abs(stepXPositions[i]-newX) < Math.abs(stepXPositions[i-1]-newX)) {
                    newX = stepXPositions[i];
                }
                else {
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
                    newY = stepYPositions[i];
                }
                else {
                    newY = stepYPositions[i-1];
                }
                break;
            }
        }
        return newY;
    }



    public float rangeX() { return rangeValueX; }
    public float rangeY() { return rangeValueY; }

    @Override
    public void xy(float x, float y) {
        // just to be clear this is called only when Slider is part of a Shape and the Shape is dragged not when the Slider is dragged
        limits[0] += Stage.distMouseX;
        limits[2] += Stage.distMouseX;
        limits[1] += Stage.distMouseY;
        limits[3] += Stage.distMouseY;
        init();
        super.xy(x, y);
    }

    @Override
    public void x(float x) {
        limits[0] += Stage.distMouseX;
        limits[2] += Stage.distMouseX;
        init();
        super.x(x);
    }

    @Override
    public void y(float y) {
        limits[1] += Stage.distMouseY;
        limits[3] += Stage.distMouseY;
        init();
        super.y(y);
    }

    @Override
    public String toString() { return String.format("Slider: x: %.2f, y: %.2f, rangeX: %.2f, rangeY: %.2f", x(), y(), rangeX(), rangeY()); }
}
