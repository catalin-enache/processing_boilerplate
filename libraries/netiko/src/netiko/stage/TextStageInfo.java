package netiko.stage;


public class TextStageInfo extends Text {

    protected final Event.Name[] eventNamesRegisteredFor = new Event.Name[]{Event.Name.mouseMove};

    public TextStageInfo (String text, float x, float y, float size) {
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

    @Override
    public Event.Name[] registerForEvents() {
        return eventNamesRegisteredFor;
    }

    @Override
    public void onEvent(Event evt, Object emitter) {
        text = String.format("x: %.0f, y: %.0f", evt.data.get("x"), evt.data.get("y"));
    }
}
