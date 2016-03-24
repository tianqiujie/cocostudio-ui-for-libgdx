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
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane.ScrollPaneStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import net.mwplay.cocostudio.ui.CocoStudioUIEditor;
import net.mwplay.cocostudio.ui.model.ObjectData;
import net.mwplay.cocostudio.ui.parser.GroupParser;

/**
 *         tip 滚动方向, 回弹滚动支持不是很好
 */
public class CCScrollView extends GroupParser {

    @Override
    public String getClassName() {
        return "ScrollViewObjectData";
    }

    @Override
    public Actor parse(CocoStudioUIEditor editor, ObjectData widget) {
        ScrollPaneStyle style = new ScrollPaneStyle();

        if (widget.getFileData() != null) {

            style.background = editor
                .findDrawable(widget, widget.getFileData());
        }

        ScrollPane scrollPane = new ScrollPane(null, style);

        if ("Vertical_Horizontal".equals(widget.getScrollDirectionType())) {
            scrollPane.setForceScroll(true, true);
        } else if ("Horizontal".equals(widget.getScrollDirectionType())) {
            scrollPane.setForceScroll(true, false);
        } else if ("Vertical".equals(widget.getScrollDirectionType())) {
            scrollPane.setForceScroll(false, true);
        }

        scrollPane.setClamp(widget.isClipAble());
        scrollPane.setFlickScroll(widget.isIsBounceEnabled());

        Table table = new Table();
        table.setSize(widget.getInnerNodeSize().getWidth(), widget
            .getInnerNodeSize().getHeight());

        if (widget.getComboBoxIndex() == 0) {// 无颜色

        } else if (widget.getComboBoxIndex() == 1) {// 单色

            Pixmap pixmap = new Pixmap((int) table.getWidth(),
                (int) table.getHeight(), Format.RGBA8888);
            Color color = editor.getColor(widget.getSingleColor(),
                widget.getBackColorAlpha());

            pixmap.setColor(color);

            pixmap.fill();

            Drawable drawable = new TextureRegionDrawable(new TextureRegion(
                new Texture(pixmap)));

            table.setBackground(drawable);
            pixmap.dispose();

        }
        scrollPane.setWidget(table);
        return scrollPane;
    }

    @Override
    public Group groupChildrenParse(CocoStudioUIEditor editor,
                                    ObjectData widget, Group parent, Actor actor) {
        ScrollPane scrollPane = (ScrollPane) actor;
        Table table = new Table();
        for (ObjectData childrenWidget : widget.getChildren()) {
            Actor childrenActor = editor.parseWidget(table, childrenWidget);
            if (childrenActor == null) {
                continue;
            }

            table.setSize(Math.max(table.getWidth(), childrenActor.getRight()),
                Math.max(table.getHeight(), childrenActor.getTop()));
            table.addActor(childrenActor);
        }
        sort(widget, table);
        //

        scrollPane.setWidget(table);

        return scrollPane;
    }

}
