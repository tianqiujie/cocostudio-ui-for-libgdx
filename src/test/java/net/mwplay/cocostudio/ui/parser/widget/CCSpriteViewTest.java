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
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.actions.ParallelAction;
import com.badlogic.gdx.scenes.scene2d.actions.RepeatAction;
import com.badlogic.gdx.scenes.scene2d.actions.ScaleToAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Array;
import net.mwplay.cocostudio.ui.CocoStudioUIEditor;
import net.mwplay.cocostudio.ui.junit.LibgdxRunner;
import net.mwplay.cocostudio.ui.junit.NeedGL;
import org.hamcrest.Matcher;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.CoreMatchers.everyItem;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

@RunWith(LibgdxRunner.class)
public class CCSpriteViewTest {
    @Test
    @NeedGL
    public void shouldParseSpriteView() throws Exception {
        CocoStudioUIEditor editor = new CocoStudioUIEditor(
            Gdx.files.internal("animation/MainScene.json"), null, null, null, null);

        Group group = editor.createGroup();
        Image image = group.findActor("st_2");
        Array<Action> actions = image.getActions();
        assertThat(actions.size, is(1));
        RepeatAction repeatAction = (RepeatAction) actions.get(0);
        ParallelAction parallelAction = (ParallelAction) repeatAction.getAction();
        assertThat(parallelAction.getActions().size, is(2));
        assertThat(parallelAction.getActions(), (Matcher) everyItem(instanceOf(SequenceAction.class)));
        SequenceAction moveAction = (SequenceAction) parallelAction.getActions().get(0);
        SequenceAction scaleAction = (SequenceAction) parallelAction.getActions().get(1);
        assertThat(moveAction.getActions().size, is(4));
        assertThat(moveAction.getActions(), (Matcher) everyItem(instanceOf(MoveToAction.class)));
        assertThat(scaleAction.getActions().size, is(4));
        assertThat(scaleAction.getActions(), (Matcher) everyItem(instanceOf(ScaleToAction.class)));
    }
}
