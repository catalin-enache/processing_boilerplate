
import java.util.ArrayList;

import netiko.math.IPoint;
import netiko.math.*;
import netiko.stage.*;

import netiko.stage.IStageEventClient;
import netiko.stage.Event;

int w = 600;
int h = 600;
boolean isCartesian = true;

PointDraggable p1, p2, p3, p4, p5, p6, p7, p8;
PointDraggable[][] lines = new PointDraggable[4][2];

void setup() {
  Stage.startSetup(this, w, h, isCartesian);

  // hook into events stream
  Stage.addEventClient(new IStageEventClient(){
    public Event.Name[] registerForEvents() {
      return new Event.Name[]{Event.Name.draggableDragged};
    }
    public void onEvent(Event evt, Object emitter) {
        //Stage.textUserInfo.text("");
    }
  });
  
  p1 = Stage.pointDraggable(-50, 100);
  p2 = Stage.pointDraggable(-50, -100);
  p3 = Stage.pointDraggable(50, 100);
  p4 = Stage.pointDraggable(50, -100);
  p5 = Stage.pointDraggable(-100, 50);
  p6 = Stage.pointDraggable(100, 50);
  p7 = Stage.pointDraggable(-100, -50);
  p8 = Stage.pointDraggable(100, -50);
  
  lines[0][0] = p1;
  lines[0][1] = p2;
  lines[1][0] = p3;
  lines[1][1] = p4;
  lines[2][0] = p5;
  lines[2][1] = p6;
  lines[3][0] = p7;
  lines[3][1] = p8;
  
  Stage.endSetup();
}

void draw() {
  Stage.startDraw();
  // draw lines
  line(p1.x(), p1.y(), p2.x(), p2.y());
  line(p3.x(), p3.y(), p4.x(), p4.y());
  line(p5.x(), p5.y(), p6.x(), p6.y());
  line(p7.x(), p7.y(), p8.x(), p8.y());
  // draw interesection points
  fill(color(0, 0, 255, 150));
  for (int i = 0; i < lines.length; i++) {
    for (int j = i+1; j < lines.length; j++) {
        IPoint intersection = Util.intersectionLineLine(lines[i][0], lines[i][1], lines[j][0], lines[j][1], true);
        if (intersection != null) {
          ellipse(intersection.x(), intersection.y(), 5, 5);
        }
    }
  }
  
  Stage.endDraw();
}

