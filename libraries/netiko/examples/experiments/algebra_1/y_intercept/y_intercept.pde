
import java.util.ArrayList;

import netiko.math.*;
import netiko.stage.*;

import netiko.stage.IStageEventClient;
import netiko.stage.Event;


int w = 600;
int h = 600;
boolean isCartesian = true;

PointDraggable P0, P1;
Point P2;

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
        float b = Util.lineYInterceptPointSlope(P0, m);
        //float b = Util.lineYInterceptPointSlope(P1, m);
        P2.y(b);
        Stage.textUserInfo.text("b: " + String.format("%.3f", b));
      }
    }
  });
  
  P0 = Stage.pointDraggable(50, 50);
  P1 = Stage.pointDraggable(-60, 60);
  float m = Util.lineSlopeFromTwoPoints(P0, P1);
  float b = Util.lineYInterceptPointSlope(P0, m);
  P2 = Stage.point(0, b);
  
  Stage.endSetup();
}

void draw() {
  Stage.startDraw();
  line(P0.x(), P0.y(), P1.x(), P1.y());
  Stage.endDraw();
}

