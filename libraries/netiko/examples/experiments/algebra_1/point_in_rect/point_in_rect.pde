
import java.util.ArrayList;

import netiko.math.*;
import netiko.stage.*;

import netiko.stage.IStageEventClient;
import netiko.stage.Event;


int w = 600;
int h = 600;
boolean isCartesian = true;

PointDraggable P0, P1, P2;

void setup() {
  Stage.startSetup(this, w, h, isCartesian);

  // hook into events stream
  Stage.addEventClient(new IStageEventClient(){
    public Event.Name[] registerForEvents() {
      return new Event.Name[]{Event.Name.draggableDragged};
    }
    public void onEvent(Event evt, Object emitter) {
      if (emitter == P0 || emitter == P1 || emitter == P2) {
        boolean y = Util.pointInRectOfTwoPoints(P0, P1, P2);
        Stage.textUserInfo.text(String.format("%s", y));
      }
    }
  });
  
  P0 = Stage.pointDraggable(50, 50);
  P1 = Stage.pointDraggable(0, 0);
  P2 = Stage.pointDraggable(150, 150);
  
  Stage.endSetup();
}

void draw() {
  Stage.startDraw();
  rectMode(CORNERS);
  rect(P1.x(), P1.y(), P2.x(), P2.y());
  Stage.endDraw();
}

