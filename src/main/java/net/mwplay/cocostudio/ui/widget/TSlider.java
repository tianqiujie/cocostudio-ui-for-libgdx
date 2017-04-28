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
package net.mwplay.cocostudio.ui.widget;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class TSlider extends Slider {
    public TSlider(
        float min,
        float max,
        float stepSize,
        boolean vertical,
        TextureRegion background,
        TextureRegion knob,
        TextureRegion knobBefore) {
        super(min, max, stepSize, vertical, new SliderStyle(new TextureRegionDrawable(background), new TextureRegionDrawable(knob)));
        this.getStyle().knobBefore = new TextureRegionDrawable(knobBefore);
    }

    public TSlider(float min, float max, float stepSize, boolean vertical, Texture background, Texture knob, Texture knobBefore) {
        this(min, max, stepSize, vertical, new TextureRegion(background), new TextureRegion(knob), new TextureRegion(knobBefore));
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        //现在只写了横着的
        float position = 0;
        if (getMinValue() != getMaxValue()) {
            float knobWidthHalf = 0;
            float positionWidth = getWidth();
            float knobWidth = getStyle().knob == null ? 0 : getStyle().knob.getMinWidth();
            float bgLeftWidth = 0;
            if (getStyle().background != null) {
                bgLeftWidth = getStyle().background.getLeftWidth();
                positionWidth -= bgLeftWidth + getStyle().background.getRightWidth();
            }
            if (getStyle().knob == null) {
                knobWidthHalf = getStyle().knobBefore == null ? 0 : getStyle().knobBefore.getMinWidth() * 0.5f;
                position = (positionWidth - knobWidthHalf) * getVisualPercent();
                position = Math.min(positionWidth - knobWidthHalf, position);
            } else {
                knobWidthHalf = knobWidth * 0.5f;
                position = (positionWidth - knobWidth) * getVisualPercent();
                position = Math.min(positionWidth - knobWidth, position) + bgLeftWidth;
            }
            position = Math.max(0, position);
        }
        ((TextureRegionDrawable) getStyle().knobBefore).getRegion().setRegionWidth(
            (int) (position + (getKnobDrawable() == null ? 0 : getKnobDrawable().getMinWidth()) * 0.5f));
        super.draw(batch, parentAlpha);
    }
}
