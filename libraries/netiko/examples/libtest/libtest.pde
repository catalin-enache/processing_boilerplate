

import netiko.stage.Stage;
import netiko.stage.Point;

Point p1;
int n = 0;
float rot = 0;

int w = 500;
int h = 500;

void setup() {
  /*
  Stage.startSetup(this, P3D, 400, 400, 400, color(255), color(0), color(100, 0, 0), true);
  
  p1 = Stage.point(0, 0, 0, 10); 
  
  Stage.endSetup();
  */
  
  size(w, h, P3D);
  noFill();
  //fill(255,0,0,0);
  lights();
  
}

void draw() {
  /*
  Stage.startDraw();
  
  n += 1;
  p1.x = n;
  p1.y = n;
  
  Stage.endDraw();
  */
  
  
  background(204);
  camera(w/2+0, h/2+0, (h/2.0) / tan(PI*20 / 180.0), w/2, h/2, 0.0, 0.0, 1.0, 0.0);
  //camera();
  translate(w/2, h/2, 0);
  if (mousePressed) {
    rot = PI/180 * mouseX;
  }
  rotateY(rot);
  
  box(400);
  line(-w/2, 0, w/2, 0, 0, 0);
  
  //line(0, 0, 40, 40, 0, 0);
  
  
  

}


