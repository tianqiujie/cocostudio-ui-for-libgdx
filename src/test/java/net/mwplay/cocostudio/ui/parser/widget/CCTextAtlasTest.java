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
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import net.mwplay.cocostudio.ui.CocoStudioUIEditor;
import net.mwplay.cocostudio.ui.junit.LibgdxRunner;
import net.mwplay.cocostudio.ui.junit.NeedGL;
import net.mwplay.cocostudio.ui.widget.LabelAtlas;
import org.hamcrest.Matcher;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

@RunWith(LibgdxRunner.class)
public class CCTextAtlasTest {
    @Test
    @NeedGL
    public void shouldParseTextAtlas() throws Exception {
        CocoStudioUIEditor editor = new CocoStudioUIEditor(
            Gdx.files.internal("levelSelection/MainScene.json"), null, null, null, null);

        Group group = editor.createGroup();
        LabelAtlas labelAtlas = group.findActor("LabelAtlas_CurrentScene");
        assertThat(labelAtlas.getChildren().size, is(3));
        assertThat(labelAtlas.getChildren(), (Matcher) everyItem(instanceOf(Image.class)));
        labelAtlas.setText("0/1");
        assertThat(labelAtlas.getChildren().size, is(3));
        assertThat(labelAtlas.getChildren(), (Matcher) everyItem(instanceOf(Image.class)));
    }
}
