
import java.util.ArrayList;

import netiko.math.IPoint;
import netiko.math.*;
import netiko.stage.*;

import netiko.stage.IStageEventClient;
import netiko.stage.Event;

int w = 600;
int h = 600;
boolean isCartesian = true;

PointDraggable p0, p1, p2, p3, p4;


void setup() {
  Stage.startSetup(this, w, h, isCartesian);

  // hook into events stream
  Stage.addEventClient(new IStageEventClient(){
    public Event.Name[] registerForEvents() {
      return new Event.Name[]{Event.Name.draggableDragged};
    }
    public void onEvent(Event evt, Object emitter) {
      if (emitter == p0) {
        boolean testLine1 = Util.intersectionPointLine(p0, p1, p2, true, 1);
        boolean testLine2 = Util.intersectionPointLine(p0, p3, p4, true, 1);

        if (testLine1 || testLine2) {
          Stage.textUserInfo.text("hit");
          p0.setBgColor(color(255,0,0,255));
        } else {
          Stage.textUserInfo.text("");
          p0.resetBgColor();
        }
        
      }  
    }
  });
  
  p0 = Stage.pointDraggable(0, 0, 8);
  p1 = Stage.pointDraggable(-100, -50);
  p2 = Stage.pointDraggable(100, -50);
  p3 = Stage.pointDraggable(-50, -100);
  p4 = Stage.pointDraggable(-50, 100);

  
  Stage.endSetup();
}

void draw() {
  Stage.startDraw();
  // draw lines
  line(p1.x(), p1.y(), p2.x(), p2.y());
  line(p3.x(), p3.y(), p4.x(), p4.y());

  Stage.endDraw();
}

