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
package net.mwplay.cocostudio.ui.parser;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Touchable;

import net.mwplay.cocostudio.ui.BaseWidgetParser;
import net.mwplay.cocostudio.ui.CocoStudioUIEditor;
import net.mwplay.cocostudio.ui.model.ObjectData;

/**
 * 控件组转换器
 */
public abstract class GroupParser extends BaseWidgetParser {

    @Override
    public Actor commonParse(CocoStudioUIEditor editor, ObjectData widget,
                             Group parent, Actor actor) {
        Actor ac = super.commonParse(editor, widget, parent, actor);

        if (ac != null) {
            return ac;
        }
        return groupChildrenParse(editor, widget, parent, actor);
    }

    /**
     * 解析group控件,当前控件类型为Group的时候处理与Widget类型处理不同
     */
    public Group groupChildrenParse(CocoStudioUIEditor editor,
                                    ObjectData widget, Group parent, Actor actor) {

        Group group = (Group) actor;

        // Group 虽然自己不接收事件,但是子控件得接收
        actor.setTouchable(widget.isTouchEnable() ? Touchable.enabled
            : Touchable.childrenOnly);
        // 必须设置Transform 为true 子控件才会跟着旋转.

        // group.setTransform(true);

        if (widget.getScale() != null || widget.getRotation() != 0) {
            group.setTransform(true);
        }

        for (ObjectData childrenWidget : widget.getChildren()) {
            Actor childrenActor = editor.parseWidget(group, childrenWidget);
            if (childrenActor == null) {
                continue;
            }
            group.addActor(childrenActor);
        }
        sort(widget, group);

        return group;

    }

}
