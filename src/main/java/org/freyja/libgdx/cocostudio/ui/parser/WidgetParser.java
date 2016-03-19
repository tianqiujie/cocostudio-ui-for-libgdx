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
package org.freyja.libgdx.cocostudio.ui.parser;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import org.freyja.libgdx.cocostudio.ui.BaseWidgetParser;
import org.freyja.libgdx.cocostudio.ui.CocoStudioUIEditor;
import org.freyja.libgdx.cocostudio.ui.model.ObjectData;
import org.freyja.libgdx.cocostudio.ui.model.Scale;

/**
 * 单控件转换器
 *
 * @author i see
 */
public abstract class WidgetParser extends BaseWidgetParser {

    @Override
    public Actor commonParse(CocoStudioUIEditor editor, ObjectData widget,
                             Group parent, Actor actor) {
        Actor ac = super.commonParse(editor, widget, parent, actor);
        if (ac != null) {
            return ac;
        }
        return widgetChildrenParse(editor, widget, parent, actor);
    }

    /**
     * 解析子控件
     */
    public Group widgetChildrenParse(CocoStudioUIEditor editor,
                                     ObjectData widget, Group parent, Actor actor) {
        Table table = new Table();
        table.setClip(widget.isClipAble());
        table.setName(actor.getName());

        Scale scale = widget.getScale();

        if (scale != null) {
            table.setScale(scale.getScaleX(), scale.getScaleY());
        }

        table.setRotation(actor.getRotation());
        table.setVisible(actor.isVisible());

        table.setTouchable(widget.isTouchEnable() ? Touchable.enabled
            : Touchable.childrenOnly);

        // editor.getActors().get(actor.getName()).removeValue(actor, true);
        //
        // addActor(editor, table, option);

        actor.setVisible(true);
        actor.setTouchable(Touchable.disabled);

        if (scale != null || widget.getRotation() != 0) {
            table.setTransform(true);
        }

        table.setSize(actor.getWidth(), actor.getHeight());
        table.setPosition(actor.getX(), actor.getY());

        // 锚点就是子控件的锚点

        Scale anchorPoint = widget.getAnchorPoint();
        if (anchorPoint != null) {

            table.setOrigin(anchorPoint.getScaleX() * table.getWidth(),
                anchorPoint.getScaleY() * table.getHeight());
        }
        for (ObjectData childrenWidget : widget.getChildren()) {
            Actor childrenActor = editor.parseWidget(table, childrenWidget);
            if (childrenActor == null) {
                continue;
            }
            table.addActor(childrenActor);
        }
        sort(widget, table);

        // Widget的位置应该与Table重合.相当于Widget的属性被移植到了Table
        actor.setPosition(0, 0);
        actor.setScale(1, 1);
        table.addActorAt(0, actor);
        return table;
    }

}
