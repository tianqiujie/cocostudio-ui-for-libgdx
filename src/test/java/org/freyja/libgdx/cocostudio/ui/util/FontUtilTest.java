package org.freyja.libgdx.cocostudio.ui.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import org.freyja.libgdx.cocostudio.ui.junit.LibgdxRunner;
import org.freyja.libgdx.cocostudio.ui.junit.NeedGL;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

@RunWith(LibgdxRunner.class)
public class FontUtilTest {
    @Test
    @NeedGL
    public void shouldAbleGenerateBitmapFontFromTTF() throws Exception {
        FileHandle defaultFont = Gdx.files.internal("share/MLFZS.ttf");
        String text = "This is a test. 这是一个测试。これはテストです.";
        BitmapFont bitmapFont = FontUtil.createFont(defaultFont, text, 14);

        assertThat(bitmapFont, not(nullValue()));
        for (char c : text.toCharArray()) {
            BitmapFont.Glyph glyph = bitmapFont.getData().getGlyph(c);
            assertThat(glyph, not(nullValue()));
            assertThat(glyph.id, not(nullValue()));
        }
    }

    @Test
    @NeedGL
    public void shouldUnableToGenerateBitmapIfCharNotInTTF() throws Exception {
        FileHandle defaultFont = Gdx.files.internal("share/MLFZS.ttf");
        String text = "มันเป็นการทดสอบ";
        BitmapFont bitmapFont = FontUtil.createFont(defaultFont, text, 14);

        assertThat(bitmapFont, not(nullValue()));
        for (char c : text.toCharArray()) {
            BitmapFont.Glyph glyph = bitmapFont.getData().getGlyph(c);
            try {
                assertThat(glyph, is(nullValue()));
            } catch (AssertionError ignored) {
                //Different behaviour for 1.7.0
                assertThat(glyph.id, is(127));
            }
        }
    }
}
