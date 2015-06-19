
import java.util.ArrayList;
import netiko.stage.Stage;
import netiko.stage.Point;
import netiko.stage.PointDraggable;
import netiko.stage.Shape;
import netiko.stage.ShapeData;
import netiko.stage.ShapeDataVertex;
import netiko.stage.IStageEventClient;
import netiko.stage.Event;

PointDraggable p1, p2, p3, p4, p5;
Shape s1;

int n = 0;
float rotx = 0;
float roty = 0;

int w = 500;
int h = 500;
int z = 500;


void setup() {
  Stage.startSetup(this, 800, 800, 0XFFFFFFFF, 0X99444444, color(255, 102, 0), true);
  
  p1 = Stage.pointDraggable(0, 0, 10);
  
  final ArrayList<ShapeData> s1_data = new ArrayList();
  s1_data.add(new ShapeDataVertex(p1.point.x, p1.point.y));
  s1_data.add(new ShapeDataVertex(50, 50));
  s1_data.add(new ShapeDataVertex(0, 100));
  s1 = Stage.shape(color(150, 100), color(100, 0), null, null, false,  s1_data);
  
  
  p2 = Stage.pointDraggable(30, 20, 3);
  p3 = Stage.pointDraggable(80, 0, 3);
  p4 = Stage.pointDraggable(80, 75, 3);
  p5 = Stage.pointDraggable(30, 75, 3);
  
  // hook into events stream
  Stage.addEventClient(new IStageEventClient(){
    public Event.Name[] registerForEvents() {
      return new Event.Name[]{Event.Name.pointUpdated};
    }
    public void onEvent(Event evt, Object emitter) {
      if (emitter == p1) {
        //println(s1_data.get(0).coords);
        s1_data.get(0).coords[0] = p1.point.x;
        s1_data.get(0).coords[1] = p1.point.y;
        //println(emitter);
        //p2.point.x = p1.point.x;
      }
    }
  });
  
  Stage.endSetup();
}

void draw() {
  Stage.startDraw();
  
  //n += 1;
  //p1.z = -n;
  //p1.x = n;
  //p1.y = n;
  
  fill(color(100,0,0,100));
  stroke(0);
  beginShape();
  vertex(p2.point.x, p2.point.y);
  bezierVertex(p3.point.x, p3.point.y, p4.point.x, p4.point.y, p5.point.x, p5.point.y);
  endShape();


  Stage.endDraw();
  
}


