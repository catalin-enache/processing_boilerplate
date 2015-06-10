

import netiko.stage.Stage;
import netiko.stage.Point;

Point p1;
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
  
  p1 = Stage.point(0, 0, 0, 10); 
  
  Stage.endSetup();
}

void draw() {
  Stage.startDraw();
  
  
  n += 1;
  //p1.z = -n;
  //p1.x = n;
  //p1.y = n;
  
  
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
  
  Stage.endDraw();
  
}


