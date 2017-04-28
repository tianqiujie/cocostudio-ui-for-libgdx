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
package net.mwplay.cocostudio.ui.parser.widget;

import com.badlogic.gdx.scenes.scene2d.Actor;

import net.mwplay.cocostudio.ui.CocoStudioUIEditor;
import net.mwplay.cocostudio.ui.model.ObjectData;
import net.mwplay.cocostudio.ui.parser.GroupParser;
import net.mwplay.cocostudio.ui.widget.TSlider;
//import net.mwplay.cocostudio.ui.widget.TSlider;

public class CCSlider extends GroupParser {

    @Override
    public String getClassName() {
        return "SliderObjectData";
    }

    @Override
    public Actor parse(CocoStudioUIEditor editor, ObjectData widget) {

        /*SliderStyle style = new SliderStyle(
            editor.findDrawable(widget, widget.getBackGroundData()),
            editor.findDrawable(widget, widget.getBallNormalData()));

        if (widget.getProgressBarData() != null) {
            style.knobBefore = editor.findDrawable(widget, widget.getProgressBarData());
        }
        if (widget.getBallDisabledData() != null) {
            style.disabledKnob = editor.findDrawable(widget, widget.getBallDisabledData());
        }
        if (widget.getBallPressedData() != null) {
            style.knobDown = editor.findDrawable(widget, widget.getBallPressedData());
        }

        float percent = widget.getPercentInfo();

        Slider slider = new Slider(0.1f, 100f, 0.1f, false, style);
        slider.setValue(percent);*/

        /*TSlider slider = new TSlider(editor.findTextureRegion(widget, widget.getBackGroundData().getPath()),
            editor.findTextureRegion(widget, widget.getProgressBarData().getPath()),
            editor.findTextureRegion(widget, widget.getBallNormalData().getPath()));
        slider.setValue(widget.getPercentInfo());*/
        TSlider mySlider = new TSlider(0, 100, 1, false, editor.findTextureRegion(widget, widget.getBackGroundData().getPath()),
            editor.findTextureRegion(widget, widget.getBallNormalData().getPath()),
            editor.findTextureRegion(widget, widget.getProgressBarData().getPath()));
        mySlider.setValue(widget.getPercentInfo());
        return mySlider;
    }

}
