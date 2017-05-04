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
package net.mwplay.cocostudio.ui.loader;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.scenes.scene2d.Group;

import net.mwplay.cocostudio.ui.CocoStudioUIEditor;
public class CocosScene {
    private CocoStudioUIEditor editor;

    public CocosScene() {

    }

    private Group root;

    public Group getRoot(AssetManager assetManager) {
        if (root == null) {
            root = editor.createGroup(assetManager);
        }
        return root;
    }

    public Group getRoot() {
        if (root == null) {
            root = editor.createGroup();
        }
        return root;
    }

    public void setRoot(Group root) {
        this.root = root;
    }

    public void setEditor(CocoStudioUIEditor editor) {
        this.editor = editor;
    }

    public CocoStudioUIEditor getEditor() {
        return editor;
    }
}
