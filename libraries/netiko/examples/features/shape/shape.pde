
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

Shape s1, s2, s3, s4;
ShapeDraggable sd1, sd2, sd3, sd4;

int w = 800;
int h = 800;
boolean isCartesian = true;

void setup() {
  Stage.startSetup(this, w, h, isCartesian);
  
  ArrayList<AbstractShapeData> s1_data = new ArrayList();
  ArrayList<AbstractShapeData> s2_data = new ArrayList();
  ArrayList<AbstractShapeData> s3_data = new ArrayList();
  ArrayList<AbstractShapeData> s4_data = new ArrayList();
  
  if (isCartesian) {
    s1_data.add(new ShapeDataVertex(Stage.pointVirtual(-350, 350)));
    s1_data.add(new ShapeDataVertex(Stage.pointVirtual(-250, 350)));
    s1_data.add(new ShapeDataVertex(Stage.pointVirtual(-300, 300)));
    
    s2_data.add(new ShapeDataVertex(Stage.point(100, 350)));
    s2_data.add(new ShapeDataQuadraticVertex(Stage.point(200, 300), Stage.point(300, 350)));
    
    s3_data.add(new ShapeDataVertex(Stage.pointDraggable(-350, -100)));
    s3_data.add(new ShapeDataVertex(Stage.pointDraggable(-350, -250)));
    s3_data.add(new ShapeDataVertex(Stage.pointDraggable(-150, -250)));
    s3_data.add(new ShapeDataBezierVertex(Stage.pointDraggable(-150, -150), Stage.pointDraggable(-250, -50), Stage.pointDraggable(-350, -50)));  

    s4_data.add(new ShapeDataVertex(Stage.slider(50, -50, 3, 50, 0, 50, -250, true, 0, 0, -1, 1, 0, 1))); 
    s4_data.add(new ShapeDataBezierVertex(
                              Stage.pointDraggable(150, -50),
                              Stage.pointDraggable(150, -150),
                              Stage.slider(50, -300, 3, 0, -250, 100, -350, true, -1, 1, -1, 1, 1, 0.25)
                              //Stage.pointDraggable(50, -300)
                              )); 
    
  } else {

  }
  s1 = Stage.shape("shape_1", color(150, 100, 0 , 50), color(100, 0, 0, 70), null, CLOSE, s1_data);
  s2 = Stage.shape("shape_2", color(150, 100, 0 , 50), color(100, 0, 0, 70), null, null, s2_data);
  s3 = Stage.shape("shape_3", color(150, 100, 0 , 50), color(100, 0, 0, 70), null, null, s3_data);
  s4 = Stage.shape("shape_4", color(150, 100, 0 , 50), color(100, 0, 0, 70), null, null, s4_data);
  
  sd1 = Stage.shapeDraggable(s1);
  sd2 = Stage.shapeDraggable(s2);
  sd3 = Stage.shapeDraggable(s3);
  sd4 = Stage.shapeDraggable(s4);
  
  Stage.addEventClient(new IStageEventClient(){
    public Event.Name[] registerForEvents() {
      return new Event.Name[]{Event.Name.draggableDragged};
    }
    public void onEvent(Event evt, Object emitter) {
       Stage.textUserInfo.text(emitter.toString());
    }
  });
  
  Stage.endSetup();
}

void draw() {
  Stage.startDraw();
  Stage.endDraw();
}


