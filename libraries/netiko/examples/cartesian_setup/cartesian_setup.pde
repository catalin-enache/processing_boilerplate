 
 void setup(){
  size(640,480);
  translate(width/2, height/2);
  ellipse(-125, -125, 4, 4);
  ellipse(0, 0, 4, 4);
}
 void draw(){
  println(mouseX - width/2, mouseY - height/2);
 }
 
