package net.mwplay.cocostudio.ui.model.timelines;

import java.util.List;

/**
 * Created by tian on 2016/3/20.
 */
public class CCTimelineData {
    private int ActionTag;
    private String Property;
    private String ctype;

    private List<CCTimelineFrame> Frames;

    public int getActionTag() {
        return ActionTag;
    }

    public void setActionTag(int actionTag) {
        ActionTag = actionTag;
    }

    public String getProperty() {
        return Property;
    }

    public void setProperty(String property) {
        Property = property;
    }

    public String getCtype() {
        return ctype;
    }

    public void setCtype(String ctype) {
        this.ctype = ctype;
    }

    public List<CCTimelineFrame> getFrames() {
        return Frames;
    }

    public void setFrames(List<CCTimelineFrame> frames) {
        Frames = frames;
    }
}
