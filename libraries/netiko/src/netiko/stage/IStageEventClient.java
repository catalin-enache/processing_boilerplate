package netiko.stage;

public interface IStageEventClient {
    public Event.Name[] registerForEvents();
    public void onEvent(Event evt);
}
