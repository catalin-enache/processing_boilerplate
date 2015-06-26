package netiko.stage;


import processing.core.PApplet;

public class Text implements IDrawable, IPoint {
    protected float x, y;
    protected String text;
    protected float size;
    protected int textColor = Stage.getTextColor();
    protected PApplet p = Stage.getPApplet();
    protected final Event.Name[] eventNamesRegisteredFor = new Event.Name[]{};

    public Text (String text, float x, float y, float size) {
        this.text = text;
        this.x = x;
        this.y = y;
        this.size = size;
    }

    @Override
    public void draw() {
        p.pushStyle();
        p.pushMatrix();
        p.fill(textColor);
        p.textSize(size);

        if (Stage.isCartezian) {
            p.scale(1, -1); // flip vertically because Stage will flip back everything when cartesian
        }
        p.text(text, x, y);
        p.popMatrix();
        p.popStyle();
    }

    public String text() {
        return text;
    }

    public void text(String txt) {
        text = txt;
    }

    @Override
    public Event.Name[] registerForEvents() {
        return eventNamesRegisteredFor;
    }

    @Override
    public void onEvent(Event evt, Object emitter) {}

    @Override
    public float x() {
        return x;
    }

    @Override
    public void x(float x) {
        this.x = x;
    }

    @Override
    public float y() {
        return y;
    }

    @Override
    public void y(float y) {
        this.y = y;
    }

    @Override
    public float[] xy() {
        return new float[]{x, y};
    }

    @Override
    public void xy(float x, float y) {
        this.x = x;
        this.y = y;
    }
}
