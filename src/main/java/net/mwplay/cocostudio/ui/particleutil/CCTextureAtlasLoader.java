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
package net.mwplay.cocostudio.ui.particleutil;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.SynchronousAssetLoader;
import com.badlogic.gdx.assets.loaders.TextureAtlasLoader;
import com.badlogic.gdx.assets.loaders.TextureLoader;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;

public class CCTextureAtlasLoader extends SynchronousAssetLoader<TextureAtlas, TextureAtlasLoader.TextureAtlasParameter> {
    ObjectMap<String, Object> map;
    Texture texture;

    public CCTextureAtlasLoader(FileHandleResolver resolver) {
        super(resolver);
    }

    @Override
    public TextureAtlas load(AssetManager assetManager, String fileName, FileHandle file, TextureAtlasLoader.TextureAtlasParameter parameter) {
        TextureAtlas atlas = new TextureAtlas();
        atlas.getTextures().add(texture);
        ObjectMap<String, Object> frames = (ObjectMap<String, Object>) map.get("frames");
        for (ObjectMap.Entry<String, Object> entry : frames.entries()) {
            String pageName = entry.key;
            ObjectMap<String, Object> params = (ObjectMap<String, Object>) entry.value;
            Rectangle frame = LyU.parseRect((String) params.get("frame"));
            GridPoint2 offset = LyU.parsePoint((String) params.get("offset"));
            boolean rotated = Boolean.parseBoolean((String) params.get("rotated"));
            GridPoint2 sourceSize = LyU.parsePoint((String) params.get("sourceSize"));
            Rectangle sourceColorRect = LyU.parseRect((String) params.get("sourceColorRect"));
            TextureAtlas.TextureAtlasData.Region region = new TextureAtlas.TextureAtlasData.Region();
            region.name = pageName.substring(0, pageName.lastIndexOf('.'));
            region.rotate = rotated;
            region.offsetX = offset.x;
            region.offsetY = offset.y;
            region.originalWidth = sourceSize.x;
            region.originalHeight = sourceSize.y;
            region.left = (int) frame.x;
            region.top = (int) frame.y;
            region.width = (int) frame.getWidth();
            region.height = (int) frame.getHeight();
            int width = region.width;
            int height = region.height;
            TextureAtlas.AtlasRegion atlasRegion = new TextureAtlas.AtlasRegion(texture, region.left, region.top,
                region.rotate ? height : width, region.rotate ? width : height);
            atlasRegion.index = region.index;
            atlasRegion.name = region.name;
            atlasRegion.offsetX = region.offsetX;
            atlasRegion.offsetY = region.offsetY;
            atlasRegion.originalHeight = region.originalHeight;
            atlasRegion.originalWidth = region.originalWidth;
            atlasRegion.rotate = region.rotate;
            atlasRegion.splits = region.splits;
            atlasRegion.pads = region.pads;
            if (region.flip) {
                atlasRegion.flip(false, true);
            }
            atlas.getRegions().add(atlasRegion);
        }
        texture = null;
        map = null;
        return atlas;
    }

    @Override
    public Array<AssetDescriptor> getDependencies(String fileName, FileHandle file, TextureAtlasLoader.TextureAtlasParameter parameter) {
        FileHandle imgDir = file.parent();
        map = LyU.createDictionaryWithContentsOfFile(file);
        ObjectMap<String, Object> metadata = (ObjectMap<String, Object>) map.get("metadata");
        String dependFile = (String) metadata.get("textureFileName");
        Array<AssetDescriptor> res = new Array<AssetDescriptor>();
        TextureLoader.TextureParameter params = new TextureLoader.TextureParameter();
        params.magFilter = Texture.TextureFilter.Linear;
        params.minFilter = Texture.TextureFilter.Linear;
        params.format = Pixmap.Format.RGBA8888;
        texture = new Texture(imgDir.child(dependFile));
        res.add(new AssetDescriptor(imgDir.child(dependFile), Texture.class, params));
        return res;
    }
}
