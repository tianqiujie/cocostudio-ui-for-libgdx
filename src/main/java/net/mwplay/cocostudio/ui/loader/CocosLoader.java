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

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.AsynchronousAssetLoader;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;

import net.mwplay.cocostudio.ui.CocoStudioUIEditor;

import java.util.List;

public class CocosLoader extends AsynchronousAssetLoader<CocosScene, CocosLoader.CocosParameter> {

    /**
     * Constructor, sets the {@link FileHandleResolver} to use to resolve the file associated with the asset name.
     *
     * @param resolver
     */
    public CocosLoader(FileHandleResolver resolver) {
        super(resolver);
    }

    CocosScene cocosScene;

    @Override
    public void loadAsync(AssetManager manager, String fileName, FileHandle file, CocosParameter parameter) {
        CocoStudioUIEditor cocoStudioUIEditor = new CocoStudioUIEditor(file, null, null, null, null);

        cocosScene = new CocosScene();
        cocosScene.setEditor(cocoStudioUIEditor);
    }

    @Override
    public CocosScene loadSync(AssetManager manager, String fileName, FileHandle file, CocosParameter parameter) {

        if (cocosScene != null) {
            return cocosScene;
        }

        CocoStudioUIEditor cocoStudioUIEditor = new CocoStudioUIEditor(file, null, null, null, null);
        CocosScene cocosScene = new CocosScene();
        cocosScene.setEditor(cocoStudioUIEditor);
        return cocosScene;
    }

    @Override
    public Array<AssetDescriptor> getDependencies(String fileName, FileHandle file, CocosParameter parameter) {
        Array<AssetDescriptor> assetDescriptors = new Array<>();
        List<String> list = CocoStudioUIEditor.getResources(file);
        for (String name : list) {
            assetDescriptors.add(new AssetDescriptor(CocoStudioUIEditor.dirName + name, Texture.class));
        }
        return assetDescriptors;
    }

    static public class CocosParameter extends AssetLoaderParameters<CocosScene> {
    }
}
