package net.mwplay.cocostudio.ui.widget;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TransformDrawable;
import com.badlogic.gdx.utils.Scaling;

/**
 * Created by tian on 2016/3/24.
 */
public class TLoadingBar extends Image{
    private int value = 100;
    private TextureRegion bar;

    public void setBar(TextureRegion bar) {
        this.bar = bar;
    }

    public TextureRegion getBar() {
        return bar;
    }

    public TLoadingBar() {
    }

    public TLoadingBar(NinePatch patch) {
        super(patch);
    }

    public TLoadingBar(TextureRegion region) {
        super(region);
    }

    public TLoadingBar(Texture texture) {
        super(texture);
    }

    public TLoadingBar(Skin skin, String drawableName) {
        super(skin, drawableName);
    }

    public TLoadingBar(Drawable drawable) {
        super(drawable);
    }

    public TLoadingBar(Drawable drawable, Scaling scaling) {
        super(drawable, scaling);
    }

    public TLoadingBar(Drawable drawable, Scaling scaling, int align) {
        super(drawable, scaling, align);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        validate();

        Color color = getColor();
        batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);

        float x = getX();
        float y = getY();
        float scaleX = getScaleX();
        float scaleY = getScaleY();
        Drawable drawable = getDrawable();

        float imageX = getImageX();
        float imageY = getImageY();
        float imageWidth = getImageWidth();
        float imageHeight = getImageHeight();

        if (drawable instanceof TransformDrawable) {
            float rotation = getRotation();
            if (scaleX != 1 || scaleY != 1 || rotation != 0) {
               // ((TransformDrawable)drawable).draw(batch, x + imageX, y + imageY, getOriginX() -
               //                 imageX, getOriginY() - imageY,
               //         imageWidth, imageHeight, scaleX, scaleY, rotation);

                batch.draw(bar, x + imageX, y + imageY, getOriginX() - imageX, getOriginY() -
                        imageY, imageWidth, imageHeight, scaleX, scaleY, rotation);

                return;
            }
        }

        if (drawable != null) drawable.draw(batch, x + imageX, y + imageY, imageWidth * scaleX,
                imageHeight * scaleY);
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
