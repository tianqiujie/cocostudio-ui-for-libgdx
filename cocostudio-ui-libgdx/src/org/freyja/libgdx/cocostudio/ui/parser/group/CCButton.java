package org.freyja.libgdx.cocostudio.ui.parser.group;

import java.lang.reflect.Method;
import java.util.WeakHashMap;

import org.freyja.libgdx.cocostudio.ui.CocoStudioUIEditor;
import org.freyja.libgdx.cocostudio.ui.model.ObjectData;
import org.freyja.libgdx.cocostudio.ui.parser.GroupParser;
import org.freyja.libgdx.cocostudio.ui.widget.TTFLabel;
import org.freyja.libgdx.cocostudio.ui.widget.TTFLabelStyle;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton.ImageButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;

public class CCButton extends GroupParser {

	@Override
	public String getClassName() {
		return "ButtonObjectData";
	}

	protected CocoStudioUIEditor editor;

	@Override
	public Actor parse(CocoStudioUIEditor editor, final ObjectData widget) {
		this.editor = editor;
        /*ImageButtonStyle style = new ImageButtonStyle(null, null, null,
				editor.findDrawable(widget, widget.getNormalFileData()),
				editor.findDrawable(widget, widget.getPressedFileData()), null);*/
        //

        final Button button;

        //分开解决TextButton和ImageButton
        if (widget.getButtonText() != null && !widget.getButtonText().equals("")){
            TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle(
                    editor.findDrawable(widget, widget.getNormalFileData()),
                    editor.findDrawable(widget, widget.getPressedFileData()),
                    null,
                    editor.createLabelStyleBitmapFint(widget, widget.getButtonText(), editor
                            .getColor(widget.getTextColor(), 0))
            );

            button = new TextButton(widget.getButtonText(), textButtonStyle);
        }else {

            ImageButtonStyle style = new ImageButtonStyle(
                    editor.findDrawable(widget, widget.getNormalFileData()),
                    editor.findDrawable(widget, widget.getPressedFileData()), null, null, null, null);

            style.imageDisabled = editor.findDrawable(widget,
                    widget.getDisabledFileData());

            button = new ImageButton(style);
            //已把textbutton和ImageButton分开，所以注释掉
            /*if (widget.getButtonText() != null
                    && !widget.getButtonText().equals("")) {
                TTFLabelStyle labelStyle = editor.createLabelStyle(widget,
                        widget.getButtonText(),
                        editor.getColor(widget.getTextColor(), 0));
                TTFLabel label = new TTFLabel(widget.getButtonText(), labelStyle);
                label.setPosition((button.getWidth() - label.getWidth()) / 2,
                        (button.getHeight() - label.getHeight()) / 2);
                button.addActor(label);
            }*/
        }

		button.setDisabled(widget.isDisplayState());

		if ("Click".equals(widget.getCallBackType())) {
			button.addListener(new ClickListener() {
				@Override
				public void clicked(InputEvent event, float x, float y) {
					invoke(button, widget.getCallBackName());
					super.clicked(event, x, y);
				}
			});
		} else if ("Touch".equals(widget.getCallBackType())) {

			button.addListener(new ClickListener() {
				@Override
				public boolean touchDown(InputEvent event, float x, float y,
						int pointer, int button2) {
					invoke(button, widget.getCallBackName());
					return super.touchDown(event, x, y, pointer, button2);
				}
			});
		}

		return button;
	}

}
