
import java.util.ArrayList;
import netiko.stage.Stage;
import netiko.stage.Point;
import netiko.stage.PointDraggable;
import netiko.stage.Shape;
import netiko.stage.ShapeData;
import netiko.stage.ShapeDataVertex;

PointDraggable p1;
Shape s1;

int n = 0;
float rotx = 0;
float roty = 0;

int w = 500;
int h = 500;
int z = 500;


void setup() {
  Stage.startSetup(this, P3D, 800, 800, 800, 0XFFFFFFFF, 0X99444444, color(255, 102, 0), true);
  hint(DISABLE_OPTIMIZED_STROKE);
  //hint(DISABLE_DEPTH_TEST);
  
  p1 = Stage.pointDraggable(0, 0, 0, 10);
  
  ArrayList<ShapeData> s1_data = new ArrayList();
  s1_data.add(new ShapeDataVertex(0, 0, 0));
  s1_data.add(new ShapeDataVertex(50, 50, 0));
  s1_data.add(new ShapeDataVertex(0, 100, 0));
  s1 = Stage.shape(150, color(100, 0, 0), null, null, true,  s1_data);
  
  Stage.endSetup();
}

void draw() {
  Stage.startDraw();
  
  n += 1;
  //p1.z = -n;
  //p1.x = n;
  //p1.y = n;
  
  /*
  pushStyle();
  pushMatrix();
  stroke(color(255, 102, 0, 255));
  line(30, 20, 80, 5);
  line(80, 75, 30, 75);
  stroke(color(0, 0, 0, 100));
  //noFill();
  fill(color(0, 0, 255, 200));
  bezier(30, 20, 0, 80, 5, 0, 80, 75, 0, 30, 75, 0);
  fill(color(0, 255, 0, 200));
  bezier(30, 20, -50, 80, 5, -50, 80, 75, 50, 30, 75, 50);
  popMatrix();  
  popStyle();
  */
  
  /*
  beginShape();
  pushStyle();
  pushMatrix();
  fill(10);
  stroke(100, 0, 0);
  vertex(5, 5, 0);
  vertex(15, 35, 0);
  vertex(15, 85, 0);
  popMatrix();
  popStyle();
  endShape();
  */

  Stage.endDraw();
  
}


