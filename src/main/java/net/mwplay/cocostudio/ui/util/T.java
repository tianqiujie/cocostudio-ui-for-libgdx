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
package net.mwplay.cocostudio.ui.util;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;

import net.mwplay.nativefont.MyWidget;

public class T {
    Actor actor;

    private static T instance;

    public T() {
    }

    public static T at(Actor actor) {
        //if (instance == null) {
        instance = new T();
        //}

        instance.actor = actor;
        return instance;
    }

    public T addTo(Group parent) {
        parent.addActor(actor);
        return this;
    }

    public T addTo(Stage stage) {
        stage.addActor(actor);
        return this;
    }

    public T size(float w, float h) {
        actor.setSize(w, h);
        return this;
    }

    public T size(Actor like) {
        actor.setSize(like.getWidth(), like.getHeight());
        return this;
    }

    public T pos(float x, float y) {
        actor.setPosition(x, y);
        return this;
    }

    public T posCenter(float x, float y) {
        actor.setPosition(x, y, Align.center);
        return this;
    }

    public T pos(float x, float y, int align) {
        actor.setPosition(x, y, align);
        return this;
    }

    public T toStageCenter(Stage stage) {
        actor.setPosition(stage.getWidth() / 2f, stage.getHeight() / 2f, Align.center);
        return this;
    }

    public T toStageXCenter(Stage stage) {
        //actor.setX(stage.getWidth() / 2f - actor.getWidth() / 2f);
        x(stage.getWidth() / 2f - actor.getWidth() / 2f);
        return this;
    }

    public T x(float x) {
        actor.setX(x);
        return this;
    }

    public T y(float y) {
        actor.setY(y);
        return this;
    }

    public T debug() {
        actor.debug();
        return this;
    }

    public T debugAll() {
        ((Group) actor).debugAll();
        return this;
    }

    public T drag() {
        MyWidget.setTouchTrack(actor);
        return this;
    }

    public T offsetY(float offset) {
        actor.setY(actor.getY() + offset);
        return this;
    }

    public T offsetX(float offset) {
        actor.setX(actor.getX() + offset);
        return this;
    }

    public T hide() {
        actor.setVisible(false);
        return this;
    }

    public T visiable() {
        actor.setVisible(true);
        return this;
    }

    public T pos(Actor ac) {
        actor.setPosition(ac.getX(), ac.getY());
        return this;
    }

    public T name(Object name) {
        actor.setName(name.toString());
        return this;
    }

    public T print() {
        LogUtil.log(actor.getX() + " " + actor.getY());
        return this;
    }

    public T pos(Vector2 mMajPos) {
        actor.setPosition(mMajPos.x, mMajPos.y);
        return this;
    }

    public float centerX() {
        return actor.getX(Align.center);
    }

    public float centerY() {
        return actor.getY(Align.center);
    }

    public T originCenter() {
        actor.setOrigin(Align.center);
        return this;
    }

    public T isButton(final TClickListener clickListener) {
        actor.setTouchable(Touchable.enabled);
        actor.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                actor.addAction(Actions.sequence(Actions.scaleTo(0.9f, 0.9f, 0.1f), Actions.scaleTo(1, 1, 0.1f), Actions.run(new Runnable() {
                    @Override
                    public void run() {
                        if (clickListener != null) {
                            clickListener.onClick(actor);
                        }
                    }
                })));
            }
        });

        return this;
    }

    public T isColorButton(final TClickListener clickListener) {
        actor.setTouchable(Touchable.enabled);
        actor.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                actor.setColor(0.5f, 0.5f, 0.5f, 1f);
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
                actor.setColor(Color.WHITE);
                if (clickListener != null) {
                    clickListener.onClick(actor);
                }
            }
        });

        return this;
    }

    public interface TClickListener {
        void onClick(Actor actor);
    }
}
