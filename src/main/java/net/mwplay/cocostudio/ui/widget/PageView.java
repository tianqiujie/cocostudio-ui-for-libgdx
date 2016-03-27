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


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.utils.SnapshotArray;

public class PageView extends Table {
    public void initGestureListener() {
        this.addListener(new ActorGestureListener() {
            @Override
            public void fling(InputEvent event, float velocityX, float velocityY, int button) {
                if (Math.abs(velocityX) > Math.abs(velocityY)) {
                    if (velocityX > 0) {
                        previousView();
                    } else {
                        nextView();
                    }
                }
                super.fling(event, velocityX, velocityY, button);
            }
        });
    }

    public void nextView() {
        Gdx.app.debug("PageView", "Change to next view");
        SnapshotArray<Actor> children = this.getChildren();
        if (children.get(children.size - 1).getX() <= 0) {
            Gdx.app.debug("PageView", "Already last one, can't move to next.");
            return;
        }
        Actor[] actors = children.begin();
        float width = this.getWidth();
        for (Actor actor : actors) {
            if (actor != null) {
                actor.addAction(Actions.moveTo(actor.getX() - width, 0, 0.5f));
            }
        }
        children.end();
    }

    public void previousView() {
        Gdx.app.debug("PageView", "Change to previous view");
        SnapshotArray<Actor> children = this.getChildren();
        if (children.get(0).getX() >= 0) {
            Gdx.app.debug("PageView", "Already first one, can't move to previous.");
            return;
        }
        float width = this.getWidth();
        Actor[] actors = children.begin();
        for (Actor actor : actors) {
            if (actor != null) {
                actor.addAction(Actions.moveTo(actor.getX() + width, 0, 0.5f));
            }
        }
        children.end();
    }
}
