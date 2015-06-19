package netiko.stage;

public interface IStageEventClient {
    public Event.Name[] registerForEvents(); // called by Stage when IStageEventClient is added to Stage
    public void onEvent(Event evt, Object emitter);
}
