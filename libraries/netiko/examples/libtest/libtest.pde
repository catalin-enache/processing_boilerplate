import netiko.stage.Test;

Test t;

void setup() {
  size(480, 120);
  t = new Test(this);
}

void draw() {
  if (mousePressed) {
    fill(0);
    t.print();
  } else {
    fill(255);
  }
}


