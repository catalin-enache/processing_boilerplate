package netiko.math;

import java.util.ArrayList;
import static java.lang.Math.*;

public class Util {

    public static ArrayList<MPoint> generateBezierCurvePoints(IPoint P0, IPoint P1, IPoint P2, IPoint P3, float resolution) {
        ArrayList<MPoint> MPoints = new ArrayList<>();
        for(float t=0; t<=1; t+=resolution){
            double x = pow((1 - t),3)*P0.x() + 3*t*pow((1 - t),2)*P1.x() + 3*pow(t,2)*(1 - t)*P2.x() + pow(t,3)*P3.x();
            double y = pow((1 - t),3)*P0.y() + 3*t*pow((1 - t),2)*P1.y() + 3*pow(t, 2)*(1 - t)*P2.y() + pow(t,3)*P3.y();
            MPoints.add(new MPoint((float)x, (float)y));
        }
        return MPoints;
    }

}
