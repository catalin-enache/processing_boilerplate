package netiko.math;

import netiko.stage.Stage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import static java.lang.Math.*;

public class Util {

    public static ArrayList<MPoint> generateBezierCurvePoints(IPoint p0, IPoint p1, IPoint p2, IPoint p3, float resolution) {
        ArrayList<MPoint> MPoints = new ArrayList<>();
        for(float t=0; t<=1; t+=resolution){
            double x = pow((1 - t),3)*p0.x() + 3*t*pow((1 - t),2)*p1.x() + 3*pow(t,2)*(1 - t)*p2.x() + pow(t,3)*p3.x();
            double y = pow((1 - t),3)*p0.y() + 3*t*pow((1 - t),2)*p1.y() + 3*pow(t, 2)*(1 - t)*p2.y() + pow(t,3)*p3.y();
            MPoints.add(new MPoint((float)x, (float)y));
        }
        return MPoints;
    }

    // helper to get margins for the value
    public static float[] e(float value, float margin) {
        return new float[]{value - margin, value + margin};
    }

    /**
     * @param p0 point to check
     * @param p1 point defining one rect corner
     * @param p2 point defining the opposite rect corner
     * @return boolean weather p0 inside rect defined by p1 & p2
     */
    public static boolean pointInRectOfTwoPoints(IPoint p0, IPoint p1, IPoint p2, float epsilon) {
        ArrayList<Float> xs = new ArrayList<>(Arrays.asList(p1.x(), p2.x()));
        Collections.sort(xs);
        ArrayList<Float> ys = new ArrayList<>(Arrays.asList(p1.y(), p2.y()));
        Collections.sort(ys);

        float leftX = xs.get(0) - epsilon;
        float rightX = xs.get(1) + epsilon;
        float bottomY = ys.get(0) - epsilon;
        float topY = ys.get(1) + epsilon;

        boolean ret = leftX <= p0.x() && p0.x() <= rightX && bottomY <= p0.y() &&  p0.y() <= topY;
        return ret;
    }

    /**
     * slope = raise / run
     * @param p0
     * @param p1
     * @return m (slope)
     */
    public static float lineSlopeFromTwoPoints(IPoint p0, IPoint p1) {
        float raise = p0.y() - p1.y();
        float run = p0.x() - p1.x();
        if (raise == 0 && run == 0) { return Float.NaN; }
        if (raise == 0) { return 0; } // horizontal
        if (run == 0) {return Float.POSITIVE_INFINITY;}
        return raise/run;
    }

    /**
     * y = mx + b || b = y - mx
     * @param p
     * @param m
     * @return b (y intercept)
     */
    public static float lineYInterceptPointSlope(IPoint p, float m) {
        float b = p.y() - m * p.x();
        return b;
    }


    /**
     * b1 = y - m1 * x | from y = mx + b
     * b2 = y - m2 * x | so, y = b2 + m2 * x in second equation
     * b1 = b2 + m2 * x - m1 * x # substitute y in first eq
     * b1 - b2 = m2 * x - m1 * x # put x terms on the same side
     * b1 - b2 = x(m2 - m1) # factor out x
     * (b1 - b2)/(m2 - m1) = x # this is x value in first equation
     * we will find y later by putting x in any of these 2 equations - theoretically
     * in practice we'll put x in both of them and y values will be almost the same
     * @param m1
     * @param x1
     * @param b1
     * @param m2
     * @param x2
     * @param b2
     * @return IPoint intersection point
     */
    public static IPoint lineSystemOfEquationsOfSlopeInterceptForm(float m1, float x1, float b1, float m2, float x2, float b2) {
        float x, y;
        // TODO: what if m is NaN ?
        if (m1 == 0 && m2 == 0){ // if both lines are horizontal
            return null;
        } else if (m1 == Float.POSITIVE_INFINITY && m2 == Float.POSITIVE_INFINITY) { // if both lines are vertical
            return null;
        } else if (m1 == Float.POSITIVE_INFINITY) { // if line_1 is vertical
            x = x1;  // any x from line_1
            y = m2 * x + b2;  // y is equation of line_2 for x1
        } else if (m2 == Float.POSITIVE_INFINITY) { // if line_2 is vertical
            x = x2;  // any x from line_2
            y = m1 * x + b1;  // y is equation of line_1 for x2
        } else {
            x = (b1 - b2) / (m2 - m1);
            float y00 = m1 * x + b1;
            float y01 = m2 * x + b2;
            // y00 and y01 are found by putting resulted x in both lines equations
            // Theoretically y00 and y01 should be identical.
            // In practice their value is almost equal but not exactly the same, so we get a mean for both
            y = (y00 + y01) / 2;
        }
        return new MPoint(x, y);
    }

    /**
     *
     * @param line1P1
     * @param line1P2
     * @param line2P1
     * @param line2P2
     * @return IPoint intersection
     */
    public static IPoint intersectionLineLine(IPoint line1P1, IPoint line1P2, IPoint line2P1, IPoint line2P2, boolean segmentIntersection) {
        float x1 = line1P1.x();
        float x2 = line2P1.x();
        float m1 = lineSlopeFromTwoPoints(line1P1, line1P2);
        float m2 = lineSlopeFromTwoPoints(line2P1, line2P2);
        float b1 = lineYInterceptPointSlope(line1P1, m1);
        float b2 = lineYInterceptPointSlope(line2P2, m2);
        IPoint intersectionPoint = lineSystemOfEquationsOfSlopeInterceptForm(m1, x1, b1, m2, x2, b2); // might be null
        if (!segmentIntersection) {
            return intersectionPoint;
        } else if(intersectionPoint != null) {
            if (pointInRectOfTwoPoints(intersectionPoint, line1P1, line1P2, 0.1F)
                    &&
                    pointInRectOfTwoPoints(intersectionPoint, line2P1, line2P2, 0.1F)) {
                return intersectionPoint;
            }
        }
        return null;
    }

    /**
     * find equation of line
     * put p.x in this equation and check if test y ~ p.y
     * @param p
     * @param lineP1
     * @param lineP2
     * @return
     */
    public static boolean intersectionPointLine(IPoint p, IPoint lineP1, IPoint lineP2, boolean segment, float epsilon) {
        float m = lineSlopeFromTwoPoints(lineP1, lineP2);
        float b = lineYInterceptPointSlope(lineP1, m);
        boolean test = false;

        if (m == Float.POSITIVE_INFINITY) { // if line is vertical
            // in theory testing if p.x() == lineP1.x() or p.x() == lineP2.x() should be enough but ...
            float[] x00_x01 = e(lineP1.x(), epsilon);
            float x00 = x00_x01[0];
            float x01 = x00_x01[1];
            test = x00 <= p.x() && p.x() <= x01;
        } else {
            float yTest = m * p.x() + b; // in theory testing if p.y() == yTest should be enough but ...
            float _epsilon = epsilon;
            if (m > 1 || m < -1) { // as m tends to infinity
                _epsilon = epsilon * Math.abs(m); // m tends to infinity as line tends toward vertical. so, we have to increase the epsilon margin because very small x changes result in big yTest changes and a fixed margin would not fit.
            }
            float[] y00_y01 = e(yTest, _epsilon);
            float y00 = y00_y01[0];
            float y01 = y00_y01[1];
            test = y00 <= p.y() && p.y() <= y01;
        }

        if (!segment) {
            return test;
        } else { // check if point on segment
            return test && pointInRectOfTwoPoints(p, lineP1, lineP2, epsilon);
        }
    }

}
