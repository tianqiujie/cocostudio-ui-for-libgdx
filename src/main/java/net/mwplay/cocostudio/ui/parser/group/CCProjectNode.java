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
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;

import net.mwplay.cocostudio.ui.CocoStudioUIEditor;
import net.mwplay.cocostudio.ui.model.ObjectData;
import net.mwplay.cocostudio.ui.parser.GroupParser;

public class CCProjectNode extends GroupParser {
    @Override
    public String getClassName() {
        return "ProjectNodeObjectData";
    }

    @Override
    public Actor parse(CocoStudioUIEditor editor, ObjectData widget) {
        if (widget.getFileData() == null){
            return new Group();
        }

        CocoStudioUIEditor cocoStudioUIEditor = new CocoStudioUIEditor(
            Gdx.files.internal(editor.getDirName() + widget.getFileData().getPath()),
            editor.getTtfs(), editor.getBitmapFonts(), editor.getDefaultFont(), editor.getTextureAtlas());
        return cocoStudioUIEditor.createGroup();
    }
}
