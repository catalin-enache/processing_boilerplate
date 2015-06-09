

import netiko.stage.Stage;
import netiko.stage.Point;

Point p1;
int n = 0;
float rotx = 0;
float roty = 0;

int w = 500;
int h = 500;
int z = 500;

PGraphics pg;

void setup() {
  
  Stage.startSetup(this, P3D, 800, 800, 800, 0XFFFFFFFF, 0XFF444444, color(255, 102, 0), true);
  
  pg = createGraphics(800, 800, P3D);
  
  p1 = Stage.point(0, 0, 0, 10); 
  
  Stage.endSetup();
  
  
}

void draw() {
  Stage.startDraw();
  
  //n += 1;
  //p1.z = -n;
  p1.x = n;
  p1.y = n;
  
  pg.beginDraw();
  pg.pushStyle();
  pg.pushMatrix();
  pg.background(102,50);
  //pg.translate(0,-50,50);
  pg.stroke(color(255, 102, 0));
  pg.line(30, 20, 80, 5);
  pg.line(80, 75, 30, 75);
  pg.stroke(0, 0, 0);
  //noFill();
  pg.fill(color(0, 0, 255, 150));
  //pg.bezier(30, 20, 80, 5, 80, 75, 30, 75);
  pg.bezier(30, 20, 0, 80, 5, 0, 80, 75, 0, 30, 75, 0);
  pg.popMatrix();  
  pg.popStyle();
  pg.endDraw();
  
  Stage.endDraw();
  image(pg, 0, 0);
}


