package netiko.stage;

import static processing.core.PConstants.*;

public class ShapeDataVertex extends ShapeData {

    public ShapeDataVertex(float... coords) {
        this.coords = coords;
    }

    @Override
    public String toString() {
        String str = "ShapeDataVertex:";
        for (int i = 0; i < coords.length; i++) {
            str = str + " " + coords[i];
        }
        return str;
    }
}
