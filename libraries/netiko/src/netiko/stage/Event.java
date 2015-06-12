package netiko.stage;


import java.util.HashMap;
import java.util.Map;

public class Event {
    public Name name;
    public HashMap<String, Object> data;
    public static enum Name { mousePressed, mouseReleased, pointUpdated}
    protected String selfAsString;

    Event(Name name, HashMap<String, Object> data) {
        this.name = name;
        this.data = data;
        selfAsString = buildToString();
    }

    protected String buildToString() {
        String str = name + ": ";
        for (Map.Entry<String, Object> entry : data.entrySet()) {
            str = entry + "\n";
        }
        return str + "\n";
    }

    public String toString() {
        return selfAsString;
    }
}
