package netiko.stage;


public class TextStageInfo extends TextUserInfo {

    protected final Event.Name[] eventNamesRegisteredFor = new Event.Name[]{Event.Name.mouseMove};

    public TextStageInfo (String text, float x, float y, float size) {
        super(text, x, y, size);
    }

    @Override
    public Event.Name[] registerForEvents() {
        return eventNamesRegisteredFor;
    }

    @Override
    public void onEvent(Event evt, Object emitter) {
        text = String.format("x: %3.0f, y: %3.0f", evt.data.get("x"), evt.data.get("y"));
    }
}
