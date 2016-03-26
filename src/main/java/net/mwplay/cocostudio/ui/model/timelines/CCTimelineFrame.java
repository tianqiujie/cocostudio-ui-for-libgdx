/*
 * Copyright 2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.mwplay.cocostudio.ui.model.timelines;

import net.mwplay.cocostudio.ui.model.FileData;

public class CCTimelineFrame {
    private float X;
    private float Y;

    private int FrameIndex;
    private CCTimelineEasingData EasingData;
    private String ctype;

    private FileData TextureFile;

    private boolean Value;

    private boolean Tween;

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

    public FileData getTextureFile() {
        return TextureFile;
    }

    public void setTextureFile(FileData textureFile) {
        TextureFile = textureFile;
    }

    public boolean isTween() {
        return Tween;
    }

    public void setTween(boolean tween) {
        Tween = tween;
    }

    public boolean isValue() {
        return Value;
    }

    public void setValue(boolean value) {
        Value = value;
    }

    @Override
    public String toString() {
        return "CCTimelineFrame{" +
                "X=" + X +
                ", Y=" + Y +
                ", FrameIndex=" + FrameIndex +
                ", EasingData=" + EasingData +
                ", ctype='" + ctype + '\'' +
                ", TextureFile=" + TextureFile +
                ", Value=" + Value +
                ", Tween=" + Tween +
                '}';
    }
}
