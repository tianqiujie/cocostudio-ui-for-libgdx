package org.freyja.libgdx.cocostudio.ui.model.timelines;

/**
 * Created by tian on 2016/3/20.
 */
public class CCTimelineFrame {
    private float X;
    private float Y;

    private int FrameIndex;
    private CCTimelineEasingData EasingData;
    private String ctype;

    public float getX() {
        return X;
    }

    public void setX(float x) {
        X = x;
    }

    public float getY() {
        return Y;
    }

    public void setY(float y) {
        Y = y;
    }

    public int getFrameIndex() {
        return FrameIndex;
    }

    public void setFrameIndex(int frameIndex) {
        FrameIndex = frameIndex;
    }

    public CCTimelineEasingData getEasingData() {
        return EasingData;
    }

    public void setEasingData(CCTimelineEasingData easingData) {
        EasingData = easingData;
    }

    public String getCtype() {
        return ctype;
    }

    public void setCtype(String ctype) {
        this.ctype = ctype;
    }
}
