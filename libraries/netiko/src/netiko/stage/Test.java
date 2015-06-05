package netiko.stage;

import processing.core.*;

public class Test {

    PApplet p;

    public Test(PApplet _p) {
        p = _p;
    }

    public String test () {
        return "test";
    }
    public void print () {
        System.out.println("print test");
        p.line(p.mouseX, p.mouseY, p.pmouseX, p.pmouseY);
    }
}
