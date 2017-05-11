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
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Logger;
import com.badlogic.gdx.utils.viewport.StretchViewport;

import net.mwplay.cocostudio.ui.loader.CocosLoader;
import net.mwplay.cocostudio.ui.loader.CocosScene;
import net.mwplay.cocostudio.ui.util.LogUtil;
import net.mwplay.cocostudio.ui.util.T;
import net.mwplay.nativefont.NativeFont;
import net.mwplay.nativefont.NativeFontPaint;

public class AMScreen extends ScreenAdapter {
    private Stage stage;
    private Group root;
    private AssetManager assetManager;
    private NativeFont font;
    private GlyphLayout layout;

    @Override
    public void show() {
        super.show();
        layout = new GlyphLayout();
        font = new NativeFont(new NativeFontPaint(25));
        font.appendText("正在加载...0123456789%");
        font.setColor(Color.BLACK);
        layout.setText(font, "正在加载...100%");

        stage = new Stage(new StretchViewport(1280, 720));

        assetManager = new AssetManager();
        assetManager.setLogger(new Logger("log", Logger.DEBUG));
        assetManager.setLoader(CocosScene.class, new CocosLoader(new InternalFileHandleResolver()));
        assetManager.load("mainscene/MenuScene.json", CocosScene.class);
    }

    boolean init;
    @Override
    public void render(float delta) {
        super.render(delta);
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        if (!init) {
            if (assetManager.update()) {
                init = true;
                initUi();
            } else {
                LogUtil.log(assetManager.getProgress());
                stage.getBatch().begin();
                font.draw(stage.getBatch(),
                    "正在加载..." + (int) (assetManager.getProgress() * 100) + "%",
                    640 - layout.width / 2f, 360);
                stage.getBatch().end();
            }
        }

        stage.act();
        stage.draw();
    }

    private void initUi() {
        CocosScene cocosScene = assetManager.get("mainscene/MenuScene.json", CocosScene.class);
        root = cocosScene.getRoot();
        stage.addActor(cocosScene.getRoot(assetManager));
        Gdx.input.setInputProcessor(stage);

        T.at(root.findActor("createroom")).isColorButton(myListener);
        T.at(root.findActor("joinroom")).isColorButton(myListener);
        T.at(root.findActor("zhanji")).isColorButton(myListener);
        T.at(root.findActor("share")).isColorButton(myListener);
        T.at(root.findActor("feedback")).isColorButton(myListener);
        T.at(root.findActor("xiaoxi")).isColorButton(myListener);
        T.at(root.findActor("shezhi")).isColorButton(myListener);
        T.at(root.findActor("bangzhu")).isColorButton(myListener);

        for (int i = 0; i < 12; i++) {
            T.at(root.findActor("num" + i)).isColorButton(new MyJoinListener());
        }

        T.at(root.findActor("joinroom_close")).isColorButton(myListener);
        T.at(root.findActor("setting_close")).isColorButton(myListener);

        nums = new Label[6];
        for (int i = 0; i < nums.length; i++) {
            nums[i] = root.findActor("N" + (i + 1));
            nums[i].setText("");
        }
    }

    Label nums[];

    MyListener myListener = new MyListener();

    private class MyListener implements T.TClickListener {
        @Override
        public void onClick(Actor actor) {
            LogUtil.log("clicked: " + actor.getName());
            switch (actor.getName()) {
                case "createroom":
                    break;
                case "joinroom":
                    root.findActor("pl_joinroom").setVisible(true);
                    for (int i = 0; i < nums.length; i++) {
                        nums[i].setText("");
                    }
                    break;
                case "joinroom_close":
                    root.findActor("pl_joinroom").setVisible(false);
                    break;
                case "setting_close":
                    root.findActor("pl_setting").setVisible(false);
                    break;
                case "shezhi":
                    root.findActor("pl_setting").setVisible(true);
                    break;
            }
        }
    }

    /**
     * 加入房间的键盘事件
     */
    class MyJoinListener implements T.TClickListener {
        @Override
        public void onClick(Actor actor) {
            int num = Integer.parseInt(actor.getName().replace("num", ""));
            switch (num) {
                case 10:// 重输入
                    for (Label numLabel : nums) {
                        numLabel.setText("");
                    }
                    break;
                case 11:// 删除
                    for (int i = nums.length - 1; i >= 0; i--) {
                        if (nums[i].getText().length > 0) {
                            nums[i].setText("");
                            break;
                        }
                    }
                    break;
                case 0:
                case 1:
                case 2:
                case 3:
                case 4:
                case 5:
                case 6:
                case 7:
                case 8:
                case 9:
                    for (Label numLabel : nums) {
                        if (numLabel.getText().length == 0) {
                            numLabel.setText(num + "");
                            break;
                        }
                    }
                    break;
            }
            /*if (nums[nums.length - 1].getText().length() == 1) {
                String roomNum = "";
                for (Label lable : nums) {
                    roomNum += lable.getText();
                }
                for (Label lable : nums) {
                    lable.setText("");
                }
            }*/
        }
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        stage.getViewport().update(width, height);
    }
}
