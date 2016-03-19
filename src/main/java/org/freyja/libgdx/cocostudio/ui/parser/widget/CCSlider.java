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
package org.freyja.libgdx.cocostudio.ui.parser.widget;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Slider.SliderStyle;
import org.freyja.libgdx.cocostudio.ui.CocoStudioUIEditor;
import org.freyja.libgdx.cocostudio.ui.model.ObjectData;
import org.freyja.libgdx.cocostudio.ui.parser.GroupParser;

/**
 * 滑动条
 *
 * @author i see
 */
public class CCSlider extends GroupParser {

    @Override
    public String getClassName() {
        return "SliderObjectData2";
    }

    @Override
    public Actor parse(CocoStudioUIEditor editor, ObjectData widget) {

        SliderStyle style = new SliderStyle(editor.findDrawable(widget,
            widget.getBackGroundData()), editor.findDrawable(widget,
            widget.getBallNormalData()));


        style.knob = editor.findDrawable(widget,
            widget.getProgressBarData());
        style.disabledBackground = editor.findDrawable(widget,
            widget.getBallDisabledData());
        if (widget.getProgressBarData() != null) {
            style.knobBefore = editor.findDrawable(widget,
                widget.getProgressBarData());
        }
        if (widget.getBallDisabledData() != null) {
            style.disabledKnob = editor.findDrawable(widget,
                widget.getBallDisabledData());
        }
        // 这里滑动条只支持1以上?

        float percent = widget.getPercentInfo();

        // if (percent <= 0) {// 进度不能小于等于0
        // percent = 0.1f;
        // }
        Slider slider = new Slider(0.1f, 100f, 0.1f, false, style);
        slider.setValue(percent);
        return slider;
    }

}
