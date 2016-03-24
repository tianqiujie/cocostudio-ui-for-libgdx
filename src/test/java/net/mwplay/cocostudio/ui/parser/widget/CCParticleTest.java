package net.mwplay.cocostudio.ui.parser.widget;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Group;
import net.mwplay.cocostudio.ui.CocoStudioUIEditor;
import net.mwplay.cocostudio.ui.junit.LibgdxRunner;
import net.mwplay.cocostudio.ui.junit.NeedGL;
import net.mwplay.cocostudio.ui.particleutil.CCParticleActor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.internal.util.reflection.Whitebox;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(LibgdxRunner.class)
public class CCParticleTest {
    @Test
    @NeedGL
    public void shouldParseParticle() throws Exception {
        CocoStudioUIEditor editor = new CocoStudioUIEditor(
            Gdx.files.internal("particle/MainScene.json"), null, null, null, null);

        Group group = editor.createGroup();
        CCParticleActor particleActor = group.findActor("Particle_1");
        Object modeA = Whitebox.getInternalState(particleActor, "modeA");
        Float speedVar = (Float) Whitebox.getInternalState(modeA, "speedVar");
        Float tangentialAccel = (Float) Whitebox.getInternalState(modeA, "tangentialAccel");
        Float tangentialAccelVar = (Float) Whitebox.getInternalState(modeA, "tangentialAccelVar");
        assertThat(speedVar, is(190.79f));
        assertThat(tangentialAccel, is(-92.11f));
        assertThat(tangentialAccelVar, is(65.79f));

        Object modeB = Whitebox.getInternalState(particleActor, "modeB");
        Float startRadius = (Float) Whitebox.getInternalState(modeB, "startRadius");
        Float endRadius = (Float) Whitebox.getInternalState(modeB, "endRadius");
        Float rotatePerSecond = (Float) Whitebox.getInternalState(modeB, "rotatePerSecond");
        assertThat(startRadius, is(0f));
        assertThat(endRadius, is(0f));
        assertThat(rotatePerSecond, is(0f));
    }
}
