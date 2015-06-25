
import java.util.ArrayList;
import netiko.stage.Stage;
import netiko.stage.Point;
import netiko.stage.PointVirtual;
import netiko.stage.PointDraggable;
import netiko.stage.Slider;
import netiko.stage.Shape;
import netiko.stage.ShapeDraggable;
import netiko.stage.AbstractShapeData;
import netiko.stage.AbstractShapeDataVertex;
import netiko.stage.ShapeDataLine;
import netiko.stage.ShapeDataVertex;
import netiko.stage.ShapeDataBezierVertex;
import netiko.stage.ShapeDataQuadraticVertex;
import netiko.stage.ShapeContour;
import netiko.stage.IStageEventClient;
import netiko.stage.Event;

PointDraggable p1, p2, p3, p4, p5;
Shape s1, s2, s3;
ShapeDraggable sd1, sd2, sd3;
Slider sl1, sl2;

int n = 0;


int w = 600;
int h = 600;



void setup() {
  Stage.startSetup(this, w, h, true);
  
  ArrayList<AbstractShapeData> s1_data = new ArrayList();
  s1_data.add(new ShapeDataVertex(Stage.point(0, 0)));
  s1_data.add(new ShapeDataVertex(Stage.pointVirtual(50, 50)));
  s1_data.add(new ShapeDataVertex(Stage.pointDraggable(0, 100)));
  s1_data.add(new ShapeDataBezierVertex(Stage.pointDraggable(-50, 100), Stage.pointDraggable(-50, 150), Stage.pointDraggable(0, 150)));
  s1_data.add(new ShapeDataQuadraticVertex(Stage.pointDraggable(50, 200), Stage.pointDraggable(0, 250)));
  s1 = Stage.shape("myShape", color(150, 100, 0 , 50), color(100, 0, 0, 70), null, null, s1_data);
  
  ArrayList<AbstractShapeData> s2_data = new ArrayList();
  s2_data.add(new ShapeDataVertex(Stage.pointVirtual(-40, -40)));
  s2_data.add(new ShapeDataVertex(Stage.pointVirtual(40, -40)));
  s2_data.add(new ShapeDataVertex(Stage.pointVirtual(40, 40)));
  s2_data.add(new ShapeDataVertex(Stage.pointVirtual(-40, 40)));
  s2_data.add(new ShapeContour(ShapeContour.BEGIN));
  s2_data.add(new ShapeDataVertex(Stage.pointDraggable(-20, -20)));
  s2_data.add(new ShapeDataVertex(Stage.pointDraggable(-20, 20)));
  s2_data.add(new ShapeDataVertex(Stage.pointDraggable(20, 20)));
  s2_data.add(new ShapeDataVertex(Stage.pointDraggable(20, -20)));
  s2_data.add(new ShapeContour(ShapeContour.END));
  s2 = Stage.shape("myShape2", color(150, 100), color(0, 0, 0, 70), null, CLOSE, s2_data);
  
  ArrayList<AbstractShapeData> s3_data = new ArrayList();
  s3_data.add(new ShapeDataLine(Stage.pointDraggable(-100, -100), Stage.pointDraggable(100, -100)));
  //s3_data.add(new ShapeDataLine(Stage.pointDraggable(-100, -100), Stage.pointDraggable(200, -200)));
  s3 = Stage.shape("myShape3", color(150, 100), color(100, 0, 0, 70), null, null, s3_data);
  
  sd1 = Stage.shapeDraggable(s1);
  sd2 = Stage.shapeDraggable(s2);
  sd3 = Stage.shapeDraggable(s3);
  
  sl1 = Stage.slider(0, 0, 8, -100, 100, 100, -100, true, -10, 10, -5, 5, 2, 1);
  //sl2 = Stage.slider(0, -200, 8, -50, -200, 100, -200, true, -10, 10, 0, 0, 5, 0);
  //sl1 = Stage.slider(200, 200, 8, 100, 100, 300, 300, true, -10, 10, -5, 5, 10, 10);
  //sl2 = Stage.slider(200, 400, 8, 100, 400, 300, 400, true, -10, 10, -5, 5, 10, 10);
  
  //p1 = Stage.pointDraggable(0, 0, 8);
  /*
  p2 = Stage.pointDraggable(30, 20, 3);
  p3 = Stage.pointDraggable(80, 0, 3);
  p4 = Stage.pointDraggable(80, 75, 3);
  p5 = Stage.pointDraggable(30, 75, 3);
  */
  /*
  // hook into events stream
  Stage.addEventClient(new IStageEventClient(){
    public Event.Name[] registerForEvents() {
      return new Event.Name[]{Event.Name.draggableDragged};
    }
    public void onEvent(Event evt, Object emitter) {
      if (emitter == p1) {
        //println(s1.getData(4));
        //println(((AbstractShapeDataVertex)s1.getData(4)).p(0));
        //((AbstractShapeDataVertex)s1.getData(4)).p(0).x(p1.x());
        //p4.x(p1.x());
      }
    }
  });
  
  Stage.addEventClient(new IStageEventClient(){
    public Event.Name[] registerForEvents() {
      return new Event.Name[]{Event.Name.shapeModified};
    }
    public void onEvent(Event evt, Object emitter) {
        //println("emitter: " + emitter);
        //println("event: " + evt);
    }
  });
  */
  Stage.endSetup();
}

void draw() {
  Stage.startDraw();
  
  //n += 1;
  //p1.z = -n;
  //p1.x = n;
  //p1.y = n;
  
  /*
  fill(color(100,0,0,100));
  stroke(0);
  beginShape();
  vertex(p2.x(), p2.y());
  bezierVertex(p3.x(), p3.y(), p4.x(), p4.y(), p5.x(), p5.y());
  endShape();
  */
  

  Stage.endDraw();
  
}


