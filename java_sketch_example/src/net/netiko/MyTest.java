package net.netiko;

import processing.core.*;


import netiko.stage.*;


public class MyTest extends PApplet {

    int w = 600;
    int h = 600;
    boolean isCartesian = true;

    PointDraggable p1;

    public static void main(String args[]) {
        // full-screen mode can be activated via parameters to PApplets main method.
        PApplet.main(new String[]{"net.netiko.MyTest"});
    }

    public void setup() {
        Stage.startSetup(this, w, h, isCartesian);
        p1 = Stage.pointDraggable(0, 0, 10);

        reactToEvents();

        Stage.endSetup();
    }

    public void draw() {
        Stage.startDraw();

        Stage.endDraw();
    }

    private void reactToEvents () {
        Stage.addEventClient(new IStageEventClient(){
            public Event.Name[] registerForEvents() {
                // mousePressed, mouseReleased, mouseMove, draggableDragged, pointUpdated, shapeModified, draggableSelected
                return new Event.Name[]{
                        Event.Name.mousePressed,
                        Event.Name.mouseReleased,
                        Event.Name.mouseMove,
                        Event.Name.pointUpdated,
                        Event.Name.draggableSelected,
                        Event.Name.draggableDragged,
                        Event.Name.shapeModified
                };
            }
            public void onEvent(Event evt, Object emitter) {
                if (evt.name == Event.Name.mousePressed) {
                    //Stage.textUserInfo.text(evt + emitter.toString());
                } else if (evt.name == Event.Name.mouseReleased) {
                    //Stage.textUserInfo.text(evt + emitter.toString());
                } else if (evt.name == Event.Name.mouseMove) {
                    //Stage.textUserInfo.text(evt + emitter.toString());
                } else if (evt.name == Event.Name.pointUpdated) {
                    //Stage.textUserInfo.text(evt + emitter.toString());
                } else if (evt.name == Event.Name.draggableSelected) {
                    if (emitter != null) {
                        Stage.textUserInfo.text(evt + emitter.toString());
                    } else {
                        Stage.textUserInfo.text(evt + "null");
                    }
                } else if (evt.name == Event.Name.draggableDragged) {
                    //Stage.textUserInfo.text(evt + emitter.toString());
                } else if (evt.name == Event.Name.shapeModified) {
                    //Stage.textUserInfo.text(evt + emitter.toString());
                }
            }
        });
    }
}
