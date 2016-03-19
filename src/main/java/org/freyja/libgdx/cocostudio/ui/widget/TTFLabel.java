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
package org.freyja.libgdx.cocostudio.ui.widget;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import org.freyja.libgdx.cocostudio.ui.util.FontUtil;

/**
 * 让Label支持TTF,使用ttf后Label的font不会发生变化,每次修改Text的时候重新创建font
 *
 * @author i see
 */
public class TTFLabel extends Label {

    public TTFLabel(CharSequence text, TTFLabelStyle ttfLabelStyle) {
        super(text, ttfLabelStyle);
    }

    @Override
    public void setText(CharSequence newText) {
        LabelStyle style = getStyle();
        style.font = createFont((TTFLabelStyle) style, "" + newText);

        super.setStyle(style);
        super.setText(newText);
    }

    public int labelAlign;

    public int lineAlign;

    @Override
    public void setAlignment(int labelAlign, int lineAlign) {
        this.labelAlign = labelAlign;
        this.lineAlign = lineAlign;
        super.setAlignment(labelAlign, lineAlign);
    }

    @Override
    public void setStyle(LabelStyle style) {
        style.font = createFont((TTFLabelStyle) style, "" + getText());

        super.setStyle(style);
    }

    BitmapFont createFont(TTFLabelStyle ttfStyle, String text) {
        return FontUtil.createFont(ttfStyle.getFontFileHandle(), text,
            ttfStyle.getFontSize());
    }
}
