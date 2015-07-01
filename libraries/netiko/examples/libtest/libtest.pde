
import java.util.ArrayList;

import netiko.stage.Stage;

import netiko.stage.Point;
import netiko.stage.PointVirtual;
import netiko.stage.PointDraggable;
import netiko.stage.Slider;

import netiko.stage.Shape;
import netiko.stage.ShapeDraggable;
import netiko.stage.AbstractShapeData;
import netiko.stage.AbstractShapeDataVertex;
import netiko.stage.ShapeDataLine;
import netiko.stage.ShapeDataVertex;
import netiko.stage.ShapeDataBezierVertex;
import netiko.stage.ShapeDataQuadraticVertex;
import netiko.stage.ShapeContour;

import netiko.stage.IStageEventClient;
import netiko.stage.Event;


int w = 600;
int h = 600;
boolean isCartesian = true;

void setup() {
  Stage.startSetup(this, w, h, isCartesian);

  // hook into events stream
  Stage.addEventClient(new IStageEventClient(){
    public Event.Name[] registerForEvents() {
      return new Event.Name[]{Event.Name.draggableDragged};
    }
    public void onEvent(Event evt, Object emitter) {
      if (emitter == null) {
        Stage.textUserInfo.text("---");
      }
    }
  });

  Stage.endSetup();
}

void draw() {
  Stage.startDraw();
  Stage.endDraw();
}


