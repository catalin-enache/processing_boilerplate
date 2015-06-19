package netiko.stage;

import processing.core.*;

public abstract class ShapeData  {
    public float[] coords;
    protected final PApplet p = Stage.getPApplet();
    public abstract String toString();
}
