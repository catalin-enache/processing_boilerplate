

import netiko.stage.Stage;
import netiko.stage.Point;

Point p1;
int x = 0;

void setup() {
  Stage.setup(this, P3D, 400, 400, color(255), color(0), color(0));
  p1 = Stage.point(200, 200, 0, 2); 
}

void draw() {
  x += 1;
  p1.move(x, 0, 0);
  Stage.draw();
}


