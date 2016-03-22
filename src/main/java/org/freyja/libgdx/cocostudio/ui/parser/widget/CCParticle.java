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
package org.freyja.libgdx.cocostudio.ui.parser.widget;

import com.badlogic.gdx.scenes.scene2d.Actor;
import org.freyja.libgdx.cocostudio.ui.CocoStudioUIEditor;
import org.freyja.libgdx.cocostudio.ui.model.ObjectData;
import org.freyja.libgdx.cocostudio.ui.parser.WidgetParser;
import org.freyja.libgdx.cocostudio.ui.particleutil.CCParticleActor;

/**
 * Created by tian on 2016/3/18.
 */
public class CCParticle extends WidgetParser {
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


