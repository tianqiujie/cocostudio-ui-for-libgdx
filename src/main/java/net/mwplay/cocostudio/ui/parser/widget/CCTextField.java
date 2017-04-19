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
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldStyle;

import net.mwplay.cocostudio.ui.CocoStudioUIEditor;
import net.mwplay.cocostudio.ui.model.ObjectData;
import net.mwplay.cocostudio.ui.parser.WidgetParser;
//import net.mwplay.cocostudio.ui.util.FontUtil;
import net.mwplay.cocostudio.ui.widget.TTFLabelStyle;

public class CCTextField extends WidgetParser {

    @Override
    public String getClassName() {
        return "TextFieldObjectData";
    }

    /**
     * 默认文字,为了支持基本字符输入
     */
    final String defaultText = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890\"!`?'.,;:()[]{}<>|/@\\^$-%+=#_&~*";

    @Override
    public Actor parse(CocoStudioUIEditor editor, ObjectData widget) {

        final TTFLabelStyle labelStyle = editor.createLabelStyle(widget,
            widget.getPlaceHolderText(),
            editor.getColor(widget.getCColor(), 0));

        TextFieldStyle style = new TextFieldStyle(labelStyle.font,
            labelStyle.fontColor, null, null, null);

        TextField textField = new TextField(widget.getLabelText(), style) {

            @Override
            public void setText(String text) {
                String sumText = text + getMessageText() + defaultText;

                /*getStyle().font = FontUtil.createFont(
                        labelStyle.getFontFileHandle(), sumText,
                        labelStyle.getFontSize());*/

                super.setText(text);
            }

            @Override
            public void setMessageText(String messageText) {

                String sumText = messageText + getText() + defaultText;

                /*getStyle().font = FontUtil.createFont(
                    labelStyle.getFontFileHandle(), sumText,
                    labelStyle.getFontSize());*/
                super.setMessageText(messageText);
            }

        };

        textField.setMaxLength(widget.getMaxLengthText());
        textField.setMessageText(widget.getPlaceHolderText());
        textField.setPasswordMode(widget.isPasswordEnable());
        textField.setPasswordCharacter(widget.getPasswordStyleText());
        return textField;
    }
}
