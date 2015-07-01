
import netiko.stage.*;
import netiko.math.Point;
import netiko.math.*;

int w = 600;
int h = 600;
boolean isCartesian = true;

PointDraggable p0, p1, p2, p3;
Slider sl1;

void setup() {
  Stage.startSetup(this, w, h, isCartesian);
  
  p0 = Stage.pointDraggable(-177, -76);
  p1 = Stage.pointDraggable(-247, 212);
  p2 = Stage.pointDraggable(-214, 212);
  p3 = Stage.pointDraggable(240, -160);
  
  sl1 = Stage.slider(-122, 250, 10, -250, 250, 0, 250, true, 1, 50, 0, 0, 1, 0);
  
  Stage.endSetup();
}

void draw() {
  Stage.startDraw();
  
  stroke(0);
  ArrayList<Point> points = Util.generateBezierCurve(p0, p1, p2, p3, 0.1/sl1.rangeX());
  for (int i = 0; i < points.size(); i++) {
    Point p = points.get(i);
    point(p.x(), p.y());
  }
  
  Stage.endDraw();
}




