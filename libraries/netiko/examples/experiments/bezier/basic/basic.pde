
void setup() {
  size(120, 120);
  background(0);
  stroke(255);
  
  float P0x = 10F, P0y = 10F;
  float P1x = 10F, P1y = 40F;
  float P2x = 40F, P2y = 60F;
  float P3x = 80F, P3y = 20F;

  stroke(255, 0, 0);
  point(P0x, P0y);
  point(P1x, P1y);
  point(P2x, P2y);
  point(P3x, P3y);

  stroke(255);
  for(float t=0.01F; t<0.99F; t+=0.05){
      float x = pow((1 - t),3)*P0x + 3*t*pow((1 - t),2)*P1x + 3*pow(t,2)*(1 - t)*P2x + pow(t,3)*P3x;
      float y = pow((1 - t),3)*P0y + 3*t*pow((1 - t),2)*P1y + 3*pow(t,2)*(1 - t)*P2y + pow(t,3)*P3y;
      point(x,y);
  }
  
  
}




