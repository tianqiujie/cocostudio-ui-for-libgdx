package net.mwplay.cocos.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import net.mwplay.cocos.MyGdxGame;
import net.mwplay.cocos.TianScreen;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.width = TianScreen.GameWidth;
        config.height = TianScreen.GameHeight;

		new LwjglApplication(new MyGdxGame(), config);
	}
}
