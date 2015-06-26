package netiko.stage;



public class TextUserInfo extends Text {

    public TextUserInfo (String text, float x, float y, float size) {
        super(text, x, y, size);
    }

    @Override
    public void draw() {
        // the same as for text just don't flip
        p.pushStyle();
        p.pushMatrix();
        p.fill(textColor);
        p.textSize(size);
        p.text(text, x, y);
        p.popMatrix();
        p.popStyle();
    }

}
