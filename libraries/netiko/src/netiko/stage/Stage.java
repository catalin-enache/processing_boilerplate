package netiko.stage;

import processing.core.*;


import static processing.core.PConstants.*;
import java.util.ArrayList;
import java.util.HashMap;

import javax.media.opengl.*;
import javax.media.opengl.glu.*;
import processing.opengl.*;

public class Stage  {

    protected static PApplet p;
    protected static String renderer;
    protected static int width;
    protected static int height;
    protected static int depth;
    protected static int bgColor = -1;
    protected static int pColor = -1;
    protected static int sColor = -1;
    protected static boolean isCartezian;
    protected static float rotX = 0;
    protected static float rotY = 0;
    protected static float cPosZ = 30;

    protected static PImage cArrow;

    protected static boolean mousePressed = false;
    protected static boolean mouseHoverOn = false;
    protected static Object mouseHoverSetter = null;

    protected static ArrayList<IDrawable> drawables = new ArrayList<>();

    //protected static GL gl; // TODO ??
    //protected static GLU glu; // TODO ??
    protected static Selection_in_P3D_OPENGL_A3D helper;

    /*
    usage:  Stage.startSetup(this, P3D, 800, 800, 800, 0XFFFFFFFF, 0XFF444444, color(255, 102, 0), true);
    hint(DISABLE_OPTIMIZED_STROKE); // might be used as default
    hint(DISABLE_DEPTH_TEST); // optional where needed
    */
    public static void startSetup(PApplet _p, String _renderer, int _width, int _height, int _depth, int _bgColor, int _pColor, int _sColor, boolean _isCartezian) {
        p = _p; // PApplet
        renderer = _renderer; // what renderer to use

        cArrow = p.loadImage(Stage.class.getResource("use32.gif").toString());

        width = _width; // stage width
        height = _height; // stage height
        depth = _depth;

        bgColor = _bgColor; // background color
        pColor = _pColor; // point color
        sColor = _sColor; // stroke color

        isCartezian = _isCartezian;

        //gl=((PGraphicsOpenGL)p.g).gl;
        //glu=((PGraphicsOpenGL)p.g).glu;
        helper = new Selection_in_P3D_OPENGL_A3D(p);

        if (!renderer.equals("")) {
            p.size(width, height, renderer);
        }
        else {
            p.size(width, height);
        }

        start();

    }

    public static void endSetup() { end(); }

    public static void startDraw() {
        start();
    }

    public static void endDraw() {
        end();
    }

    // SECTION create and return objects

    public static Point point(float x, float y, float z, float r) {
        Point newPoint =  new Point(x, y, z, r);
        drawables.add(newPoint);
        return newPoint;
    }

    public static PointDraggable pointDraggable(float x, float y, float z, float r) {
        PointDraggable newPointDraggable =  new PointDraggable(x, y, z, r);
        drawables.add(newPointDraggable);
        return newPointDraggable;
    }

    public static Shape shape(int bgColor, int sColor, Integer beginShape, Integer endShape, boolean showPoints, ArrayList<ShapeData> shapeData) {
        Shape newShape =  new Shape(bgColor, sColor, beginShape, endShape, showPoints, shapeData);
        drawables.add(newShape);
        return newShape;
    }

    // SECTION get stuff

    public static PApplet getPApplet() {
        return p;
    }

    public static String getRenderer() {
        return renderer;
    }

    public static int getBgColor() {
        return bgColor;
    }

    public static int getPointColor() {
        return pColor;
    }

    public static int getStrokeColor() {
        return sColor;
    }

    // to be called after stage transformation was applied
    public static float[] getTranslatedMouse() {
        float[] coords;
        if (isCartezian) {
            coords = new float[]{p.mouseX - width/2, -(p.mouseY - height/2)};
        } else {
            coords = new float[]{p.mouseX, p.mouseY};
        }
        return coords;
    }

    // SECTION set stuff

    public static void setHoverState(boolean on, Object setter) {
        if (on && setter != mouseHoverSetter) {
            mouseHoverOn = true;
            mouseHoverSetter = setter;
            p.cursor(HAND);
        } else if (!on && setter == mouseHoverSetter) {
            mouseHoverOn = false;
            mouseHoverSetter = null;
            p.cursor(ARROW);
        }
    }

    // SECTION internal stage stuff

    private static void resetView () {
        cPosZ = 30;
        rotX = 0;
        rotY = 0;
    }

    private static void start() {
        p.smooth(8);
        p.stroke(bgColor); // seems to help with antialiasing
        p.strokeWeight(1);
        // DISABLE_OPTIMIZED_STROKE helps in transparency issues (when transparency seems to be ignored)
        // though not necessarily needed if DISABLE_DEPTH_TEST is on
        // yet it is still  needed because without it the strokes behind a transparent fill will not respect the order
        // and will be seen as they would have been drown on top of the fill (even if beyond it)
        //p.hint(DISABLE_OPTIMIZED_STROKE);
        // DISABLE_DEPTH_TEST helps in transparency issues (when transparency seems to be ignored)
        // better results in combination with DISABLE_OPTIMIZED_STROKE
        // though overlapping fills do not respect z buffer
        // http://processingjs.org/reference/hint_/
        //p.hint(DISABLE_DEPTH_TEST);
        if (renderer.equals(P3D)) {
            p.lights();
        }



        if (isCartezian) {
            if (renderer.equals(P3D)) {
                if (p.keyPressed) {
                    if (p.key == 'w') {
                        cPosZ += 0.2;
                    } else if (p.key == 's') {
                        cPosZ -= 0.2;
                    } else if (p.key == 'r') {
                        resetView();
                    }
                    // set the camera
                    p.camera(width/2F, height/2F, (height/2F) / p.tan(PI/180F * cPosZ), width/2F, height/2F, 0F, 0F, 1F, 0F); // PI*?
                }
                // move to the middle of stage
                p.translate(width/2, height/2, 0);
            }
            else {
                p.translate(width/2, height/2);
            }
        }

        if (renderer.equals(P3D)) {
            if (p.mousePressed && p.mouseButton == RIGHT) {
                rotX = PI/180 * (p.mouseX - width/2) / 5;
                rotY = PI/180 * (p.mouseY - height/2) / 5;
            }
            // apply any rotation
            p.rotateY(rotX);
            if (isCartezian) {
                p.rotateX(rotY);
            } else {
                p.rotateX(-rotY);
            }
        }

        helper.captureViewMatrix((PGraphics3D)p.g);

        p.background(bgColor);

        if (isCartezian) {
            drawCoords(); //  put here in case of DISABLE_OPTIMIZED_STROKE is on and DISABLE_DEPTH_TEST is off so we still can see the coords as they are drown before anything
        }

        emitStageEvent();
        // let the rest of objects draw their stuff in this new environment
    }

    private static void end() {
        for (IDrawable d : drawables) {
            d.draw();
        }

        drawCursor();

        if (isCartezian) {
            reverseSceneY();
        }
    }

    private static void drawCursor() {
        float[] xyz = getTranslatedMouse();
        //p.ellipse(xyz[0], xyz[1], 5, 5);
        p.hint(DISABLE_DEPTH_TEST);
        p.image(cArrow, xyz[0], xyz[1]);
        p.hint(ENABLE_DEPTH_TEST);

        //System.out.println(coords[0] + " | " + coords[1]);
        //System.out.println((p.modelX(p.mouseX - width/2, p.mouseY - height/2, 0) - width/2) + " || " + (-(p.modelY(p.mouseX - width/2, p.mouseY - height/2, 0) - height/2)));


        //p.printMatrix();
        helper.calculatePickPoints(xyz[0], xyz[1]);
        p.println(helper.ptStartPos);
        p.println(helper.ptEndPos);

        /*
        PMatrix3D pMatrix = (PMatrix3D)p.getMatrix();
        pMatrix.invert();
        PVector mouseCoords = new PVector(p.mouseX, p.mouseY, 0);
        //PVector mouseCoords = new PVector(xyz[0], xyz[1], 0);
        PVector mouseTranslated = pMatrix.mult(mouseCoords, null);
        p.println((mouseTranslated.x - width/2) + ", " + -(mouseTranslated.y - height/2) + ", " + (mouseTranslated.z));
        //pMatrix.print();
        //float[] translatedCoords = multiply_vec4_with_m4_4(new float[p.mouseX, p.mouseY, 1F], );
        */
    }

    private static void drawCoords() {
        p.pushStyle();

        p.stroke(255, 0, 0, 120); // x
        p.line(-width / 2, 0, width / 2, 0);
        p.stroke(0, 255, 0, 120); // y
        p.line(0, -height/2, 0, height/2);
        if (renderer == P3D) {
            p.stroke(0, 0, 255, 120); // z
            p.line(0, 0, -depth / 2, 0, 0, depth / 2);
            p.noFill();
            p.stroke(100); // cube
            p.box(width, height, depth);
        }

        p.popStyle();
    }

    private static void reverseSceneY() {
        p.loadPixels();
        for (int i = 0; i < height/2; i++) {
            int[] tmp = new int[width];
            int startRowPosition = i * width;
            int endRowPosition = width * (height - i - 1);

            System.arraycopy(p.pixels, endRowPosition, tmp, 0, width); // save end row into tmp
            System.arraycopy(p.pixels, startRowPosition, p.pixels, endRowPosition, width); // put start row into  end row
            System.arraycopy(tmp, 0, p.pixels, startRowPosition, width); // put tmp row into  start row
        }
        p.updatePixels();
    }

    // section emit events

    protected static void emitStageEvent() {
        Event evt = null;
        HashMap<String, Object> evtData = null;

        if (!mousePressed && p.mousePressed) {
            mousePressed = true;
            evt = new Event(Event.Name.mousePressed, evtData);
        } else if (mousePressed && !p.mousePressed) {
            mousePressed = false;
            evt = new Event(Event.Name.mouseReleased, evtData);
        } else {
            evt = new Event(Event.Name.mouseMove, evtData);
        }

        emitEvent(evt);
    }

    public static void emitEvent(Event evt) {
        if (evt == null) {
            return;
        }
        for (IStageEventClient d : drawables) {
            Event.Name[] eventNameRegistered = d.registerForEvents();
            for (int i = 0; i < eventNameRegistered.length; i++) {
                if (eventNameRegistered[i] == evt.name) {
                    d.onEvent(evt);
                }
            }
        }
    }
    /*
    private float[] multiply_vec4_with_m4_4(float[] vec4, float[][] m4_4)
    {
        float transformedX, transformedY, transformedZ;

        transformedX =  vec4[0] * m4_4[0][0] + vec4[1] * m4_4[1][0] + vec4[2] * m4_4[2][0] + m4_4[3][0];
        transformedY =  vec4[0] * m4_4[0][1] + vec4[1] * m4_4[1][1] + vec4[2] * m4_4[2][1] + m4_4[3][1];
        transformedZ =  vec4[0] * m4_4[0][2] + vec4[1] * m4_4[1][2] + vec4[2] * m4_4[2][2] + m4_4[3][2];

        return new float[]{transformedX, transformedY, transformedZ, 1F};
    }

    private float[] multiply_vec3_with_m3_3(float[] vec3, float[][] m3_3)
    {
        float transformedX, transformedY;

        transformedX =  vec3[0] * m3_3[0][0] + vec3[1] * m3_3[1][0] + m3_3[2][0];
        transformedY =  vec3[0] * m3_3[0][1] + vec3[1] * m3_3[1][1] + m3_3[2][1];


        return new float[]{transformedX, transformedY, 1F};

    }
    */



}

// http://andrewmarsh.com/blog/2011/12/04/gluunproject-p3d-and-opengl-sketches
class Selection_in_P3D_OPENGL_A3D {


    private boolean m_bValid = false;
    private int[] m_aiViewport = new int[4];
    // Store the near and far ray positions.
    public PVector ptStartPos = new PVector();
    public PVector ptEndPos = new PVector();
    private PMatrix3D m_pMatrix = new PMatrix3D();

    private PApplet p;

    Selection_in_P3D_OPENGL_A3D (PApplet p) {
        this.p = p;
    }

    //Maintain own projection matrix.
    public PMatrix3D getMatrix() { return m_pMatrix; }

    // Maintain own viewport data.
    public int[] getViewport() { return m_aiViewport; }

    //True if near and far points calculated.
    public boolean isValid() { return m_bValid; }

    public void captureViewMatrix(PGraphics3D g3d) {
    // Call this to capture the selection matrix after
    // you have called perspective() or ortho() and applied your
    // pan, zoom and camera angles - but before you start drawing
    // or playing with the matrices any further.
        if (g3d == null) {
            // Use main canvas if it is P3D, OPENGL or A3D.
            g3d = (PGraphics3D)p.g;
        }
        if (g3d != null) {
                // Check for a valid 3D canvas.
                // Capture current projection matrix.
                m_pMatrix.set(g3d.projection);
                // Multiply by current modelview matrix.
                m_pMatrix.apply(g3d.modelview);
                // Invert the resultant matrix.
                m_pMatrix.invert();
                // Store the viewport.
                m_aiViewport[0] = 0;
                m_aiViewport[1] = 0;
                m_aiViewport[2] = g3d.width;
                m_aiViewport[3] = g3d.height;
        }
    }

    public boolean gluUnProject(float winx, float winy, float winz, PVector result) {
        float[] in = new float[4];
        float[] out = new float[4];
        // Transform to normalized screen coordinates (-1 to 1).
        in[0] = ((winx - (float)m_aiViewport[0]) / (float)m_aiViewport[2]) * 2.0f - 1.0f;
        in[1] = ((winy - (float)m_aiViewport[1]) / (float) m_aiViewport[3]) * 2.0f - 1.0f;
        in[2] = p.constrain(winz, 0f, 1f) * 2.0f - 1.0f; in[3] = 1.0f;
        // Calculate homogeneous coordinates.
        out[0] = m_pMatrix.m00 * in[0] + m_pMatrix.m01 * in[1] + m_pMatrix.m02 * in[2] + m_pMatrix.m03 * in[3];
        out[1] = m_pMatrix.m10 * in[0] + m_pMatrix.m11 * in[1] + m_pMatrix.m12 * in[2] + m_pMatrix.m13 * in[3];
        out[2] = m_pMatrix.m20 * in[0] + m_pMatrix.m21 * in[1] + m_pMatrix.m22 * in[2] + m_pMatrix.m23 * in[3];
        out[3] = m_pMatrix.m30 * in[0] + m_pMatrix.m31 * in[1] + m_pMatrix.m32 * in[2] + m_pMatrix.m33 * in[3];
        if (out[3] == 0.0f) {
            // Check for an invalid result.
            result.x = 0.0f;
            result.y = 0.0f;
            result.z = 0.0f;
            return false;
        }
        // Scale to world coordinates.
        out[3] = 1.0f / out[3];
        result.x = out[0] * out[3];
        result.y = out[1] * out[3];
        result.z = out[2] * out[3];
        return true;
    }
    public boolean calculatePickPoints(float x, float y) {
        // Calculate positions on the near and far 3D frustum planes.
        m_bValid = true;
        // Have to do both in order to reset PVector on error.
        if (!gluUnProject((float)x, (float)y, 0.0f, ptStartPos)) m_bValid = false;
        if (!gluUnProject((float)x, (float)y, 1.0f, ptEndPos)) m_bValid = false;
        return m_bValid;
    }
}

/*
class Selection_in_OPENGL_A3D_Only {
    // Need to know Left/Right elevations.
    public static final int VIEW_Persp = 0;
    public static final int VIEW_Axon = 1;
    public static final int VIEW_Plan = 2;
    public static final int VIEW_Front = 3;
    public static final int VIEW_Right = 4;
    public static final int VIEW_Rear = 5;
    public static final int VIEW_Left = 6;
    // Store the near and far ray positions.
    public PVector ptStartPos = new PVector();
    public PVector ptEndPos = new PVector();
    // Internal matrices and projection type.
    protected int m_iProjection = VIEW_Persp;
    protected double[] m_adModelview = new double[16];
    protected double[] m_adProjection = new double[16];
    protected int[] m_aiViewport = new int[4];

    protected PApplet p;

    Selection_in_OPENGL_A3D_Only (PApplet p) {
        this.p = p;
    }

    public void captureViewMatrix(PGraphicsOpenGL pgl, int projection) {
        // Call this to capture the selection matrix after
        // you have called perspective() or ortho() and applied your
        // pan, zoom and camera angles - but before you start drawing
        // or playing with the matrices any further.
        if (pgl != null) {
            pgl.beginGL();
            m_iProjection = projection;
            pgl.gl.glGetDoublev(pgl.gl.GL_MODELVIEW_MATRIX, this.m_adModelview, 0);
            pgl.gl.glGetDoublev(pgl.gl.GL_PROJECTION_MATRIX, this.m_adProjection, 0);
            pgl.gl.glGetIntegerv(pgl.gl.GL_VIEWPORT, this.m_aiViewport, 0);
            pgl.endGL();
        }
    }

    public boolean calculatePickPoints(int x, int y, PGraphicsOpenGL pgl) {
        // Calculate positions on the near and far 3D frustum planes.
        if (pgl == null) {
            // Use main canvas if it is OPENGL or A3D.
            pgl = (PGraphicsOpenGL)p.g;
        }
        if (pgl != null) {
            double[] out = new double[4];
            pgl.glu.gluUnProject((double)x, (double)y, (double)0.0, this.m_adModelview, 0, this.m_adProjection, 0, this.m_aiViewport, 0, out, 0 );
            ptStartPos.set((float)out[0], (float)out[1], (float)out[2]);
            if ((m_iProjection != VIEW_Front) && (m_iProjection != VIEW_Rear)) ptStartPos.y = -ptStartPos.y;
            pgl.glu.gluUnProject((double)x, (double)y, (double)1.0, this.m_adModelview, 0, this.m_adProjection, 0, this.m_aiViewport, 0, out, 0 );
            ptEndPos.set((float)out[0], (float)out[1], (float)out[2]);
            if ((m_iProjection != VIEW_Front) && (m_iProjection != VIEW_Rear)) ptEndPos.y = -ptEndPos.y;
            return true;
        }
        return false;
    }
}
*/