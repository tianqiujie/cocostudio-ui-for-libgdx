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
package org.freyja.libgdx.cocostudio.ui.util;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;

import java.util.HashMap;
import java.util.Map;

public class FontUtil {

    static FreeTypeFontGenerator generator;
    static Map<FileHandle, FreeTypeFontGenerator> generators = new HashMap<FileHandle, FreeTypeFontGenerator>();

    /**
     * 缓存FreeTypeFontGenerator 对性能有显著提升
     *
     * @param fontHandle
     * @param text
     * @param fontSize
     * @return
     */
    public static BitmapFont createFont(FileHandle fontHandle, String text,
                                        int fontSize) {

        if (fontHandle == null) {
            return new BitmapFont();
        }

        BitmapFont font = null;
        // FreeTypeFontGenerator generator = null;
        try {

            generator = generators.get(fontHandle);
            if (generator == null) {
                generator = new FreeTypeFontGenerator(fontHandle);
                generators.put(fontHandle, generator);
            }

            String newText = StringUtil.removeRepeatedChar(text);

            FreeTypeFontParameter parameter = new FreeTypeFontParameter();

            parameter.size = fontSize;
            parameter.characters += newText;
            font = generator.generateFont(parameter);

//			font = generator.generateFont(fontSize, newText, false);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // if (generator != null) {
            // generator.dispose();
            // }
        }
        if (font == null) {
            return new BitmapFont();
        }
        return font;

    }

    /**
     * 缓存FreeTypeFontGenerator 对性能有显著提升
     *
     * @param fontHandle
     * @param text
     * @param fontSize
     * @return
     */
    public static BitmapFont createFont(FileHandle fontHandle, String text,
                                        int fontSize, Color color) {

        if (fontHandle == null) {
            return new BitmapFont();
        }

        BitmapFont font = null;
        // FreeTypeFontGenerator generator = null;
        try {

            generator = generators.get(fontHandle);
            if (generator == null) {
                generator = new FreeTypeFontGenerator(fontHandle);
                generators.put(fontHandle, generator);
            }

            String newText = StringUtil.removeRepeatedChar(text);

            FreeTypeFontParameter parameter = new FreeTypeFontParameter();

            parameter.size = fontSize;
            parameter.characters += newText;
            parameter.color = color;
            font = generator.generateFont(parameter);

//			font = generator.generateFont(fontSize, newText, false);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // if (generator != null) {
            // generator.dispose();
            // }
        }
        if (font == null) {
            return new BitmapFont();
        }
        return font;

    }
}
