import netiko.stage.Test;

Test t;

void setup() {
  size(480, 120);
  background(0);
  t = new Test(this);
}

void draw() {
  if (mousePressed) {
    stroke(255);
    t.print();
  } 
}


