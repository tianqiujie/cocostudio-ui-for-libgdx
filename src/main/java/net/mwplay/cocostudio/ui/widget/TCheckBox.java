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

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Align;

public class TCheckBox extends TextButton {
    private Image image;
    private Image bg;
    private Cell imageCell;
    private CheckBoxStyle style;

    public TCheckBox(String text, Skin skin) {
        this(text, skin.get(CheckBoxStyle.class));
    }

    public TCheckBox(String text, Skin skin, String styleName) {
        this(text, skin.get(styleName, CheckBoxStyle.class));
    }

    public TCheckBox(String text, CheckBoxStyle style) {
        super(text, style);
        clearChildren();
        Label label = getLabel();
        imageCell = add(image = new Image(style.checkboxOff));
        //添加背景图片
        bg = new Image(style.noramlBack);

        add(label);
        label.setAlignment(Align.left);
        setSize(getPrefWidth(), getPrefHeight());
    }

    public void setStyle(ButtonStyle style) {
        if (!(style instanceof CheckBoxStyle)) {
            throw new IllegalArgumentException("style must be a CheckBoxStyle.");
        }
        super.setStyle(style);
        this.style = (CheckBoxStyle) style;
    }

    /**
     * Returns the checkbox's style. Modifying the returned style may not have an effect until {@link #setStyle(ButtonStyle)} is
     * called.
     */
    public CheckBoxStyle getStyle() {
        return style;
    }

    @Override
    public void setPosition(float x, float y) {
        super.setPosition(x, y);

        if (bg != null){
            bg.setPosition(x, y);
        }
    }

    @Override
    public void setPosition(float x, float y, int alignment) {
        super.setPosition(x, y, alignment);
        if (bg != null){
            bg.setPosition(x, y, alignment);
        }
    }

    public void draw(Batch batch, float parentAlpha) {
        if (bg != null){
            bg.draw(batch, parentAlpha);
        }

        Drawable checkbox = null;
        if (isDisabled()) {
            if (bg != null){
                bg.setDrawable(style.disableBack);
            }

            if (isChecked()) {
                checkbox = style.nodeDissable;
            } else {
                checkbox = null;
            }
        } else {
            if (isChecked() && style.checkboxOn != null) {
                checkbox = style.checkboxOn;
            } else if (isOver() && style.checkboxOver != null && !isDisabled()) {
                checkbox = style.checkboxOver;
            } else {
                checkbox = style.checkboxOff;
            }
        }

        /*if (checkbox == null) {
            if (isChecked() && style.checkboxOn != null)
                checkbox = style.checkboxOn;
            else if (isOver() && style.checkboxOver != null && !isDisabled())
                checkbox = style.checkboxOver;
            else
                checkbox = style.checkboxOff;
        }*/

        image.setDrawable(checkbox);
        super.draw(batch, parentAlpha);
    }

    @Override
    public void setScale(float scaleX, float scaleY) {
        super.setScale(scaleX, scaleY);

        if (bg != null) {
            bg.setScale(scaleX, scaleY);
        }
    }

    @Override
    public void setScale(float scaleXY) {
        super.setScale(scaleXY);
        if (bg != null) {
            bg.setScale(scaleXY);
        }
    }

    @Override
    public void setScaleX(float scaleX) {
        super.setScaleX(scaleX);
        if (bg != null) {
            bg.setScaleX(scaleX);
        }
    }

    @Override
    public void setScaleY(float scaleY) {
        super.setScaleY(scaleY);
        if (bg != null) {
            bg.setScaleY(scaleY);
        }
    }

    @Override
    public void setSize(float width, float height) {
        super.setSize(width, height);
        if (bg != null) {
            bg.setSize(width, height);
        }
    }

    @Override
    public void setWidth(float width) {
        super.setWidth(width);
        if (bg != null) {
            bg.setWidth(width);
        }
    }

    @Override
    public void setHeight(float height) {
        super.setHeight(height);
        if (bg != null) {
            bg.setHeight(height);
        }
    }

    @Override
    public void setOrigin(int alignment) {
        super.setOrigin(alignment);
        if (bg != null) {
            bg.setOrigin(alignment);
        }
    }

    @Override
    public void setOrigin(float originX, float originY) {
        super.setOrigin(originX, originY);
        if (bg != null) {
            bg.setOrigin(originX, originY);
        }
    }

    @Override
    public void setOriginX(float originX) {
        super.setOriginX(originX);
        if (bg != null) {
            bg.setOriginX(originX);
        }
    }

    @Override
    public void setOriginY(float originY) {
        super.setOriginY(originY);
        if (bg != null) {
            bg.setOriginY(originY);
        }
    }

    @Override
    public void addAction(Action action) {
        super.addAction(action);
        if (bg != null) {
            bg.addAction(action);
        }
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        if (bg != null) {
            bg.act(delta);
        }
    }

    public Image getImage() {
        return image;
    }

    public Cell getImageCell() {
        return imageCell;
    }

    /**
     * The style for a select box, see
     */
    static public class CheckBoxStyle extends TextButtonStyle {
        public Drawable checkboxOn, checkboxOff;
        /**
         * Optional.
         */
        public Drawable checkboxOver, checkboxOnDisabled, checkboxOffDisabled;
        public Drawable noramlBack, pressedBack, disableBack, nodeNoraml, nodeDissable;

        public CheckBoxStyle() {

        }

        /**
         * 设置背景
         *
         * @param noramlBack
         * @param pressedBack
         * @param disableBack
         * @param nodeNoraml
         * @param nodeDissable
         */
        public void setTianCheckBox(Drawable noramlBack, Drawable pressedBack, Drawable disableBack,
                                    Drawable nodeNoraml, Drawable nodeDissable) {
            this.noramlBack = noramlBack;
            this.pressedBack = pressedBack;
            this.disableBack = disableBack;
            this.nodeNoraml = nodeNoraml;
            this.nodeDissable = nodeDissable;
        }

        public CheckBoxStyle(Drawable checkboxOff, Drawable checkboxOn, BitmapFont font, Color fontColor) {
            this.checkboxOff = checkboxOff;
            this.checkboxOn = checkboxOn;
            this.font = font;
            this.fontColor = fontColor;
        }

        public CheckBoxStyle(CheckBoxStyle style) {
            this.checkboxOff = style.checkboxOff;
            this.checkboxOn = style.checkboxOn;
            this.checkboxOver = style.checkboxOver;
            this.checkboxOffDisabled = style.checkboxOffDisabled;
            this.checkboxOnDisabled = style.checkboxOnDisabled;
            this.font = style.font;
            this.fontColor = new Color(style.fontColor);
        }
    }
}
