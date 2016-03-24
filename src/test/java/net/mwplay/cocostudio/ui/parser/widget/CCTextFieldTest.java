package net.mwplay.cocostudio.ui.parser.widget;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import net.mwplay.cocostudio.ui.CocoStudioUIEditor;
import net.mwplay.cocostudio.ui.junit.LibgdxRunner;
import net.mwplay.cocostudio.ui.junit.NeedGL;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

@RunWith(LibgdxRunner.class)
public class CCTextFieldTest {
    @Test
    @NeedGL
    public void shouldParseTextField() throws Exception {
        FileHandle defaultFont = Gdx.files.internal("share/MLFZS.ttf");
        CocoStudioUIEditor editor = new CocoStudioUIEditor(
            Gdx.files.internal("textField/MainScene.json"), null, null, defaultFont, null);

        Group group = editor.createGroup();
        TextField textField = group.findActor("TextField_1");
        assertThat(textField.getText(), is("Here is text"));
        assertThat(textField.getMessageText(), is("Place Holder"));
        assertThat(textField.getText(), is("Here is text"));
        assertThat(textField.getColor().toString(), is("008000ff"));
        assertThat(textField.getListeners().size, is(1));
        textField = group.findActor("TextField_2");
        assertThat(textField.getText(), is(""));
        assertThat(textField.getMessageText(), is("Place Holder"));
        assertThat(textField.getColor().toString(), is("ff0000ff"));
        assertThat(textField.getListeners().size, is(1));
    }
}
