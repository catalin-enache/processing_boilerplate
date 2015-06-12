package netiko.stage;

import processing.core.*;

public abstract class ShapeData  {
    protected final PApplet p = Stage.getPApplet();
    protected final String renderer = Stage.getRenderer();
    public abstract String toString();
}
