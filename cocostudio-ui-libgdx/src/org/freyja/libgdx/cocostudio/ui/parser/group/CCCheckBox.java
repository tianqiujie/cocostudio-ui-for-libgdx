package org.freyja.libgdx.cocostudio.ui.parser.group;

import org.freyja.libgdx.cocostudio.ui.CocoStudioUIEditor;
import org.freyja.libgdx.cocostudio.ui.model.ObjectData;
import org.freyja.libgdx.cocostudio.ui.parser.GroupParser;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * @tip libgdx的CheckBox只有选中和未选中两个状态的图片显示
 * @author i see
 * 
 */
public class CCCheckBox extends GroupParser {

	@Override
	public String getClassName() {
		return "CheckBoxObjectData";
	}

	@Override
	public Actor parse(CocoStudioUIEditor editor, ObjectData widget) {
		org.freyja.libgdx.cocostudio.ui.widget.TCheckBox.CheckBoxStyle style = new org.freyja.libgdx.cocostudio.ui.widget.TCheckBox.CheckBoxStyle(null, null, new BitmapFont(),
				Color.BLACK);

		if (widget.getNodeNormalFileData() != null) {// 选中图片

			style.checkboxOff = editor.findDrawable(widget,
					widget.getNodeNormalFileData());
		}
		if (widget.getNormalBackFileData() != null) {// 没选中图片
			style.checkboxOn = editor.findDrawable(widget,
					widget.getNormalBackFileData());
		}

        style.setTianCheckBox(editor.findDrawable(widget, widget.getNormalBackFileData())
                , editor.findDrawable(widget, widget.getPressedBackFileData())
                , editor.findDrawable(widget, widget.getDisableBackFileData())
                , editor.findDrawable(widget, widget.getNodeNormalFileData())
                , editor.findDrawable(widget, widget.getNodeDisableFileData()));

        org.freyja.libgdx.cocostudio.ui.widget.TCheckBox checkBox = new org.freyja.libgdx.cocostudio.ui.widget.TCheckBox("", style);
		checkBox.setChecked(widget.isDisplayState());
		checkBox.setDisabled(widget.isDisplayState());
		return checkBox;
	}
}
