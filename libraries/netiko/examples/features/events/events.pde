
import netiko.stage.Stage;
import netiko.stage.PointDraggable;
import netiko.stage.Slider;
import netiko.stage.AbstractShapeData;
import netiko.stage.ShapeDataVertex;
import netiko.stage.Shape;
import netiko.stage.ShapeDraggable;
import netiko.stage.IStageEventClient;
import netiko.stage.Event;

PointDraggable p1;
Slider sl1;
Shape s1;
ShapeDraggable sd1;

int w = 600;
int h = 600;
boolean isCartesian = true;

void setup() {
  Stage.startSetup(this, w, h, isCartesian);
  
  ArrayList<AbstractShapeData> s1_data = new ArrayList();
  
  if (isCartesian) {
    p1 = Stage.pointDraggable(0, 0, 10);
    // fixed slider - might be treated as a button
    sl1 = Stage.slider(50, -50, 10, 50, -50, 50, -50, false, 0, 0, 0, 0, 0, 0);
    s1_data.add(new ShapeDataVertex(Stage.pointDraggable(10, 10)));
    s1_data.add(new ShapeDataVertex(Stage.pointDraggable(100, 100)));
    s1_data.add(new ShapeDataVertex(Stage.pointDraggable(50, 100)));
  } else {
    p1 = Stage.pointDraggable(w/2, h/2, 10);
    // fixed slider - might be treated as a button
    sl1 = Stage.slider(50, 150, 10, 50, 150, 50, 150, false, 0, 0, 0, 0, 0, 0);
    s1_data.add(new ShapeDataVertex(Stage.pointDraggable(300, 130)));
    s1_data.add(new ShapeDataVertex(Stage.pointDraggable(340, 220)));
    s1_data.add(new ShapeDataVertex(Stage.pointDraggable(390, 220)));
  }

  s1 = Stage.shape("shape_1", color(150, 100, 0 , 50), color(100, 0, 0, 70), null, null, s1_data);
  sd1 = Stage.shapeDraggable(s1);
  
  // notice how we hook into Events stream with IStageEventClient
  Stage.addEventClient(new IStageEventClient(){
    public Event.Name[] registerForEvents() {
      // mousePressed, mouseReleased, mouseMove, draggableDragged, pointUpdated, shapeModified, draggableSelected
      return new Event.Name[]{
        Event.Name.mousePressed, 
        Event.Name.mouseReleased, 
        Event.Name.mouseMove, 
        Event.Name.pointUpdated, 
        Event.Name.draggableSelected,
        Event.Name.draggableDragged, 
        Event.Name.shapeModified
      };
    }
    public void onEvent(Event evt, Object emitter) {
      if (evt.name == Event.Name.mousePressed) {
        //Stage.textUserInfo.text(evt + emitter.toString());
      } else if (evt.name == Event.Name.mouseReleased) {
        //Stage.textUserInfo.text(evt + emitter.toString());
      } else if (evt.name == Event.Name.mouseMove) {
        //Stage.textUserInfo.text(evt + emitter.toString());
      } else if (evt.name == Event.Name.pointUpdated) {
        //Stage.textUserInfo.text(evt + emitter.toString());
      } else if (evt.name == Event.Name.draggableSelected) {
        if (emitter != null) {
          Stage.textUserInfo.text(evt + emitter.toString());
        } else {
          Stage.textUserInfo.text(evt + "null");
        }
      } else if (evt.name == Event.Name.draggableDragged) {
        //Stage.textUserInfo.text(evt + emitter.toString());
      } else if (evt.name == Event.Name.shapeModified) {
        //Stage.textUserInfo.text(evt + emitter.toString());
      } 
    }
  });
  
  Stage.endSetup();
}

void draw() {
  Stage.startDraw();
  Stage.endDraw();
}


