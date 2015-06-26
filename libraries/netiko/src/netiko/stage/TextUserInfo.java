package netiko.stage;



public class TextUserInfo extends Text {

    protected final Event.Name[] eventNamesRegisteredFor = new Event.Name[]{};

    public TextUserInfo (String text, float x, float y, float size) {
        super(text, x, y, size);
    }

    @Override
    public void draw() {
        p.pushStyle();
        p.pushMatrix();
        p.fill(textColor);
        p.textSize(size);
        p.text(text, x, y);
        p.popMatrix();
        p.popStyle();
    }

    public void text(String message) {
        text = message;
    }
    public String text() {
        return text;
    }
}
