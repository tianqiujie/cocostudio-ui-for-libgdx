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
package net.mwplay.cocostudio.ui;

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

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

public class DemoScreen extends ScreenAdapter {
    private Stage stage;
    private List<String> demos;
    private FileHandle defaultFont;
    private int currentIndex;

    @Override
    public void show() {
        super.show();
        stage = new Stage(new StretchViewport(DemoGame.GAME_WIDTH, DemoGame.GAME_HEIGHT));
        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(stage);
        initDemoChange(multiplexer);
        Gdx.input.setInputProcessor(multiplexer);
        findAllDemos();
        defaultFont = Gdx.files.internal("share/MLFZS.TTF");
        changeDemo();
    }

    private void findAllDemos() {
        demos = new ArrayList<String>();
        FileHandle[] demoFolder = Gdx.files.internal("./").list(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                String[] mainJsons = pathname.list(new FilenameFilter() {
                    @Override
                    public boolean accept(File dir, String name) {
                        return name.equalsIgnoreCase("MainScene.json");
                    }
                });
                return mainJsons != null && mainJsons.length > 0;
            }
        });
        for (FileHandle handle : demoFolder) {
            demos.add(handle.name());
        }
    }

    private void initDemoChange(InputMultiplexer multiplexer) {
        Stage demoChangeStage = new Stage(new StretchViewport(DemoGame.GAME_WIDTH, DemoGame.GAME_HEIGHT));
        Actor actor = new Actor();
        actor.setWidth(stage.getWidth());
        actor.setHeight(stage.getHeight());
        actor.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                updateCurrentIndex();
                changeDemo();
            }
        });
        demoChangeStage.addActor(actor);
        multiplexer.addProcessor(demoChangeStage);
    }

    private void changeDemo() {
        CocoStudioUIEditor editor = new CocoStudioUIEditor(
            Gdx.files.internal(demos.get(currentIndex) + File.separator + "MainScene.json"),
            null, null, defaultFont, null);
        stage.clear();
        Group group = editor.createGroup();
        stage.addActor(group);
    }

    private void updateCurrentIndex() {
        Gdx.app.debug("demo", "Change demo");
        currentIndex++;
        if (currentIndex >= demos.size()) {
            currentIndex = 0;
        }
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, false);
    }

}
