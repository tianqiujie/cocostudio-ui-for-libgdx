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
package net.mwplay.cocostudio.ui.parser.group;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;

import net.mwplay.cocostudio.ui.widget.TCheckBox;
import net.mwplay.cocostudio.ui.CocoStudioUIEditor;
import net.mwplay.cocostudio.ui.model.ObjectData;
import net.mwplay.cocostudio.ui.parser.GroupParser;

/**
 * tip libgdx的CheckBox只有选中和未选中两个状态的图片显示
 */
public class CCCheckBox extends GroupParser {

    @Override
    public String getClassName() {
        return "CheckBoxObjectData";
    }

    @Override
    public Actor parse(CocoStudioUIEditor editor, ObjectData widget) {
        TCheckBox.CheckBoxStyle style = new TCheckBox.CheckBoxStyle(null, null, new BitmapFont(),
            Color.BLACK);

        if (widget.getNodeNormalFileData() != null) {// 选中图片

            style.checkboxOff = editor.findDrawable(widget,
                widget.getNodeNormalFileData());
        }
        if (widget.getNormalBackFileData() != null) {// 没选中图片
            style.checkboxOn = editor.findDrawable(widget,
                widget.getNormalBackFileData());
        }

        style.setTianCheckBox(editor.findDrawable(widget, widget.getNormalBackFileData())
            , editor.findDrawable(widget, widget.getPressedBackFileData())
            , editor.findDrawable(widget, widget.getDisableBackFileData())
            , editor.findDrawable(widget, widget.getNodeNormalFileData())
            , editor.findDrawable(widget, widget.getNodeDisableFileData()));

        TCheckBox checkBox = new TCheckBox("", style);
        checkBox.setChecked(widget.isDisplayState());
        checkBox.setDisabled(widget.isDisplayState());
        return checkBox;
    }
}
