

import netiko.stage.Stage;
import netiko.stage.Point;

Point p1;
int n1 = 0;

void setup() {
  Stage.setup(this, P3D, 400, 400, color(255), color(0), color(0));
  p1 = Stage.point(200, 200, 0, 5); 
}

void draw() {
  n1 += 1;
  //p1.x = n1;
  //p1.y = n1;
  Stage.draw();
}


