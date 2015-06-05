package net.netiko;

import processing.core.*;
import netiko.stage.Test;

public class MyTest extends PApplet {

    Test t;

    public static void main(String args[]) {
        // full-screen mode can be activated via parameters to PApplets main method.
        System.out.println("in main");
        PApplet.main(new String[]{"net.netiko.MyTest"});
    }

    public void setup() {
        size(400,400);
        background(0);
        t = new Test(this);
    }

    public void draw() {
        stroke(255);
        if (mousePressed) {
            //line(mouseX,mouseY,pmouseX,pmouseY);
            t.print();
        }
    }
}
