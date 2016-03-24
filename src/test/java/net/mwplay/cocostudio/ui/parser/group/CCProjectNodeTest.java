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

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import net.mwplay.cocostudio.ui.CocoStudioUIEditor;
import net.mwplay.cocostudio.ui.junit.LibgdxRunner;
import net.mwplay.cocostudio.ui.junit.NeedGL;
import org.hamcrest.Matcher;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.CoreMatchers.everyItem;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

@RunWith(LibgdxRunner.class)
public class CCProjectNodeTest {
    @Test
    @NeedGL
    public void shouldEnableParseProjectNode() throws Exception {
        FileHandle defaultFont = Gdx.files.internal("share/MLFZS.ttf");

        CocoStudioUIEditor editor = new CocoStudioUIEditor(
            Gdx.files.internal("mainMenu/MainScene.json"), null, null, defaultFont, null);

        Group group = editor.createGroup();
        Actor button = group.findActor("Bnss_2");
        assertThat(button, not(nullValue()));
    }

    @Test
    @NeedGL
    public void shouldGetCorrectTouchableConfig() throws Exception {
        FileHandle defaultFont = Gdx.files.internal("share/MLFZS.ttf");

        CocoStudioUIEditor editor = new CocoStudioUIEditor(
            Gdx.files.internal("mainMenu/MainScene.json"), null, null, defaultFont, null);

        Group group = editor.createGroup();
        assertThat(group.getTouchable(), is(Touchable.childrenOnly));
        Group subGroup = (Group) group.getChildren().get(0);
        assertThat(subGroup.getTouchable(), is(Touchable.childrenOnly));
        assertThat(subGroup.getChildren(), (Matcher) everyItem(hasProperty("touchable", is(true))));
    }
}
