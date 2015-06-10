
import netiko.stage.Stage;
import netiko.stage.Point;

Point p1;

int w = 800;
int h = 800;
int z = 800;

PGraphics pg;

void setup() {
  Stage.startSetup(this, P3D, w, h, z, 0XFFFFFFFF, 0X99444444, color(255, 102, 0), true);
  
  pg = createGraphics(w, h, P3D);
  
  p1 = Stage.point(0, 0, 0, 10); 
  
  Stage.endSetup();
}

void draw() {
  Stage.startDraw();
  
  pg.beginDraw();
  pg.pushStyle();
  pg.pushMatrix();
  pg.background(102,50);
  pg.translate(w/2,h/2, 0);
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
  image(pg, -w/2, -h/2);
}


