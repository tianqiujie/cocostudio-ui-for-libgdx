package org.freyja.libgdx.cocostudio.ui.parser.widget;

import com.badlogic.gdx.scenes.scene2d.Actor;

import org.freyja.libgdx.cocostudio.ui.CocoStudioUIEditor;
import org.freyja.libgdx.cocostudio.ui.model.ObjectData;
import org.freyja.libgdx.cocostudio.ui.parser.WidgetParser;
import org.freyja.libgdx.cocostudio.ui.particleutil.CCParticleActor;

/**
 * Created by tian on 2016/3/18.
 */
public class CCParticle extends WidgetParser{
    @Override
    public String getClassName() {
        return "ParticleObjectData";
    }

    @Override
    public Actor parse(CocoStudioUIEditor editor, ObjectData widget) {
        CCParticleActor ccParticleActor = new CCParticleActor(editor.findParticePath(
                widget.getFileData().getPath()));
        ccParticleActor.setBlendAdditive(true);


        return ccParticleActor;
    }
}


