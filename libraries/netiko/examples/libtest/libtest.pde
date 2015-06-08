

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
  
  
  background(255);
  //camera(w/2+0, h/2+0, (h/2.0) / tan(PI*20 / 180.0), w/2, h/2, 0.0, 0.0, 1.0, 0.0);
  camera();
  translate(w/2, h/2, 0);
  if (mousePressed) {
    rotx = PI/180 * (mouseX - width/2) / 5;
    roty = PI/180 * (mouseY - height/2) / 5;
  }
  rotateY(rotx);
  rotateX(-roty);
  
  stroke(100);
  box(w);
  //line(-w/2, 0, w/2, 0, 0, 0);
  
  stroke(255,0,0);
  line(-w/2, 0, 0, w/2, 0, 0);
  stroke(0,255,0);
  line(0, -h/2, 0, 0, h/2, 0);
  stroke(0,0,255);
  line(0, 0, -z/2, 0, 0, z/2);
  
  
  stroke(255, 102, 0);
  line(30, 20, 80, 5);
  line(80, 75, 30, 75);
  stroke(0, 0, 0);
  bezier(30, 20,  80, 5,  80, 75,  30, 75);

}


