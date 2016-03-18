package net.mwplay.cocos;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.StretchViewport;

import org.freyja.libgdx.cocostudio.ui.CocoStudioUIEditor;

/**
 * Created by tian on 2016/3/18.
 */

public class TianScreen extends ScreenAdapter{
    Stage stage;
    public static final int GameWidth = 960;
    public static final int GameHeight = 640;

    @Override
    public void show() {
        super.show();
        stage = new Stage(new StretchViewport(GameWidth, GameHeight));

        InputMultiplexer inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(stage);
        Gdx.input.setInputProcessor(inputMultiplexer);

        initDemo();
    }

    private void initDemo() {
        FileHandle defaultFont = Gdx.files
                .internal("demo/FangZhengZhunYuan_GBK.TTF");

		/*CocoStudioUIEditor editor = new CocoStudioUIEditor(
				Gdx.files.internal("demo/Layer.json"), null, null, defaultFont,
				null);*/

        CocoStudioUIEditor editor = new CocoStudioUIEditor(
                Gdx.files.internal("MainScene.json"), null, null, defaultFont,
                null);

        Group group = editor.createGroup();
        stage.addActor(group);
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
