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

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import net.mwplay.cocostudio.ui.CocoStudioUIEditor;
import net.mwplay.cocostudio.ui.model.CColor;
import net.mwplay.cocostudio.ui.model.ObjectData;
import net.mwplay.cocostudio.ui.parser.WidgetParser;

public class CCLabelBMFont extends WidgetParser {

    @Override
    public String getClassName() {
        return "TextBMFontObjectData";
    }

    @Override
    public Actor parse(CocoStudioUIEditor editor, ObjectData widget) {
        BitmapFont font = null;
        if (editor.getBitmapFonts() != null) {
            font = editor.getBitmapFonts().get(
                widget.getLabelBMFontFile_CNB().getPath());
        }
        if (font == null) {// 备用创建字体方式
            font = new BitmapFont(Gdx.files.internal(editor.getDirName()
                + widget.getLabelBMFontFile_CNB().getPath()));
        }

        if (font == null) {
            editor.debug(widget, "BitmapFont字体:"
                + widget.getLabelBMFontFile_CNB().getPath() + " 不存在");
            font = new BitmapFont();
        }

        CColor color = widget.getCColor();

        Color textColor = new Color(color.getR() / 255.0f,
            color.getG() / 255.0f, color.getB() / 255.0f,
            widget.getAlpha() / 255.0f);
        LabelStyle style = new LabelStyle(font, textColor);
        Label label = new Label(widget.getLabelText(), style);
        return label;
    }

}
