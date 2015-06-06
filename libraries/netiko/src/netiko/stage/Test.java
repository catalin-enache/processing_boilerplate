package netiko.stage;

import processing.core.*;
import static processing.core.PConstants.*;

public class Test {

    PApplet p;

    public Test(PApplet _p) {
        p = _p;
    }

    public void setup () {
        p.size(480, 120);
        p.background(0);
        p.stroke(255);
        p.smooth();

//        for(int i=0; i<360; i++){
//            float x = (30F * p.cos(PI/180F * i) );
//            float y = (30F * p.sin(PI/180F * i) );
//            p.println(p.sin(PI/180F * i));
//            p.point(x+60, y+60);
//        }

//        for(int x = -50; x<50; x++){
//            for(int y=-50; y<50; y++) {
////                if(x*x + y*y > 25*25 && x*x + y*y < 26*26){
////                    p.stroke(255);
////                    p.point(x+150,y+60);
////                }
////                if(x*x + y*y == 25*25){
////                    p.stroke(255, 0, 0);
////                    p.point(x+150,y+60);
////                }
////                p.point(x+150,y+60);
//            }
//        }


        float P0x = 10F, P0y = 10F;
        float P1x = 10F, P1y = 40F;
        float P2x = 40F, P2y = 60F;
        float P3x = 80F, P3y = 20F;

        p.stroke(255, 0, 0);
        p.point(P0x, P0y);
        p.point(P1x, P1y);
        p.point(P2x, P2y);
        p.point(P3x, P3y);

        p.stroke(255);
        for(float t=0.01F; t<0.99F; t+=0.05){
            float x = p.pow((1 - t),3)*P0x + 3*t*p.pow((1 - t),2)*P1x + 3*p.pow(t,2)*(1 - t)*P2x + p.pow(t,3)*P3x;
            float y = p.pow((1 - t),3)*P0y + 3*t*p.pow((1 - t),2)*P1y + 3*p.pow(t,2)*(1 - t)*P2y + p.pow(t,3)*P3y;
            p.point(x,y);
        }


    }

    public void draw () {

    }
}
