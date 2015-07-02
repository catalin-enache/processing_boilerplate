
import java.util.ArrayList;

import netiko.math.*;
import netiko.stage.*;

import netiko.stage.IStageEventClient;
import netiko.stage.Event;


int w = 600;
int h = 600;
boolean isCartesian = true;

PointDraggable P0, P1;

void setup() {
  Stage.startSetup(this, w, h, isCartesian);

  // hook into events stream
  Stage.addEventClient(new IStageEventClient(){
    public Event.Name[] registerForEvents() {
      return new Event.Name[]{Event.Name.draggableDragged};
    }
    public void onEvent(Event evt, Object emitter) {
      if (emitter == P0 || emitter == P1) {
        float m = Util.lineSlopeFromTwoPoints(P0, P1);
        Stage.textUserInfo.text("slope: " + String.format("%.3f", m));
      }
    }
  });
  
  P0 = Stage.pointDraggable(50, 50);
  P1 = Stage.pointDraggable(0, 0);
  
  Stage.endSetup();
}

void draw() {
  Stage.startDraw();
  line(P0.x(), P0.y(), P1.x(), P1.y());
  Stage.endDraw();
}

