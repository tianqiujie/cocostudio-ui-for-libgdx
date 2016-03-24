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
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import net.mwplay.cocostudio.ui.CocoStudioUIEditor;
import net.mwplay.cocostudio.ui.junit.LibgdxRunner;
import net.mwplay.cocostudio.ui.junit.NeedGL;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(LibgdxRunner.class)
public class CCSliderTest {
    @Test
    @NeedGL
    public void shouldParseSliderBar() throws Exception {
        CocoStudioUIEditor editor = new CocoStudioUIEditor(
            Gdx.files.internal("slideBar/MainScene.json"), null, null, null, null);

        Group group = editor.createGroup();
        Slider slider = group.findActor("Slider_1");
        assertThat(slider.getWidth(), is(200f));
        assertThat(slider.getHeight(), is(14f));
        assertThat(slider.getValue(), is(50f));
        Slider.SliderStyle style = slider.getStyle();
        assertThat(style.knob, instanceOf(TextureRegionDrawable.class));
        assertThat(style.background, instanceOf(TextureRegionDrawable.class));
        assertThat(style.knobBefore, instanceOf(TextureRegionDrawable.class));
        assertThat(style.disabledKnob, instanceOf(TextureRegionDrawable.class));
        assertThat(style.knobDown, instanceOf(TextureRegionDrawable.class));
    }
}
