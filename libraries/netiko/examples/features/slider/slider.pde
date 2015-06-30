
import netiko.stage.Stage;
import netiko.stage.Slider;
import netiko.stage.IStageEventClient;
import netiko.stage.Event;

Slider sl1, sl2, sl3;

int w = 600;
int h = 600;
boolean isCartesian = true;

void setup() {
  Stage.startSetup(this, w, h, isCartesian);
  
  if (isCartesian) {
    sl1 = Stage.slider(-200, 200, 8, -200, 200, -200, -200, true, 0, 0, -1, 1, 0, 0.1);
    sl2 = Stage.slider(0, -200, 8, 0, -200, 200, -200, true, -3, 3, 0, 0, 3, 0);
    sl3 = Stage.slider(150, 150, 8, 100, 200, 200, 100, true, -100, 100, -100, 100, 0, 0);
  } else {
    sl1 = Stage.slider(100, 100, 8, 100, 100, 100, 400, true, 0, 0, -1, 1, 0, 0.1);
    sl2 = Stage.slider(w/2, 400, 8, w/2, 400, w/2+200, 400, true, -3, 3, 0, 0, 3, 0);
    sl3 = Stage.slider(350, 150, 8, 350, 150, 450, 250, true, -100, 100, -100, 100, 0, 0);
  }
  
  Stage.addEventClient(new IStageEventClient(){
    public Event.Name[] registerForEvents() {
      return new Event.Name[]{Event.Name.draggableDragged};
    }
    public void onEvent(Event evt, Object emitter) {
       Stage.textUserInfo.text(emitter.toString() + "\nline length: " + sqrt(pow(sl1.x()-sl2.x(), 2) + pow(sl1.y()-sl2.y(), 2)));
    }
  });
  
  Stage.endSetup();
}

void draw() {
  Stage.startDraw();
  
  if (isCartesian) {
    ellipse(-150, -150, sl3.rangeX(), sl3.rangeY());
  } else {
    ellipse(150, 450, sl3.rangeX(), sl3.rangeY());
  }
  line(sl1.x(), sl1.y(), sl2.x(), sl2.y());
  
  Stage.endDraw();
}


