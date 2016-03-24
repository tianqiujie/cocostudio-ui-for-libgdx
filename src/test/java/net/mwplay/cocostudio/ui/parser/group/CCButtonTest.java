package net.mwplay.cocostudio.ui.parser.group;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import net.mwplay.cocostudio.ui.CocoStudioUIEditor;
import net.mwplay.cocostudio.ui.junit.LibgdxRunner;
import net.mwplay.cocostudio.ui.junit.NeedGL;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

@RunWith(LibgdxRunner.class)
public class CCButtonTest {
    @Test
    @NeedGL
    public void shouldParseSingleButtonWithImages() throws Exception {
        FileHandle defaultFont = Gdx.files.internal("share/MLFZS.ttf");

        CocoStudioUIEditor editor = new CocoStudioUIEditor(
            Gdx.files.internal("single-button/MainScene.json"), null, null, defaultFont, null);

        Group group = editor.createGroup();
        Actor actor = group.findActor("Button_1");

        assertThat(actor, not(nullValue()));
        assertThat(actor, instanceOf(ImageButton.class));
        ImageButton imageButton = (ImageButton) actor;
        assertThat(imageButton.getScaleX(), is(1.7958f));
        assertThat(imageButton.getScaleY(), is(1.8041f));
        ImageButton.ImageButtonStyle style = imageButton.getStyle();
        assertThat(style.imageDisabled, instanceOf(NinePatchDrawable.class));
        assertThat(style.up, instanceOf(NinePatchDrawable.class));
        assertThat(style.down, instanceOf(NinePatchDrawable.class));
    }
}
