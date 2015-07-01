
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

PointDraggable p1;
Shape s1, s2, s3, s4, s5;
ShapeDraggable sd1, sd2, sd3, sd4, sd5;

int w = 800;
int h = 800;
boolean isCartesian = false;

/*
ShapeContour inherits from AbstractShapeData (used to draw holes in a Shape)
AbstractShapeDataVertex inherits from AbstractShapeData
ShapeDataVertex, ShapeDataLine, ShapeDataQuadraticVertex, ShapeDataBezierVertex inherits from AbstractShapeDataVertex

shape_data is an ArrayList<AbstractShapeData>
PointVirtual, Point, PointDraggable, Slider are IPoint implementations
IPoint passed when constructing new AbstractShapeDataVertex implementation can be retreived back like: ((AbstractShapeDataVertex)shape_1.getData(index)).p(index));
IPoint postition might be get/set with x(), y(), xy(), x(float), y(float), xy(float, float)
example: ((AbstractShapeDataVertex)s3.getData(3)).p(2).y(p1.y());

ShapeDraggable accepts a Shape and makes it draggable
*/

void setup() {
  Stage.startSetup(this, w, h, isCartesian);
  
  ArrayList<AbstractShapeData> s1_data = new ArrayList();
  ArrayList<AbstractShapeData> s2_data = new ArrayList();
  ArrayList<AbstractShapeData> s3_data = new ArrayList();
  ArrayList<AbstractShapeData> s4_data = new ArrayList();
  ArrayList<AbstractShapeData> s5_data = new ArrayList();
  
  if (isCartesian) {
    p1 = Stage.pointDraggable(0, 0, 5);
    
    // PointVirtual (not drawable) handlers
    s1_data.add(new ShapeDataVertex(Stage.pointVirtual(-350, 350)));
    s1_data.add(new ShapeDataVertex(Stage.pointVirtual(-250, 350)));
    s1_data.add(new ShapeDataVertex(Stage.pointVirtual(-300, 300)));
    
    // Point (drawable but not draggable) handlers !!!
    s2_data.add(new ShapeDataVertex(Stage.point(100, 350)));
    s2_data.add(new ShapeDataQuadraticVertex(Stage.point(200, 300), Stage.point(300, 350)));
    
    // PointDraggable handlers
    s3_data.add(new ShapeDataVertex(Stage.pointDraggable(-350, -100)));
    s3_data.add(new ShapeDataVertex(Stage.pointDraggable(-350, -250)));
    s3_data.add(new ShapeDataVertex(Stage.pointDraggable(-150, -250)));
    s3_data.add(new ShapeDataBezierVertex(Stage.pointDraggable(-150, -150), Stage.pointDraggable(-250, -50), Stage.pointDraggable(-350, -50)));  
    // inner shape
    s3_data.add(new ShapeContour(ShapeContour.BEGIN));
    s3_data.add(new ShapeDataVertex(Stage.pointDraggable(-310, -130)));
    s3_data.add(new ShapeDataVertex(Stage.pointDraggable(-240, -130)));
    s3_data.add(new ShapeDataVertex(Stage.pointDraggable(-240, -200)));
    s3_data.add(new ShapeDataVertex(Stage.pointDraggable(-310, -200)));
    s3_data.add(new ShapeContour(ShapeContour.END));
    
    // mixed Slider & PointDraggable handlers
    s4_data.add(new ShapeDataVertex(Stage.slider(50, -300, 3, 0, -250, 100, -350, true, -1, 1, -1, 1, 1, 1))); 
    s4_data.add(new ShapeDataBezierVertex(
                              Stage.slider(100, -150, 3, 100, -50, 100, -200, true, 0, 0, -100, 100, 0, 1),
                              Stage.slider(250, -150, 3, 150, -150, 300, -150, true, -1, 1, 0, 0, 0.5, 0),
                              Stage.slider(300, -300, 3, 250, -250, 350, -350, true, -1, 1, -1, 1, 0, 0)
                              //Stage.pointDraggable(50, -300) // this PointDraggable or the previous Slider
                              )); 
    
    s5_data.add(new ShapeDataLine(Stage.pointDraggable(0, 100), Stage.pointDraggable(0, 200)));
    
  } else {
    p1 = Stage.pointDraggable(w/2, h/2, 5);
    
    // PointVirtual (not drawable) handlers
    s1_data.add(new ShapeDataVertex(Stage.pointVirtual(50, 50)));
    s1_data.add(new ShapeDataVertex(Stage.pointVirtual(150, 50)));
    s1_data.add(new ShapeDataVertex(Stage.pointVirtual(100, 100)));
    
    // Point (drawable but not draggable) handlers !!!
    s2_data.add(new ShapeDataVertex(Stage.point(500, 50)));
    s2_data.add(new ShapeDataQuadraticVertex(Stage.point(575, 100), Stage.point(650, 50)));
    
    // PointDraggable handlers
    s3_data.add(new ShapeDataVertex(Stage.pointDraggable(50, 550)));
    s3_data.add(new ShapeDataVertex(Stage.pointDraggable(50, 750)));
    s3_data.add(new ShapeDataVertex(Stage.pointDraggable(250, 750)));
    s3_data.add(new ShapeDataBezierVertex(Stage.pointDraggable(250, 550), Stage.pointDraggable(130, 500), Stage.pointDraggable(50, 500)));  
    // inner shape
    s3_data.add(new ShapeContour(ShapeContour.BEGIN));
    s3_data.add(new ShapeDataVertex(Stage.pointDraggable(100, 600)));
    s3_data.add(new ShapeDataVertex(Stage.pointDraggable(180, 600)));
    s3_data.add(new ShapeDataVertex(Stage.pointDraggable(180, 700)));
    s3_data.add(new ShapeDataVertex(Stage.pointDraggable(100, 700)));
    s3_data.add(new ShapeContour(ShapeContour.END));
    
    // mixed Slider & PointDraggable handlers
    s4_data.add(new ShapeDataVertex(Stage.slider(450, 700, 3, 400, 650, 500, 750, true, -1, 1, -1, 1, 1, 1))); 
    s4_data.add(new ShapeDataBezierVertex(
                              Stage.slider(450, 550, 3, 450, 400, 450, 600, true, 0, 0, -100, 100, 0, 1),
                              Stage.slider(600, 500, 3, 500, 500, 700, 500, true, -1, 1, 0, 0, 0.5, 0),
                              Stage.slider(650, 700, 3, 600, 650, 700, 750, true, -1, 1, -1, 1, 0, 0)
                              //Stage.pointDraggable(650, 700) // this PointDraggable or the previous Slider
                              )); 
                              
    s5_data.add(new ShapeDataLine(Stage.pointDraggable(w/2, 100), Stage.pointDraggable(w/2, 200)));


}
  s1 = Stage.shape("shape_1", color(150, 100, 0 , 50), color(100, 0, 0, 70), null, CLOSE, s1_data);
  s2 = Stage.shape("shape_2", color(150, 100, 0 , 50), color(100, 0, 0, 70), null, null, s2_data);
  s3 = Stage.shape("shape_3", color(150, 100, 0 , 50), color(100, 0, 0, 70), null, null, s3_data);
  s4 = Stage.shape("shape_4", color(150, 100, 0 , 50), color(100, 0, 0, 70), null, null, s4_data);
  s5 = Stage.shape("shape_5", color(150, 100, 0 , 50), color(100, 0, 0, 70), null, null, s5_data);
  
  sd1 = Stage.shapeDraggable(s1);
  sd2 = Stage.shapeDraggable(s2);
  sd3 = Stage.shapeDraggable(s3);
  sd4 = Stage.shapeDraggable(s4);
  sd5 = Stage.shapeDraggable(s5);
  
  // hook into events
  Stage.addEventClient(new IStageEventClient(){
    public Event.Name[] registerForEvents() {
      return new Event.Name[]{Event.Name.draggableDragged, Event.Name.shapeModified, Event.Name.draggableSelected};
    }
    public void onEvent(Event evt, Object emitter) {
      if (evt.name == Event.Name.draggableDragged) {
          Stage.textUserInfo.text(emitter.toString());
          if (emitter == p1) {
             ((AbstractShapeDataVertex)s1.getData(1)).p(0).x(p1.x());
             ((AbstractShapeDataVertex)s2.getData(0)).p(0).x(p1.x());
             ((AbstractShapeDataVertex)s3.getData(3)).p(2).y(p1.y());
           }
      } else if (evt.name == Event.Name.shapeModified) {
        //println(emitter);
      } else if (evt.name == Event.Name.draggableSelected) {
        println(emitter);
      }
       
    }
  });
  
  Stage.endSetup();
}

void draw() {
  Stage.startDraw();
  Stage.endDraw();
}


