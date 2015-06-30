
import netiko.stage.Stage;
import netiko.stage.PointDraggable;
import netiko.stage.IStageEventClient;
import netiko.stage.Event;

PointDraggable p1, p2;

int w = 600;
int h = 600;
boolean isCartesian = true;

void setup() {
  Stage.startSetup(this, w, h, isCartesian);
  
  if (isCartesian) {
    p1 = Stage.pointDraggable(0, 0, 8);
    p2 = Stage.pointDraggable(30, 30, 8);
  } else {
    p1 = Stage.pointDraggable(w/2, h/2, 8);
    p2 = Stage.pointDraggable(w/2 + 30, h/2 + 30, 8);
  }
  
  Stage.addEventClient(new IStageEventClient(){
    public Event.Name[] registerForEvents() {
      return new Event.Name[]{Event.Name.draggableDragged};
    }
    public void onEvent(Event evt, Object emitter) {
       Stage.textUserInfo.text(emitter.toString());
    }
  });
  
  Stage.endSetup();
}

void draw() {
  Stage.startDraw();
  line(p1.x(), p1.y(), p2.x(), p2.y());
  Stage.endDraw();
}


