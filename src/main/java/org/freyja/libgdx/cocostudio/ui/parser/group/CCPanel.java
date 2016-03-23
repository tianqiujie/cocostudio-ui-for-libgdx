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
package org.freyja.libgdx.cocostudio.ui.parser.group;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import org.freyja.libgdx.cocostudio.ui.CocoStudioUIEditor;
import org.freyja.libgdx.cocostudio.ui.model.ObjectData;
import org.freyja.libgdx.cocostudio.ui.model.Size;
import org.freyja.libgdx.cocostudio.ui.parser.GroupParser;

/**
 * @author i see
 * tip 还未支持单色背景属性, 背景图片在Cocostudio里面并不是铺满, 而是居中
 */
public class CCPanel extends GroupParser {

    @Override
    public String getClassName() {
        return "PanelObjectData";
    }

    @Override
    public Actor parse(CocoStudioUIEditor editor, ObjectData widget) {
        Table table = new Table();

        Size size = widget.getSize();
        if (widget.getComboBoxIndex() == 0) {// 无颜色

        } else if (widget.getComboBoxIndex() == 1) {// 单色

            Pixmap pixmap = new Pixmap((int) size.getX(), (int) size.getY(),
                Format.RGBA8888);

            pixmap.setColor(editor.getColor(widget.getSingleColor(),
                widget.getBackColorAlpha()));

            pixmap.fill();

            Drawable d = new TextureRegionDrawable(new TextureRegion(
                new Texture(pixmap)));
            table.setBackground(d);
            pixmap.dispose();

            // table.addActor(new Image(d));

        } else {// 渐变色

        }

        if (widget.getFileData() != null) {// Panel的图片并不是拉伸平铺的!!.但是这里修改为填充
            Drawable tr = editor.findDrawable(widget, widget.getFileData());
            if (tr != null) {
                Image bg = new Image(tr);
                bg.setPosition((size.getX() - bg.getWidth()) / 2,
                    (size.getY() - bg.getHeight()) / 2);
                // bg.setFillParent(true);
                bg.setTouchable(Touchable.disabled);

                table.addActor(bg);
            }
        }

        table.setClip(widget.isClipAble());

        return table;
    }

}
