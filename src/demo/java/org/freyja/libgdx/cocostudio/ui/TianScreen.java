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
package org.freyja.libgdx.cocostudio.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.StretchViewport;

public class TianScreen extends ScreenAdapter {
    public static final int GAME_WIDTH = 960;
    public static final int GAME_HEIGHT = 640;
    private Stage stage;

    @Override
    public void show() {
        super.show();
        stage = new Stage(new StretchViewport(GAME_WIDTH, GAME_HEIGHT));

        InputMultiplexer inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(stage);
        Gdx.input.setInputProcessor(inputMultiplexer);

        initDemo();
    }

    private void initDemo() {
        FileHandle defaultFont = Gdx.files
            .internal("demo/FangZhengZhunYuan_GBK.TTF");

        CocoStudioUIEditor editor = new CocoStudioUIEditor(
            Gdx.files.internal("MainScene.json"), null, null, defaultFont,
            null);

        Group group = editor.createGroup();
        stage.addActor(group);

        Actor login = editor.findActor("Button_1");
        login.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {

                System.out.println("click LOGIN");
                super.clicked(event, x, y);
            }
        });
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        stage.getViewport().update(width, height, false);
    }

}
