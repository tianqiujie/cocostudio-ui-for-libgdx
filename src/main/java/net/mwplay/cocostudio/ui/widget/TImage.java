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
package net.mwplay.cocostudio.ui.widget;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;

public class TImage extends Image {
    private boolean isAnimate;
    public Rectangle box = new Rectangle();

    public TImage() {
        init();
    }

    public TImage(NinePatch patch) {
        super(patch);
        init();
    }

    public TImage(TextureRegion region) {
        super(region);
        init();
    }

    public TImage(Texture texture) {
        super(texture);
        init();
    }

    public TImage(String path) {
        Texture texture = new Texture(path);
        texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        setDrawable(new TextureRegionDrawable(new TextureRegion(texture)));
        setSize(texture.getWidth(), texture.getHeight());
        init();
    }

    public TImage(Skin skin, String drawableName) {
        super(skin, drawableName);
        init();
    }

    public TImage(Drawable drawable) {
        super(drawable);
        init();
    }

    public TImage(Drawable drawable, Scaling scaling) {
        super(drawable, scaling);
        init();
    }

    public TImage(Drawable drawable, Scaling scaling, int align) {
        super(drawable, scaling, align);
        init();
    }

    protected void init() {
        box.setSize(getWidth(), getHeight());
        origonCenter();
    }

    public TImage toCenterOf(Actor actor) {
        setPosition(actor.getX() + actor.getWidth() / 2f - getWidth() / 2f, actor.getY() + actor.getHeight() / 2f - getHeight() / 2f);
        return this;
    }

    public TImage width(float width) {
        setWidth(width);
        return this;
    }

    public TImage height(float height) {
        setHeight(height);
        return this;
    }

    public float width() {
        return getWidth();
    }

    public float height() {
        return getHeight();
    }

    public TImage disableTouch() {
        setTouchable(Touchable.disabled);
        return this;
    }

    public TImage enableTouch() {
        setTouchable(Touchable.enabled);
        return this;
    }

    public TImage drawable(Drawable drawable) {
        setDrawable(drawable);
        return this;
    }

    public TImage toCenterXOf(Actor actor) {
        setX(actor.getX() + actor.getWidth() / 2f - getWidth() / 2f);
        return this;
    }

    public TImage toCenterYOf(Actor actor) {
        setY(actor.getY() + actor.getHeight() / 2f - getHeight() / 2f);
        return this;
    }

    public TImage toStageCenter(Stage stage) {
        posCenter(stage.getWidth() / 2f, stage.getHeight() / 2f);
        return this;
    }

    public TImage toStageXCenter(Stage stage) {
        x(stage.getWidth() / 2f - getWidth() / 2f);
        return this;
    }

    public TImage toStageYCenter(Stage stage) {
        y(stage.getHeight() / 2f - getHeight() / 2f);
        return this;
    }

    public TImage toLeftOf(Actor actor) {
        setPosition(actor.getX() - getWidth(), actor.getY());
        return this;
    }

    public TImage toLeftOf(Actor actor, int align) {
        setPosition(actor.getX() - getWidth(), actor.getY(), align);
        return this;
    }

    public TImage toRightOf(Actor actor) {
        setPosition(actor.getRight(), actor.getY());
        return this;
    }

    public TImage toTopOf(Actor actor) {
        setPosition(actor.getX(), actor.getTop());
        return this;
    }

    public TImage toBottomOf(Actor actor) {
        setPosition(actor.getX(), actor.getY() - getHeight());
        return this;
    }

    public TImage toLeftTopOf(Actor actor) {
        setPosition(actor.getX() - getWidth(), actor.getTop());
        return this;
    }

    public TImage toLeftBottomOf(Actor actor) {
        setPosition(actor.getX(), actor.getY());
        return this;
    }

    public TImage addTo(Stage stage) {
        stage.addActor(this);
        return this;
    }

    public TImage addTo(Group group) {
        group.addActor(this);
        return this;
    }

    public TImage add(Group group) {
        group.addActor(this);
        return this;
    }

    public TImage pos(Vector2 pos) {
        setPosition(pos.x, pos.y);
        return this;
    }

    public TImage pos(Actor actor) {
        setPosition(actor.getX(), actor.getY());
        return this;
    }

    public TImage pos(float x, float y) {
        setPosition(x, y);
        return this;
    }

    public TImage pos(float x, float y, int align) {
        setPosition(x, y, align);
        return this;
    }

    /**
     * 代表可以拖动,方便调试
     *
     * @return
     */
    /*public TImage drag() {
        //MyWidget.setTouchTrack(this);
        return this;
    }*/
    @Override
    public void setPosition(float x, float y) {
        super.setPosition(x, y);
        box.setX(x);
        box.setY(y);
    }

    public TImage scale(float scale) {
        setSize(getWidth() * scale, getHeight() * scale);
        origonCenter();
        return this;
    }

    public TImage realScale(float scale) {
        // origonCenter();
        setScale(scale);
        return this;
    }

    public TImage scaleByH(float h) {
        float i = h / getHeight();
        setHeight(h);
        setWidth(getWidth() * i);
        origonCenter();
        return this;
    }

    public TImage scale(float scale, int align) {
        setSize(getWidth() * scale, getHeight() * scale);
        setAlign(align);
        return this;
    }

    public TImage scale(float scaleX, float scaleY) {
        setSize(getWidth() * scaleX, getHeight() * scaleY);
        return this;
    }

    public TImage scaleX(float scaleX) {
        setWidth(getWidth() * scaleX);
        return this;
    }

    public TImage scaleY(float scaleY) {
        setHeight(getHeight() * scaleY);
        return this;
    }

    public TImage origon(int align) {
        setOrigin(align);
        return this;
    }

    public TImage origon(Actor actor) {
        setOrigin(actor.getOriginX(), actor.getOriginY());
        return this;
    }

    public TImage origonCenter() {
        setOrigin(Align.center);
        return this;
    }

    public TImage origon(int alignX, int alignY) {
        setOrigin(alignX, alignY);
        return this;
    }

    public TImage posCenter(float x, float y) {
        setPosition(x - getWidth() / 2f, y - getHeight() / 2f);
        return this;
    }

    public TImage posCenter(Vector2 pos) {
        setPosition(pos.x - getWidth() / 2f, pos.y - getHeight() / 2f);
        return this;
    }

    public TImage offsetX(float x) {
        setX(getX() + x);
        return this;
    }

    public TImage offsetY(float y) {
        setY(getY() + y);
        return this;
    }

    public TImage offset(float value) {
        offsetX(value);
        offsetY(value);
        return this;
    }

    public TImage size(float w, float h) {
        setSize(w, h);
        origonCenter();
        return this;
    }

    public TImage size(Actor actor) {
        setSize(actor.getWidth(), actor.getHeight());
        origonCenter();
        return this;
    }

    private Animation animation;
    private float stateTime;
    private boolean isPlaying;

    // 动画
    public void isAnimate(Animation ani) {
        if (ani == null) {
            return;
        }
        this.animation = ani;
        isAnimate = true;
        ((TextureRegionDrawable) getDrawable()).setRegion((TextureRegion) animation.getKeyFrame(0));
    }

    public void play() {
        if (animation != null) {
            isPlaying = true;
        }
    }

    public void stop() {
        isPlaying = false;
    }

    public boolean hasAnimation() {
        return this.isAnimate;
    }

    @Override
    public void act(float delta) {
        if (animation != null && isPlaying) {
            ((TextureRegionDrawable) getDrawable()).setRegion((TextureRegion) animation.getKeyFrame(stateTime += delta, true));
        }
        super.act(delta);
    }

    /**
     * @param onClickListener
     * @return
     */
    public TImage listener(EventListener onClickListener) {
        addListener(onClickListener);
        return this;
    }

    TClickListener clickListener;

    public void setClickListener(TClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public TImage isButton() {
        clearListeners();
        origonCenter();
        addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                addAction(Actions.sequence(Actions.scaleTo(0.9f, 0.9f, 0.1f), Actions.scaleTo(1, 1, 0.1f), Actions.run(new Runnable() {
                    @Override
                    public void run() {
                        if (clickListener != null) {
                            clickListener.onClicked(TImage.this);
                        }
                    }
                })));
            }
        });
        return this;
    }

    public TImage isNoButton() {
        clearListeners();
        origonCenter();
        addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                if (clickListener != null) {
                    clickListener.onClicked(TImage.this);
                }
            }
        });
        return this;
    }

    public TImage isColorButton() {
        clearListeners();
        addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                event.getListenerActor().setColor(0.5f, 0.5f, 0.5f, 1f);
                //MyAssetUtil.playSound(MyGame.assetManager, "start", "ui_click", MyGame.soundVolume);
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
                event.getListenerActor().setColor(Color.WHITE);
                if (clickListener != null) {
                    clickListener.onClicked(TImage.this);
                }
            }
        });
        return this;
    }

    public float centerX() {
        return getX() + getWidth() / 2f;
    }

    public float centerY() {
        return getY() + getHeight() / 2f;
    }

    /**
     * 在actor的内部的右边
     *
     * @param actor
     * @return
     */
    public TImage inRightOf(Actor actor) {
        setPosition(actor.getWidth() - getWidth(), 0);
        return this;
    }

    public TImage inLeftOf(Actor actor) {
        setPosition(0, 0);
        return this;
    }

    public TImage inTopOf(Actor actor) {
        setY(actor.getHeight() - getHeight());
        return this;
    }

    public TImage inCenterXOf(Actor container) {
        setX(container.getWidth() / 2f - getWidth() / 2f);
        return this;
    }

    public TImage inCenterYOf(Actor container) {
        setY(container.getHeight() / 2f - getHeight() / 2f);
        return this;
    }

    public TImage inCenterOf(Actor container) {
        setX(container.getWidth() / 2f - getWidth() / 2f);
        setY(container.getHeight() / 2f - getHeight() / 2f);
        return this;
    }

    public TImage x(float x) {
        setX(x);
        return this;
    }

    public TImage y(float y) {
        setY(y);
        return this;
    }

    public TImage name(Object name) {
        setName(name.toString());
        return this;
    }

    public String name() {
        return getName();
    }

    public TImage replace(Actor actor) {
        addTo(actor.getParent());
        size(actor);
        pos(actor);
        origon(actor);
        setZIndex(actor.getZIndex());
        return this;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        box.set(getX(), getY(), getWidth(), getHeight());
    }

    public TImage debug() {
        super.debug();
        return this;
    }

    public Vector2 getPos() {
        return new Vector2(getX(), getY());
    }

    public Vector2 getCenterPos() {
        return new Vector2(getX() + getWidth() / 2f, getY() + getHeight() / 2f);
    }

    public TImage hide() {
        if (isVisible()) {
            setVisible(false);
        }
        return this;
    }

    public TImage visiable() {
        if (!isVisible()) {
            setVisible(true);
        }
        return this;
    }

    public TImage visiable(boolean visable) {
        setVisible(visable);
        return this;
    }

    public Vector2 size() {
        return new Vector2(getWidth(), getHeight());
    }

    public TImage front() {
        toFront();
        return this;
    }

    public Rectangle copyBox() {
        return new Rectangle(box);
    }

    public Rectangle makeLeftUp(float width) {
        return new Rectangle(getX() + 5, getTop() - width, width, width);
    }

    public Rectangle copyBox(float scale) {
        Rectangle rectangle = new Rectangle(box);
        rectangle.setSize(rectangle.getWidth() / 2f, rectangle.getHeight() / 2f);
        return rectangle;
    }

    public TImage roateAction(float angle, float duration, boolean forever) {
        if (forever) {
            addAction(Actions.forever(Actions.rotateBy(angle, duration)));
        } else {
            addAction(Actions.rotateBy(angle, duration));
        }
        return this;
    }

    /**
     * 水平中线对齐
     *
     * @param midMenubg
     */
    public TImage alignCenter(TImage midMenubg) {
        setY(midMenubg.centerY() - getHeight() / 2f);
        return this;
    }

    public TImage rotation(int roation) {
        setRotation(roation);
        return this;
    }

    public static TImage makeAlphaImage(float w, float h) {
        Pixmap pixmap = new Pixmap((int) w, (int) h, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.BLACK);
        pixmap.fill();
        Texture texture = new Texture(pixmap);
        TImage image = new TImage(texture);
        image.addAction(Actions.alpha(0.5f));
        return image;
    }

    public static TImage makeImage(float w, float h, Color color) {
        Pixmap pixmap = new Pixmap((int) w, (int) h, Pixmap.Format.RGBA8888);
        pixmap.setColor(color);
        pixmap.fill();
        Texture texture = new Texture(pixmap);
        TImage image = new TImage(texture);
        return image;
    }

    public TImage width(Actor actor) {
        setWidth(actor.getWidth());
        return this;
    }

    /**
     * 创建人：Administrator 邮箱：tqj.zyy@gmail.com 创建时间：2016/6/27 16:46
     * 修改人：Administrator 修改时间：2016/6/27 16:46 修改备注：
     */
    public interface TClickListener {
        public void onClicked(TImage image);
    }
}
